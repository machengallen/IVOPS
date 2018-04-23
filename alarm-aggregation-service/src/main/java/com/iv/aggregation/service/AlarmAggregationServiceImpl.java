package com.iv.aggregation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.iv.aggregation.api.constant.AlarmStatus;
import com.iv.aggregation.api.constant.OpsType;
import com.iv.aggregation.api.constant.StrategyCycle;
import com.iv.aggregation.api.dto.AlarmQueryDto;
import com.iv.aggregation.api.dto.AlarmTransferDto;
import com.iv.aggregation.api.service.IAlarmAggregationService;
import com.iv.aggregation.dao.AlarmPaging;
import com.iv.aggregation.dao.IAlarmCleanStrategyDao;
import com.iv.aggregation.dao.IAlarmLifeDao;
import com.iv.aggregation.dao.impl.AlarmCleanStrategyDaoImpl;
import com.iv.aggregation.dao.impl.AlarmLifeDaoImpl;
import com.iv.aggregation.dao.impl.AlarmMsgDaoImpl;
import com.iv.aggregation.entity.AlarmCleanStrategyEntity;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmLogEntity;
import com.iv.aggregation.entity.AlarmMsgEntity;
import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.aggregation.feign.clients.IUserServiceClient;
import com.iv.aggregation.feign.clients.IWechatServiceClient;
import com.iv.aggregation.util.WechatProxyClient;
import com.iv.common.enumeration.NoticeType;
import com.iv.common.enumeration.SendType;
import com.iv.common.response.ErrorMsg;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.common.util.spring.SpringContextUtil;
import com.iv.facade.dto.AlarmLifeDto;
import com.iv.facade.dto.AlarmLogDto;
import com.iv.facade.dto.AlarmPagingDto;
import com.iv.facade.dto.AlarmRecoveryDto;
import com.iv.facade.dto.AlarmSourceDto;
import com.iv.outer.dto.LocalAuthDto;

import net.sf.json.JSONObject;

