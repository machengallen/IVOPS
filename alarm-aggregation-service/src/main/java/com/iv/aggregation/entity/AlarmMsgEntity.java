package com.iv.aggregation.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iv.aggregation.api.constant.AlarmStatus;
import com.iv.aggregation.api.constant.NoticeType;

/**
 * 消息中心-告警通知消息体
 * @author macheng
 * 2018年4月3日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Entity
@Table(indexes = {@Index(columnList = "user_id")})
public class AlarmMsgEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9148726291338784401L;
	/*
	 * 消息类属性
	 */
	private String id;
	@JsonIgnore
	private int userId;
	private boolean confirmed;// 消息是否被确认
	@JsonIgnore
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
	
	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Enumerated(EnumType.STRING)
	public NoticeType getType() {
		return type;
	}
	public void setType(NoticeType type) {
		this.type = type;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
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
	@Enumerated(EnumType.STRING)
	public AlarmStatus getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(AlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	
}
