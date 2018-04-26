package com.iv.dto;

import java.sql.Date;
import java.sql.Time;

import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.Severity;

public class AlarmLifeEntityDto {
	
	//当前处理人
	private int currentHandlerId;
	//平台收到告警触发时间
	private long triDate;
	//平台收到告警恢复时间
	private long recDate;		
	// 告警标题
	private String title;
	//告警状态
	private AlarmStatus alarmStatus;
	// 故障设备ip
	private String hostIp;
	private String id;
	// 告警内容
	private String content;
	// 告警级别
	private Severity severity;
	//告警升级记录
	private byte upgrade;
	// 触发日期
	private Date eventData;
	// 触发时间
	private Time eventTime;
	// 故障服务名
	private String hostName;
	public int getCurrentHandlerId() {
		return currentHandlerId;
	}
	public void setCurrentHandlerId(int currentHandlerId) {
		this.currentHandlerId = currentHandlerId;
	}
	public long getTriDate() {
		return triDate;
	}
	public void setTriDate(long triDate) {
		this.triDate = triDate;
	}
	public long getRecDate() {
		return recDate;
	}
	public void setRecDate(long recDate) {
		this.recDate = recDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public AlarmStatus getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(AlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public byte getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(byte upgrade) {
		this.upgrade = upgrade;
	}
	public Date getEventData() {
		return eventData;
	}
	public void setEventData(Date eventData) {
		this.eventData = eventData;
	}
	public Time getEventTime() {
		return eventTime;
	}
	public void setEventTime(Time eventTime) {
		this.eventTime = eventTime;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}	
	
}