/**
 * 告警北向支撑服务
 * 
 * @author macheng 2018年4月11日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public class AlarmAggregationServiceImpl implements IAlarmAggregationService {

	private static final IAlarmLifeDao ALARM_LIFE_DAO = AlarmLifeDaoImpl.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmAggregationServiceImpl.class);

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private AlarmMsgDaoImpl alarmMsgDao;
	@Autowired
	private AlarmCleanStrategyDaoImpl cleanStrategyDao;
	@Autowired
	private IEmailServiceClient emailServiceClient;
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private WechatProxyClient wechatProxyClient;
	@Autowired
	private IWechatServiceClient wechatServiceClient;

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
				Date date = new Date();
				if (alarmLifeEntity.getLogs().size() == 1) {
					alarmLifeEntity.getAlarmEvent().setResDate(date.getTime());
				}
				alarmLifeEntity.getLogs().add(AlarmLogEntity.builder(date, OpsType.CLAIM, realName, null, 0));
				ALARM_LIFE_DAO.updateAlarmLife(alarmLifeEntity);
			}
		} catch (RuntimeException e) {

			LOGGER.error("", e);
			return ResponseDto.builder(ErrorMsg.UNKNOWN);
		}
		return ResponseDto.builder(ErrorMsg.OK);
	}

	@Override
	public ResponseDto transferAlarm(AlarmTransferDto dto) {
		// 获取用户基本信息
		LocalAuthDto toUser = userServiceClient.selectLocalAuthById(dto.getToUser());
		
		AlarmLifeEntity alarmLifeEntity = ALARM_LIFE_DAO.selectAlarmLifeById(dto.getLifeId());
		if (null != alarmLifeEntity) {
			// 被转让人
			String transferee = toUser.getRealName() == null ? toUser.getUserName() : toUser.getRealName();
			boolean flag = false;
			// 调用微信服务判断用户是否关注告警公众号
			if (wechatServiceClient.ifFocusWechat(toUser.getId())) {
				// 已绑定微信，模板消息推送
				// 获取告警源信息
				Object map0 = redisTemplate.opsForValue().get(alarmLifeEntity.getAlarm().getTenantId());
				if (null != map0) {
					//Map<String, AlarmSourceEntity> map = (Map<String, AlarmSourceEntity>) map0;
					//AlarmSourceEntity alarmSourceEntity = map.get(alarmLifeEntity.getAlarm().getAlarmId());
					// 微信通知被转让人
					try {
						wechatProxyClient.alarmNotice(alarmLifeEntity, null, null, toUser.getId());
						flag = true;
					} catch (Exception e) {
						LOGGER.error("告警转让：微信通知失败");
					}
					
				}
			} 
			ArrayList<String> toEmails = new ArrayList<String>(1);
			toEmails.add(toUser.getUserName());
			// 调用邮件服务
			try {
				emailServiceClient.alarmToMail(toEmails.toArray(new String[toEmails.size()]), SendType.ALARMTRIGGER, alarmLifeEntity);
				flag = true;
			} catch (Exception e) {
				LOGGER.error("告警转让：邮件通知失败");
			}
			if(!flag) {
				// 所有通知失败，转让未成功
				return ResponseDto.builder(ErrorMsg.UNKNOWN);
			}
			
			// 加入处理人信息
			alarmLifeEntity.getToHandlers().add(dto.getToUser());
			Date date = new Date();
			if(alarmLifeEntity.getLogs().size() == 1) {
				alarmLifeEntity.getAlarmEvent().setResDate(date.getTime());
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
			AlarmMsgEntity alarmMsgEntityselected = alarmMsgDao.selectUnconfirmedMsgById(alarmLifeEntity.getId(),dto.getToUser(),alarmLifeEntity.getAlarm().getTenantId());
			if(null == alarmMsgEntityselected) {
				AlarmMsgEntity AlarmMsgEntity = new AlarmMsgEntity();
				AlarmMsgEntity.setUserId(dto.getToUser());
				AlarmMsgEntity.setConfirmed(false);
				AlarmMsgEntity.setMsgDate(System.currentTimeMillis());
				AlarmMsgEntity.setAlarmId(alarmLifeEntity.getId());
				AlarmMsgEntity.setType(NoticeType.ALARM);
				AlarmMsgEntity.setTitle(alarmLifeEntity.getAlarm().getTitle());
				AlarmMsgEntity.setHostName(alarmLifeEntity.getAlarm().getHostName());
				AlarmMsgEntity.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
				AlarmMsgEntity.setTriDate(alarmLifeEntity.getTriDate());
				AlarmMsgEntity.setAlarmStatus(alarmLifeEntity.getAlarmStatus());
				alarmMsgDao.save(AlarmMsgEntity, alarmLifeEntity.getAlarm().getTenantId());
			}
			return ResponseDto.builder(ErrorMsg.OK);
		}
		return ResponseDto.builder(ErrorMsg.UNKNOWN);
		
	}

	@Override
	public ResponseDto getMyAlarmPaging(AlarmQueryDto query) {
		// 查询我的告警
		AlarmPagingDto alarmPagingDto = getAlarmLifeDto(
				ALARM_LIFE_DAO.selectPaging(query.getCurPage(), query.getItems(), query, query.getHandlerCurrent()));
		ResponseDto response = ResponseDto.builder(ErrorMsg.OK);
		response.setData(alarmPagingDto);
		return response;
	}

	@Override
	public ResponseDto getAlarmPaging(AlarmQueryDto query) {

		AlarmPagingDto alarmPagingDto = getAlarmLifeDto(
				ALARM_LIFE_DAO.selectPaging(query.getCurPage(), query.getItems(), query, null));
		ResponseDto response = ResponseDto.builder(ErrorMsg.OK);
		response.setData(alarmPagingDto);
		return response;

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
				// copy entity
				AlarmSourceDto alarmSourceDto = new AlarmSourceDto();
				BeanUtils.copyProperties(alarmLifeEntity.getAlarm(), alarmSourceDto);
				AlarmRecoveryDto alarmRecoveryDto = new AlarmRecoveryDto();
				BeanUtils.copyProperties(alarmLifeEntity.getRecovery(), alarmRecoveryDto);
				Set<AlarmLogDto> alarmLogDtos = new HashSet<AlarmLogDto>();
				for (AlarmLogEntity entity : alarmLifeEntity.getLogs()) {
					AlarmLogDto dto = new AlarmLogDto();
					BeanUtils.copyProperties(entity, dto);
					alarmLogDtos.add(dto);
				}

				AlarmLifeDto alarmLifeDto = new AlarmLifeDto();
				alarmLifeDto.setAlarm(alarmSourceDto);
				alarmLifeDto.setRecovery(alarmRecoveryDto);
				alarmLifeDto.setLogs(alarmLogDtos);
				alarmLifeDto.setAlarmStatus(
						com.iv.facade.constant.AlarmStatus.valueOf(alarmLifeEntity.getAlarmStatus().name()));
				alarmLifeDto.setHandlerCurrent(alarmLifeEntity.getHandlerCurrent());
				alarmLifeDto.setHandlerLast(alarmLifeEntity.getHandlerLast());
				alarmLifeDto.setHostAlarmNum(alarmLifeEntity.getHostAlarmNum());
				alarmLifeDto.setId(alarmLifeEntity.getId());
				alarmLifeDto.setItemType(alarmLifeEntity.getItemType());

				alarmLifeDto.setRecDate(alarmLifeEntity.getRecDate());
				alarmLifeDto.setTriDate(alarmLifeEntity.getTriDate());
				alarmLifeDto.setUpgrade(alarmLifeEntity.getUpgrade());
				AlarmLifeDtos.add(alarmLifeDto);
			}
			alarmPagingDto.setEntries(AlarmLifeDtos);
		}
		return alarmPagingDto;
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
