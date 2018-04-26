package com.iv.dto;

import com.iv.common.enumeration.SendType;

public class AlarmInfoTemplate {
	private String[] toEmails;
	private SendType emailType;
	private AlarmLifeEntityDto alarmLifeEntity;
	public String[] getToEmails() {
		return toEmails;
	}
	public void setToEmails(String[] toEmails) {
		this.toEmails = toEmails;
	}
	public SendType getEmailType() {
		return emailType;
	}
	public void setEmailType(SendType emailType) {
		this.emailType = emailType;
	}
	public AlarmLifeEntityDto getAlarmLifeEntity() {
		return alarmLifeEntity;
	}
	public void setAlarmLifeEntity(AlarmLifeEntityDto alarmLifeEntity) {
		this.alarmLifeEntity = alarmLifeEntity;
	}
	
}
