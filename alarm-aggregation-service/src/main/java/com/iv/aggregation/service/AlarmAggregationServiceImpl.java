package com.iv.aggregation.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iv.aggregation.api.constant.OpsType;
import com.iv.aggregation.api.constant.StrategyCycle;
import com.iv.aggregation.api.dto.AlarmLifeDto;
import com.iv.aggregation.api.dto.AlarmLogDto;
import com.iv.aggregation.api.dto.AlarmPagingDto;
import com.iv.aggregation.api.dto.AlarmQueryDto;
import com.iv.aggregation.api.dto.AlarmRecoveryDto;
import com.iv.aggregation.api.dto.AlarmSourceDto;
import com.iv.aggregation.api.dto.AlarmTransferDto;
import com.iv.aggregation.api.service.IAlarmAggregationService;
import com.iv.aggregation.dao.AlarmPaging;
import com.iv.aggregation.dao.IAlarmCleanStrategyDao;
import com.iv.aggregation.dao.IAlarmLifeDao;
import com.iv.aggregation.dao.impl.AlarmCleanStrategyDaoImpl;
import com.iv.aggregation.dao.impl.AlarmLifeDaoImpl;
import com.iv.aggregation.entity.AlarmCleanStrategyEntity;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmLogEntity;
import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.aggregation.feign.clients.IMessageServiceClient;
import com.iv.aggregation.feign.clients.IUserServiceClient;
import com.iv.aggregation.util.WechatProxyClient;
import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.NoticeType;
import com.iv.common.enumeration.SendType;
import com.iv.common.response.ErrorMsg;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.common.util.spring.SpringContextUtil;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.dto.AlarmLifeEntityDto;
import com.iv.message.api.dto.AlarmMsgDto;
import com.iv.outer.dto.LocalAuthDto;

import net.sf.json.JSONObject;

