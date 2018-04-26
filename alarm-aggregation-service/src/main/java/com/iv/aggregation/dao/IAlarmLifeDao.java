package com.iv.aggregation.dao;

import java.util.List;
import java.util.Set;

import com.iv.aggregation.api.constant.AgentType;
import com.iv.aggregation.api.dto.AlarmQueryDto;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmRecoveryEntity;
import com.iv.aggregation.entity.AlarmSourceEntity;
import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.CycleType;

/**
 * 统一告警数据dao
 * @author macheng
 * 2018年4月2日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public interface IAlarmLifeDao {

	AlarmLifeEntity saveAlarmLife(AlarmLifeEntity alarmLifeEntity) throws RuntimeException;
	
	void delById(String id) throws RuntimeException;

	AlarmLifeEntity selectAlarmLifeByAlarmSrc(AlarmSourceEntity sourceEntity) throws RuntimeException;

	AlarmLifeEntity selectAlarmLifeById(String id) throws RuntimeException;
	
	AlarmLifeEntity selectAlarmLifeById(String id, String tenantId) throws RuntimeException;

	void updateAlarmStatus(String alarmId, Integer hireHandlerCurrent, Integer hireHandlerLast,
			AlarmRecoveryEntity recovery, AlarmStatus alarmStatus, Byte upgrade) throws RuntimeException;

	void updateAlarmLife(AlarmLifeEntity alarmLifeEntity)throws RuntimeException;
	
	AlarmPaging selectPaging(int index, int nums, AlarmQueryDto query, Integer localAuthId) throws RuntimeException;
	
	AlarmPaging selectByCurTeamPaging(int index, int nums, AlarmQueryDto query, Set<String> alarmLifeIds) throws RuntimeException;
	
	Long countHostIps(Long date) throws RuntimeException;
	
	List<Object[]> selectAlarmInfo(Long date) throws RuntimeException;
	
	List<Object[]> groupByHostIp(Long date) throws RuntimeException;
	
	List<Object[]> groupBySeverity(Long date) throws RuntimeException;
	
	List<Object[]> groupByItemType(Long date) throws RuntimeException;
	
	List<Object> groupByHostIpOpen(Long date) throws RuntimeException;
	
	Long getAlarmNumOpen(Long date) throws RuntimeException;
	
	List<Object[]> groupByCycle(CycleType cycle, Long date) throws RuntimeException;
	
	List<Object[]> getAlarmByCycle(CycleType cycle, Long date) throws RuntimeException;
	
	List<Object[]> selectBatchByIds(String[] ids) throws RuntimeException;
	
	void delBeforeTimestamp(long timestamp, String tenantId) throws RuntimeException;
	
	AlarmLifeEntity saveOrUpdateAlarmLife(AlarmLifeEntity alarmLifeEntity) throws RuntimeException;
	
	List<Object[]> groupByHostIpItemType(Long date) throws RuntimeException;
	
	AlarmSourceEntity saveAlarmSource(AlarmSourceEntity alarmSourceEntity) throws RuntimeException;

	AlarmRecoveryEntity saveAlarmRecovery(AlarmRecoveryEntity alarmRecoveryEntity) throws RuntimeException;

	AlarmSourceEntity selectAlarmSourceByEventId(String eventId, String monitorIp, String tenantId,
			AgentType agentType) throws RuntimeException;

	
}
