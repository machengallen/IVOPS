package com.iv.message.api.dto;

import java.util.List;

import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.NoticeType;

/**
 * 告警通知消息体
 * @author macheng
 * 2018年4月23日
 * 
 */
public class AlarmMsgDto {

	/*
	 * 消息类属性
	 */
	private List<Integer> userIds;// 通知用户id
	private String tenantId;
	//private boolean confirmed;// 消息是否被确认
	private long msgDate;// 消息创建时间
	/*
	 * 业务类属性
	 */
	private String alarmId;// 告警单号
	private NoticeType type;
	private String title;// 告警标题
	private String hostName;// 故障对象
	private String hostIp;// 故障ip
	private long triDate;// 告警发生时间
	private AlarmStatus alarmStatus;// 告警当前状态
	
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public NoticeType getType() {
		return type;
	}
	public void setType(NoticeType type) {
		this.type = type;
	}
	public long getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(long msgDate) {
		this.msgDate = msgDate;
	}
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public long getTriDate() {
		return triDate;
	}
	public void setTriDate(long triDate) {
		this.triDate = triDate;
	}
	public AlarmStatus getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(AlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	
}
