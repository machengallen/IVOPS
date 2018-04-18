package com.iv.facade.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class AlarmRecoveryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1726458361559875312L;
	//数据库标识
	private String recoveryId;
	//第三方监控系统ip
	private String monitorIp;
	//告警恢复事件id
	private String eventRecoveryId;
	//原告警
	private AlarmSourceDto alarmSourceEntity;
	//告警持续时间
	private String eventAge;
	//恢复日期
	private Date recoveryData;
	//恢复时间
	private Time recoveryTime;
	
	public String getRecoveryId() {
		return recoveryId;
	}
	public void setRecoveryId(String recoveryId) {
		this.recoveryId = recoveryId;
	}
	public String getEventRecoveryId() {
		return eventRecoveryId;
	}
	public void setEventRecoveryId(String eventRecoveryId) {
		this.eventRecoveryId = eventRecoveryId;
	}
	public AlarmSourceDto getAlarmSourceEntity() {
		return alarmSourceEntity;
	}
	public void setAlarmSourceEntity(AlarmSourceDto alarmSourceEntity) {
		this.alarmSourceEntity = alarmSourceEntity;
	}
	public String getEventAge() {
		return eventAge;
	}
	public void setEventAge(String eventAge) {
		this.eventAge = eventAge;
	}
	public Date getRecoveryData() {
		return recoveryData;
	}
	public void setRecoveryData(Date recoveryData) {
		this.recoveryData = recoveryData;
	}
	public Time getRecoveryTime() {
		return recoveryTime;
	}
	public void setRecoveryTime(Time recoveryTime) {
		this.recoveryTime = recoveryTime;
	}
	public String getMonitorIp() {
		return monitorIp;
	}
	public void setMonitorIp(String monitorIp) {
		this.monitorIp = monitorIp;
	}
	
}
