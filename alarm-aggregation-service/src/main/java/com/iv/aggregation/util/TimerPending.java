package com.iv.aggregation.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import com.iv.aggregation.api.constant.NoticeModel;
import com.iv.aggregation.api.constant.OpsType;
import com.iv.aggregation.dao.IAlarmLifeDao;
import com.iv.aggregation.dao.impl.AlarmLifeDaoImpl;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmLogEntity;
import com.iv.aggregation.entity.AlarmSourceEntity;
import com.iv.aggregation.feign.clients.IAlarmStrategyClient;
import com.iv.aggregation.feign.clients.IMessageServiceClient;
import com.iv.aggregation.service.CoreService;
import com.iv.common.enumeration.NoticeType;
import com.iv.common.util.spring.SpringContextUtil;
import com.iv.message.api.dto.AlarmMsgDto;
import com.iv.strategy.api.dto.AlarmStrategyDto;
import com.iv.strategy.api.dto.StrategyQueryDto;

/**
 * 告警定时通知器
 * 
 * @author macheng 2018年4月3日 alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public class TimerPending implements Runnable {
	// 日志记录工具
	private final static Logger LOGGER = LoggerFactory.getLogger(TimerPending.class);
	// 数据库工具类
	private final static IAlarmLifeDao ALARM_LIFE_DAO = AlarmLifeDaoImpl.getInstance();
	// 团队数量
	private int groupNum;
	// 推送团队位置（pushTeam==1，推送给第一团队）
	private Short pushTeam;
	// 告警源
	private AlarmSourceEntity alarmSourceEntity;
	
	public TimerPending() {
		super();
	}

	public TimerPending(Short pushTeam, AlarmSourceEntity alarmSourceEntity) {
		super();
		this.pushTeam = pushTeam;
		this.alarmSourceEntity = alarmSourceEntity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		IAlarmStrategyClient alarmStrategyClient = SpringContextUtil.getBean(IAlarmStrategyClient.class);
		WechatProxyClient wechatProxyClient = SpringContextUtil.getBean(WechatProxyClient.class);
		IMessageServiceClient messageServiceClient = SpringContextUtil.getBean(IMessageServiceClient.class);
		RedisTemplate redisTemplate = SpringContextUtil.getBean("redisTemplate", RedisTemplate.class);
		
		AlarmLifeEntity alarmLifeEntity = ALARM_LIFE_DAO.selectAlarmLifeByAlarmSrc(this.alarmSourceEntity);
		// 调用策略服务获取分派策略
		StrategyQueryDto strategyQuery = new StrategyQueryDto();
		strategyQuery.setSeverity(alarmSourceEntity.getSeverity());
		strategyQuery.setItemType(alarmLifeEntity.getItemType());
		strategyQuery.setTenantId(alarmSourceEntity.getTenantId());
		AlarmStrategyDto dispatchStrategy = (AlarmStrategyDto)alarmStrategyClient.getStrategy(strategyQuery).getData();
		
		Map<String, AlarmSourceEntity> map = (Map<String, AlarmSourceEntity>) redisTemplate.opsForValue()
				.get(this.alarmSourceEntity.getTenantId());
		AlarmSourceEntity sourceEntity = map.get(alarmLifeEntity.getAlarm().getAlarmId());

		try {
			if (null != dispatchStrategy && null != sourceEntity) {
				// 解析通知方式
				List<NoticeModel> noticeModels = parse2NoticeModel(dispatchStrategy.getNoticeModel());
				this.groupNum = dispatchStrategy.getGroupIds().size();
				ArrayList<Integer> toUserIds = new ArrayList<Integer>(1);
				if (pushTeam == 1) {
					Map<String, Object> infoMap = wechatProxyClient.alarmNotice(alarmLifeEntity, pushTeam - 1, noticeModels.get(pushTeam - 1), null);
					String dispatcher = (String) infoMap.get("dispatcher");
					toUserIds = (ArrayList<Integer>) infoMap.get("toUserIds");
					alarmLifeEntity.getLogs().add(AlarmLogEntity.builder(new Date(), OpsType.ASSIGN, dispatcher, null,
							dispatchStrategy.getDelayTime()));
				} else if (pushTeam <= groupNum) {
					// 未升级到最终团队，可继续升级
					// 未认领超过三次推送，告警升级
					// this.ami.upgrade();
					alarmLifeEntity.setUpgrade((byte) (alarmLifeEntity.getUpgrade() + 1));
					Map<String, Object> infoMap = wechatProxyClient.alarmNotice(alarmLifeEntity, pushTeam - 1, noticeModels.get(pushTeam - 1), null);
					String dispatcher = (String) infoMap.get("dispatcher");
					toUserIds = (ArrayList<Integer>) infoMap.get("toUserIds");
					alarmLifeEntity.upgrade(dispatcher, parse2Short(dispatchStrategy.getUpgradeTime()).get(pushTeam - 2));
				}
				pushTeam++;
				if (pushTeam <= groupNum && null != sourceEntity) {
					TimerPending pending = new TimerPending(pushTeam, this.alarmSourceEntity);
					CoreService.SCHEDULED_EXECUTOR_SERVICE.schedule(pending,
							parse2Short(dispatchStrategy.getUpgradeTime()).get(pushTeam - 2) * 60, TimeUnit.SECONDS);
				}
				// 先清空联合主键表，再更新
				Set<Integer> mapUsers = new HashSet<Integer>(alarmLifeEntity.getToHireUserIds());
				alarmLifeEntity.getToHireUserIds().clear();
				ALARM_LIFE_DAO.saveOrUpdateAlarmLife(alarmLifeEntity);
				alarmLifeEntity.setToHireUserIds(mapUsers);
				ALARM_LIFE_DAO.saveOrUpdateAlarmLife(alarmLifeEntity);
				// 将告警周期对象存入团队中
				/*
				 * GroupEntity groupEntity = strategyEntity.getGroupIds().get(pushTeam - 2);
				 * groupEntity.getAlarmLifeEntity().add(alarmLifeEntity);
				 * groupDao.saveGroupInTenant(groupEntity,
				 * alarmLifeEntity.getAlarm().getTenantId());
				 */
				// 保存告警消息体
				if (!CollectionUtils.isEmpty(toUserIds)) {
					AlarmMsgDto msgDto = new AlarmMsgDto();
					msgDto.setUserIds(toUserIds);
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
				}
			}

		} catch (Exception e) {
			LOGGER.error("定时任务执行失败：", e);
			LOGGER.error("告警单号：{}", alarmLifeEntity.getId());
		}
	}

	private List<NoticeModel> parse2NoticeModel(String model) {
		model = model.trim();
		List<NoticeModel> models = new ArrayList<>();
		for (String noticeModel : model.split("#")) {
			models.add(NoticeModel.values()[Byte.parseByte(noticeModel)]);
		}
		return models;
	}

	// 将升级时间字符串按“#”拆分
	private List<Short> parse2Short(String upgradeTime) {
		upgradeTime = upgradeTime.trim();
		List<Short> upgradeTimes = new ArrayList<>();
		for (String gradeTime : upgradeTime.split("#")) {
			upgradeTimes.add(Short.parseShort(gradeTime));
		}
		return upgradeTimes;
	}

	public Short getPushTeam() {
		return pushTeam;
	}

	public void setPushTeam(Short pushTeam) {
		this.pushTeam = pushTeam;
	}

	public AlarmSourceEntity getAlarmSourceEntity() {
		return alarmSourceEntity;
	}

	public void setAlarmSourceEntity(AlarmSourceEntity alarmSourceEntity) {
		this.alarmSourceEntity = alarmSourceEntity;
	}

}
