package com.iv.aggregation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iv.aggregation.api.dto.AlarmMessageInput;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.util.DataConvert;

/**
 * 南向zabbix告警处理
 * @author macheng
 * 2018年4月3日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Service
public class ZabbixAlarmHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(ZabbixAlarmHandler.class);
	@Autowired
	private CoreService coreService;
	/**
	 * 处理三方监控服务器发来的告警数据
	 * 
	 * @param AlarmTempMessage
	 */
	public AlarmLifeEntity alarmProcessing(AlarmMessageInput ami) {

		try {

			if (StringUtils.isEmpty(ami.getEventRecoveryId())) {
				// 告警触发
				return coreService.alarmTrigger(DataConvert.zabbixAlarmConvert(ami));
			} else {
				// 告警恢复
				coreService.alarmRecovery(DataConvert.zabbixRecoveryConvert(ami));
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("告警消息处理失败", e);
			return null;
		}

	}
	
}
