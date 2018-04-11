package com.iv.dto;

import com.iv.common.enumeration.AlarmStatus;

public class AlarmLifeEntityDto {
	private String id;	
	//当前处理人
	private int currentHandlerId;
	//平台收到告警触发时间
	private long triDate;
	//平台收到告警恢复时间
	private long recDate;
	//告警信息
	private AlarmSourceEntityDto alarm;		
	//告警状态
	private AlarmStatus alarmStatus;
	//告警升级记录
	private byte upgrade;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public AlarmSourceEntityDto getAlarm() {
		return alarm;
	}
	public void setAlarm(AlarmSourceEntityDto alarm) {
		this.alarm = alarm;
	}
	public byte getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(byte upgrade) {
		this.upgrade = upgrade;
	}
	public AlarmStatus getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(AlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}	
			
}
