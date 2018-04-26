package com.iv.dto;

import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.Severity;

public class AlarmLifeEntityDto {
	
	//当前处理人
	private int currentHandlerId;	
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
	// 触发/恢复时间
	private String time;
	// 故障服务名
	private String hostName;
	public int getCurrentHandlerId() {
		return currentHandlerId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setCurrentHandlerId(int currentHandlerId) {
		this.currentHandlerId = currentHandlerId;
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
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}	
	
}
