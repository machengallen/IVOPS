package com.iv.report.dao;

import java.util.List;

import com.iv.common.enumeration.CycleType;

/**
 * 告警报表数据
 * @author zhangying
 * 2018年4月23日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface AlarmReportDao {
	
	List<Object[]> selectAlarmByItemType(Long startTime,Long endTime) throws RuntimeException;
	
	List<Object[]> countUpAlarmByItemType(Long startTime,Long endTime) throws RuntimeException;
	
	List<Object[]> countpressAlarmByItemType(Long startTime,Long endTime) throws RuntimeException;
	
	List<Object[]> alarmsSolvedAuto(Long startTime,Long endTime) throws RuntimeException;
	
	List<Object[]> alarmsSolvedByPerson(Long startTime,Long endTime) throws RuntimeException;	
	
	Long countHostIps(Long date) throws RuntimeException;
	
	List<Object[]> selectAlarmInfo(Long date) throws RuntimeException;
	
	List<Object[]> groupByHostIp(Long date) throws RuntimeException;
	
	List<Object[]> groupByHostIpItemType(Long date) throws RuntimeException;
	
	List<Object[]> groupBySeverity(Long date) throws RuntimeException;
	
	List<Object[]> groupByItemType(Long date) throws RuntimeException;
	
	List<Object> groupByHostIpOpen(Long date) throws RuntimeException;
	
	Long getAlarmNumOpen(Long date) throws RuntimeException;
	
	List<Object[]> groupByCycle(CycleType cycle, Long date) throws RuntimeException;
	
	List<Object[]> getAlarmByCycle(CycleType cycle, Long date) throws RuntimeException;
	
	List<Object[]> selectBatchByIds(String[] ids) throws RuntimeException;
}
