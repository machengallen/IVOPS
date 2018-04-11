package com.iv.dto;

import java.sql.Date;
import java.sql.Time;

import com.iv.common.enumeration.Severity;

public class AlarmSourceEntityDto {
	// 数据库告警标识
		private String alarmId;
		// 第三方监控系统ip
		private String monitorIp;
		// 三方监控告警标识
		private Long eventId;
		// 告警标题
		private String title;
		// 告警内容
		private String content;
		// 故障服务名
		private String hostName;
		// 故障设备ip
		private String hostIp;
		// 告警级别
		private Severity severity;
		// 监控项key
		private String itemKey;
		// 触发日期
		private Date eventData;
		// 触发时间
		private Time eventTime;
		// 告警事件详情
		private String details;
		// 租户id
		private String tenantId;
		public String getAlarmId() {
			return alarmId;
		}
		public void setAlarmId(String alarmId) {
			this.alarmId = alarmId;
		}
		public String getMonitorIp() {
			return monitorIp;
		}
		public void setMonitorIp(String monitorIp) {
			this.monitorIp = monitorIp;
		}
		public Long getEventId() {
			return eventId;
		}
		public void setEventId(Long eventId) {
			this.eventId = eventId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
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
		public Severity getSeverity() {
			return severity;
		}
		public void setSeverity(Severity severity) {
			this.severity = severity;
		}
		public String getItemKey() {
			return itemKey;
		}
		public void setItemKey(String itemKey) {
			this.itemKey = itemKey;
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
		public String getDetails() {
			return details;
		}
		public void setDetails(String details) {
			this.details = details;
		}
		public String getTenantId() {
			return tenantId;
		}
		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}
		
		

}
