package com.iv.aggregation.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iv.aggregation.api.constant.OpsType;
import com.iv.aggregation.api.dto.AlarmMessageInput;
import com.iv.aggregation.dao.IAlarmLifeDao;
import com.iv.aggregation.dao.impl.AlarmLifeDaoImpl;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmLogEntity;
import com.iv.aggregation.entity.AlarmRecoveryEntity;
import com.iv.aggregation.entity.AlarmSourceEntity;
import com.iv.aggregation.feign.clients.IAlarmStrategyClient;
import com.iv.aggregation.util.DataConvert;
import com.iv.aggregation.util.DispatchUtil;
import com.iv.aggregation.util.TimerPending;
import com.iv.aggregation.util.WechatProxyClient;
import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.response.ResponseDto;
import com.iv.strategy.api.dto.AlarmStrategyDto;
import com.iv.strategy.api.dto.StrategyQueryDto;

/**
 * 核心告警处理服务
 * @author macheng
 * 2018年4月3日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Service
public class CoreService {

	@Autowired
	private WechatProxyClient wechatProxyClient;
	@Autowired
	private IAlarmStrategyClient alarmStrategyClient;
	@Autowired
	private RedisTemplate redisTemplate;
	private static Logger logger = LoggerFactory.getLogger(CoreService.class);
	public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);
	// 数据库工具实例化
	private static final IAlarmLifeDao ALARM_LIFE_DAO = AlarmLifeDaoImpl.getInstance();

	/**
	 * 处理三方监控服务器发来的告警数据
	 * 
	 * @param AlarmTempMessage
	 */
	public AlarmLifeEntity alarmProcessing(AlarmMessageInput ami, String tenantId) {

		try {

			if (StringUtils.isEmpty(ami.getEventRecoveryId())) {
				// 告警触发
				return alarmTrigger(DataConvert.zabbixAlarmConvert(ami, tenantId));
			} else {
				// 告警恢复
				alarmRecovery(DataConvert.zabbixRecoveryConvert(ami, tenantId));
				return null;
			}
		} catch (Exception e) {
			logger.error("告警消息处理失败", e);
			return null;
		}

	}

	public AlarmLifeEntity alarmTrigger(AlarmSourceEntity alarmSourceEntity) throws Exception {

		// 存储告警触发数据
		alarmSourceEntity = ALARM_LIFE_DAO.saveAlarmSource(alarmSourceEntity);
		// 创建告警生命周期对象
		AlarmLifeEntity alarmLifeEntity = new AlarmLifeEntity();
		Long tridate = System.currentTimeMillis();
		alarmLifeEntity.setAlarmStatus(AlarmStatus.PENDING);
		alarmLifeEntity.setUpgrade((byte) 0);
		alarmLifeEntity.setAlarm(alarmSourceEntity);
		alarmLifeEntity.setItemType(DispatchUtil.getItemType(alarmSourceEntity.getItemKey()));
		alarmLifeEntity.getLogs()
				.add(AlarmLogEntity.builder(new Date(tridate), OpsType.TRIGGER, null, null, 0));
		alarmLifeEntity.setTriDate(tridate);
		// 告警存储
		ALARM_LIFE_DAO.saveOrUpdateAlarmLife(alarmLifeEntity);
		//Timer timer = new Timer();
		
		// 调用告警策略服务
		StrategyQueryDto strategyQuery = new StrategyQueryDto();
		strategyQuery.setSeverity(alarmSourceEntity.getSeverity());
		strategyQuery.setItemType(alarmLifeEntity.getItemType());
		strategyQuery.setTenantId(alarmSourceEntity.getTenantId());
		AlarmStrategyDto dispatchStrategy = alarmStrategyClient.getStrategy(strategyQuery);
		
		int delayTime;// 单位秒
		if (null != dispatchStrategy) {
			delayTime = dispatchStrategy.getDelayTime();
		} else {
			logger.warn("alarm got, but no dispatch");
			return alarmLifeEntity;
		}
		Short pushTeam = 1;
		TimerPending pending = new TimerPending(pushTeam, alarmSourceEntity);

		// 告警存入redis
		Object map0 = redisTemplate.opsForValue().get(alarmSourceEntity.getTenantId());
		Map<String, AlarmSourceEntity> map = null;
		if (null == map0) {
			map = new HashMap<String, AlarmSourceEntity>();
		} else {
			map = (Map<String, AlarmSourceEntity>) map0;
		}
		map.put(alarmSourceEntity.getAlarmId(), alarmSourceEntity);
		redisTemplate.opsForValue().set(alarmSourceEntity.getTenantId(), map);
		SCHEDULED_EXECUTOR_SERVICE.schedule(pending, delayTime, TimeUnit.SECONDS);
		
		return 	alarmLifeEntity;
	}


	public void alarmRecovery(AlarmRecoveryEntity alarmRecoveryEntity) throws Exception {

		// 存储告警恢复数据
		alarmRecoveryEntity = ALARM_LIFE_DAO.saveAlarmRecovery(alarmRecoveryEntity);
		Object map0 = redisTemplate.opsForValue().get(alarmRecoveryEntity.getAlarmSourceEntity().getTenantId());
		if (null != map0) {
			Map<String, AlarmSourceEntity> map = (Map<String, AlarmSourceEntity>) map0;
			map.remove(alarmRecoveryEntity.getAlarmSourceEntity().getAlarmId());
			redisTemplate.opsForValue().set(alarmRecoveryEntity.getAlarmSourceEntity().getTenantId(), map);
		}
		AlarmLifeEntity lifeEntity = ALARM_LIFE_DAO.selectAlarmLifeByAlarmSrc(alarmRecoveryEntity.getAlarmSourceEntity());
		Long recdate = System.currentTimeMillis();
		lifeEntity.setRecDate(recdate);
		lifeEntity.setRecovery(alarmRecoveryEntity);
		// 记录操作日志
		lifeEntity.getLogs()
				.add(AlarmLogEntity.builder(new Date(recdate), OpsType.RECOVER, null, null, 0));
		// 发送模板消息
		wechatProxyClient.alarmRecovery(lifeEntity);
		lifeEntity.setAlarmStatus(AlarmStatus.CLOSED);
		lifeEntity.setHandlerLast(lifeEntity.getHandlerCurrent());
		// 同步数据库
		ALARM_LIFE_DAO.saveAlarmLife(lifeEntity);
	}

	/**
	 * 获取告警数据详情
	 * 
	 * @param alarmId
	 */
	public AlarmLifeEntity getAlarmDetails(String lifeId) {

		String[] strs = lifeId.split("\\$");
		lifeId = strs[0];
		String tenantId = strs[1];
		AlarmLifeEntity alarmLifeEntity = ALARM_LIFE_DAO.selectAlarmLifeById(lifeId, tenantId);
		return alarmLifeEntity;
	}

	/**
	 * 获取告警恢复详情
	 * 
	 * @param alarmId
	 */
	public AlarmLifeEntity getRecoveryDetails(String lifeId) {

		String[] strs = lifeId.split("#");
		lifeId = strs[0];
		String tenantId = strs[1];
		AlarmLifeEntity alarmLifeEntity = ALARM_LIFE_DAO.selectAlarmLifeById(lifeId, tenantId);
		return alarmLifeEntity;
	}

	private Date getFormatDate(String before) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date after = dateFormat.parse(before);
		return after;
	}

}
