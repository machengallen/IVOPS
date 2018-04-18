package com.iv.aggregation.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

/**
 * 告警恢复信息实体类
 * @author macheng
 * 2018年4月2日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Entity
@Table(name = "Alarm_Recovery")
public class AlarmRecoveryEntity implements Serializable{

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
	private AlarmSourceEntity alarmSourceEntity;
	//告警持续时间
	private String eventAge;
	//恢复日期
	private Date recoveryData;
	//恢复时间
	private Time recoveryTime;
	
	@Id  
    @GenericGenerator(name="idGenerator", strategy="uuid")  
    @GeneratedValue(generator="idGenerator")
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
	@OneToOne(orphanRemoval = true)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name = "alarmId")
	public AlarmSourceEntity getAlarmSourceEntity() {
		return alarmSourceEntity;
	}
	public void setAlarmSourceEntity(AlarmSourceEntity alarmSourceEntity) {
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