/**
 * 告警北向支撑服务
 * 
 * @author macheng 2018年4月11日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@RestController
public class AlarmAggregationServiceImpl implements IAlarmAggregationService {

	private static final IAlarmLifeDao ALARM_LIFE_DAO = AlarmLifeDaoImpl.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmAggregationServiceImpl.class);

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private AlarmCleanStrategyDaoImpl cleanStrategyDao;
	@Autowired
	private IEmailServiceClient emailServiceClient;
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private WechatProxyClient wechatProxyClient;
	@Autowired
	private IMessageServiceClient messageServiceClient;

	@Override
	public ResponseDto claimAlarm(String token, String lifeId) {

		JSONObject session = JWTUtil.getJWtJson(token);
		int userId = session.getInt("userId");
		String realName = session.getString("realName");
		try {
			AlarmLifeEntity alarmLifeEntity = ALARM_LIFE_DAO.selectAlarmLifeById(lifeId);
			if (null != alarmLifeEntity) {
				alarmLifeEntity.setHandlerCurrent(userId);
				alarmLifeEntity.setAlarmStatus(AlarmStatus.PROCESSING);
				if (alarmLifeEntity.getLogs().size() == 1) {
					alarmLifeEntity.setResDate(System.currentTimeMillis());
				}
				alarmLifeEntity.getLogs().add(AlarmLogEntity.builder(new Date(), OpsType.CLAIM, realName, null, 0));
				ALARM_LIFE_DAO.updateAlarmLife(alarmLifeEntity);
			}
		} catch (RuntimeException e) {

			LOGGER.error("", e);
			return ResponseDto.builder(ErrorMsg.UNKNOWN);
		}
		return ResponseDto.builder(ErrorMsg.OK);
	}

	@Override
	public ResponseDto transferAlarm(@RequestBody AlarmTransferDto dto) {
		// 获取用户基本信息
		LocalAuthDto toUser = userServiceClient.selectLocalAuthById(dto.getToUser());
		ArrayList<String> toEmails = new ArrayList<String>(1);
		String transferee = null;
		if(null != toUser) {
			// 被转让人
			transferee = toUser.getRealName() == null ? toUser.getUserName() : toUser.getRealName();
			toEmails.add(toUser.getEmail());
		}
		AlarmLifeEntity alarmLifeEntity = ALARM_LIFE_DAO.selectAlarmLifeById(dto.getLifeId());
		if (null != alarmLifeEntity) {
			
			boolean flag = false;
			// 已绑定微信，模板消息推送
			// 获取告警源信息
			Object map0 = redisTemplate.opsForValue().get(alarmLifeEntity.getAlarm().getTenantId());
			if (null != map0) {
				//Map<String, AlarmSourceEntity> map = (Map<String, AlarmSourceEntity>) map0;
				//AlarmSourceEntity alarmSourceEntity = map.get(alarmLifeEntity.getAlarm().getAlarmId());
				// 微信通知被转让人
				try {
					wechatProxyClient.alarmNotice(alarmLifeEntity, null, null, dto.getToUser());
					flag = true;
				} catch (Exception e) {
					LOGGER.error("告警转让：微信通知失败");
				}
				
			}
			
			// 调用邮件服务
			try {
				if(!toEmails.isEmpty()) {
					AlarmInfoTemplate alarmInfoTemplate = new AlarmInfoTemplate();
					alarmInfoTemplate.setToEmails(toEmails.toArray(new String[toEmails.size()]));
					alarmInfoTemplate.setEmailType(SendType.ALARMTRIGGER);
					AlarmLifeEntityDto alarmLifeEntityDto = new AlarmLifeEntityDto();
					alarmLifeEntityDto.setAlarmStatus(alarmLifeEntity.getAlarmStatus());
					alarmLifeEntityDto.setContent(alarmLifeEntity.getAlarm().getContent());
					alarmLifeEntityDto.setCurrentHandlerId(alarmLifeEntity.getHandlerCurrent());
					alarmLifeEntityDto.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
					alarmLifeEntityDto.setHostName(alarmLifeEntity.getAlarm().getHostName());
					alarmLifeEntityDto.setId(alarmLifeEntity.getId());
					alarmLifeEntityDto.setSeverity(alarmLifeEntity.getAlarm().getSeverity());
					alarmLifeEntityDto.setTime(new Date(alarmLifeEntity.getTriDate()).toLocaleString());
					alarmLifeEntityDto.setTitle(alarmLifeEntity.getAlarm().getTitle());
					alarmLifeEntityDto.setUpgrade(alarmLifeEntityDto.getUpgrade());
					alarmInfoTemplate.setAlarmLifeEntity(alarmLifeEntityDto);
					emailServiceClient.alarmToMail(alarmInfoTemplate);
					flag = true;
				}
			} catch (Exception e) {
				LOGGER.error("告警转让：邮件通知失败");
			}
			if(!flag) {
				// 所有通知失败，转让未成功
				return ResponseDto.builder(ErrorMsg.UNKNOWN);
			}
			
			// 加入处理人信息
			alarmLifeEntity.getToHireUserIds().add(dto.getToUser());
			Date date = new Date();
			if(alarmLifeEntity.getLogs().size() == 1) {
				alarmLifeEntity.setResDate(date.getTime());
			}
			// 记录操作日志
			alarmLifeEntity.getLogs()
					.add(AlarmLogEntity.builder(date, OpsType.TRANSFER, transferee, dto.getRemark(),0));
			// 修改当前处理人
			// alarmLifeEntity.setHandlerCurrent(hireUser);
			// 更新告警状态为待处理
			alarmLifeEntity.setAlarmStatus(AlarmStatus.PENDING);
			ALARM_LIFE_DAO.saveOrUpdateAlarmLife(alarmLifeEntity);
			/*GroupEntity groupEntity = I_Group_Dao.selectGroupById(event.getGroupId());
			groupEntity.getAlarmLifeEntity().add(alarmLifeEntity);
			I_Group_Dao.saveGroup(groupEntity);*/
			//保存转让消息
			AlarmMsgDto msgDto = new AlarmMsgDto();
			msgDto.setUserIds(Arrays.asList(dto.getToUser()));
			msgDto.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
			msgDto.setMsgDate(System.currentTimeMillis());
			msgDto.setAlarmId(alarmLifeEntity.getId());
			msgDto.setType(NoticeType.ALARM);
			msgDto.setTitle(alarmLifeEntity.getAlarm().getTitle());
			msgDto.setHostName(alarmLifeEntity.getAlarm().getHostName());
			msgDto.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
			msgDto.setTriDate(alarmLifeEntity.getTriDate());
			msgDto.setAlarmStatus(alarmLifeEntity.getAlarmStatus());
			// 调用用户消息服务
			messageServiceClient.produceAlarmMsg(msgDto);
			return ResponseDto.builder(ErrorMsg.OK);
		}
		return ResponseDto.builder(ErrorMsg.UNKNOWN);
		
	}

	@Override
	public AlarmPagingDto getMyAlarmPaging(@RequestBody AlarmQueryDto query) {
		// 查询我的告警
		AlarmPagingDto alarmPagingDto = getAlarmLifeDto(
				ALARM_LIFE_DAO.selectPaging((query.getCurPage() - 1) * query.getItems(), query.getItems(), query, query.getHandlerCurrent()));
		return alarmPagingDto;
	}

	@Override
	public AlarmPagingDto getAlarmPaging(@RequestBody AlarmQueryDto query) {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		AlarmPagingDto alarmPagingDto = getAlarmLifeDto(
				ALARM_LIFE_DAO.selectPaging((query.getCurPage() - 1) * query.getItems(), query.getItems(), query, null));
		return alarmPagingDto;

	}
	
	/**
	 * 获取告警数据详情
	 * 
	 * @param alarmId
	 */
	@Override
	public ResponseDto getAlarmDetails(String lifeId) {

		try {
			String[] strs = lifeId.split("\\$");
			lifeId = strs[0];
			String tenantId = strs[1];
			AlarmLifeEntity alarmLifeEntity = ALARM_LIFE_DAO.selectAlarmLifeById(lifeId, tenantId);
			AlarmLifeDto lifeDto = alarmLifeDtoConvert(alarmLifeEntity);
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(lifeDto);
			return responseDto;
		} catch (RuntimeException e) {
			LOGGER.error("获取告警数据失败", e);
			return ResponseDto.builder(ErrorMsg.GET_DATA_FAILED);
		}
	}

	/**
	 * 封装告警数据
	 * 
	 * @param alarmPaging
	 * @return
	 */
	public AlarmPagingDto getAlarmLifeDto(AlarmPaging alarmPaging) {
		AlarmPagingDto alarmPagingDto = new AlarmPagingDto();
		if (alarmPaging.getTotalCount() > 0) {
			alarmPagingDto.setTotalCount(alarmPaging.getTotalCount());
			List<AlarmLifeDto> AlarmLifeDtos = new ArrayList<AlarmLifeDto>();
			for (AlarmLifeEntity alarmLifeEntity : alarmPaging.getEntries()) {
				AlarmLifeDtos.add(alarmLifeDtoConvert(alarmLifeEntity));
			}
			alarmPagingDto.setEntries(AlarmLifeDtos);
		}else {
			alarmPagingDto.setEntries(new ArrayList<AlarmLifeDto>());
		}
		return alarmPagingDto;
	}
	
	/**
	 * 实体类转换
	 * @param alarmLifeEntity
	 * @return
	 */
	public AlarmLifeDto alarmLifeDtoConvert(AlarmLifeEntity alarmLifeEntity) {
		AlarmLifeDto alarmLifeDto = new AlarmLifeDto();
		// copy entity
		AlarmSourceDto alarmSourceDto = new AlarmSourceDto();
		alarmLifeDto.setAlarm(alarmSourceDto);
		BeanUtils.copyProperties(alarmLifeEntity.getAlarm(), alarmSourceDto);
		AlarmRecoveryDto alarmRecoveryDto = new AlarmRecoveryDto();
		if(null != alarmLifeEntity.getRecovery()) {
			BeanUtils.copyProperties(alarmLifeEntity.getRecovery(), alarmRecoveryDto);
			alarmLifeDto.setRecovery(alarmRecoveryDto);
		}
		TreeSet<AlarmLogDto> alarmLogDtos = new TreeSet<AlarmLogDto>();
		for (AlarmLogEntity entity : alarmLifeEntity.getLogs()) {
			AlarmLogDto dto = new AlarmLogDto();
			BeanUtils.copyProperties(entity, dto);
			alarmLogDtos.add(dto);
		}

		alarmLifeDto.setLogs(alarmLogDtos);
		alarmLifeDto.setAlarmStatus(alarmLifeEntity.getAlarmStatus());
		if(alarmLifeEntity.getHandlerCurrent() != 0) {
			LocalAuthDto handlerCurrentDto = userServiceClient.selectLocalAuthById(alarmLifeEntity.getHandlerCurrent());
			String handlerCurrent = handlerCurrentDto == null ? null : handlerCurrentDto.getRealName();
			alarmLifeDto.setHandlerCurrent(handlerCurrent);
		}
		if(alarmLifeEntity.getHandlerCurrent() != 0) {
			LocalAuthDto handlerLastDto = userServiceClient.selectLocalAuthById(alarmLifeEntity.getHandlerCurrent());
			String handlerLast = handlerLastDto == null ? null : handlerLastDto.getRealName();
			alarmLifeDto.setHandlerLast(handlerLast);
		}
		alarmLifeDto.setHostAlarmNum(alarmLifeEntity.getHostAlarmNum());
		alarmLifeDto.setId(alarmLifeEntity.getId());
		alarmLifeDto.setItemType(alarmLifeEntity.getItemType());

		alarmLifeDto.setRecDate(alarmLifeEntity.getRecDate());
		alarmLifeDto.setTriDate(alarmLifeEntity.getTriDate());
		alarmLifeDto.setUpgrade(alarmLifeEntity.getUpgrade());
		return alarmLifeDto;
	}

	@Override
	public ResponseDto updateAlarmCleanQuartz(String exp) {
		try {
			Scheduler scheduler = SpringContextUtil.getBean(SchedulerFactoryBean.class).getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey("cronTriggerBean", "DEFAULT");
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null == trigger) {
				System.out.println("不存在trigger");
			} else {
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(exp);
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}

			return ResponseDto.builder(ErrorMsg.OK);

		} catch (SchedulerException e) {
			LOGGER.error("告警清理后台任务更新失败", e);
			return ResponseDto.builder(ErrorMsg.UNKNOWN);
		}
	}

	@Override
	public ResponseDto updateAlarmCleanCycle(StrategyCycle cycle) {
		try {
			AlarmCleanStrategyEntity cleanStrategyEntity = new AlarmCleanStrategyEntity();
			cleanStrategyEntity.setCycleType(cycle);
			cleanStrategyEntity.setId(IAlarmCleanStrategyDao.id);
			cleanStrategyDao.saveStrategy(cleanStrategyEntity);
			return ResponseDto.builder(ErrorMsg.OK);

		} catch (RuntimeException e) {
			LOGGER.error("告警清理后台任务更新失败", e);
			return ResponseDto.builder(ErrorMsg.UNKNOWN);
		}
	}

}
