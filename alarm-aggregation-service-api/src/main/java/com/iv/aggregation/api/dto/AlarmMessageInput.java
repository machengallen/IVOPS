package com.iv.aggregation.api.dto;

import java.io.Serializable;

/**
 * 三方监控入口消息体
 * @author macheng
 *
 */
public class AlarmMessageInput implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -417962484129245112L;
	//openID
	private String touser;
	//微信模板id
	private String template_id;
	//微信下载接口
	private String url;
	//告警发生事件id
	private String eventId;
	//告警恢复事件id
	private String eventRecoveryId;
	//监控项key
	private String itemKey;
	//告警标题
	private String title;
	//告警内容
	private String content;
	//第三方监控系统ip
	private String monitorIp;
	//故障业务名
	private String hostName;
	//故障设备ip
	private String hostIp;
	//告警级别
	private String severity;
	//触发日期
	private String eventData;
	//触发时间
	private String eventTime;
	//告警持续时间
	private String eventAge;
	//恢复日期
	private String recoveryData;
	//恢复时间
	private String recoveryTime;
	//告警事件详情
	private String details;
	//租户id
	private String token;
	
	public AlarmMessageInput() {
		
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEventId() {
		return eventId;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventRecoveryId() {
		return eventRecoveryId;
	}

	public void setEventRecoveryId(String eventRecoveryId) {
		this.eventRecoveryId = eventRecoveryId;
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

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getEventData() {
		return eventData;
	}

	public void setEventData(String eventData) {
		this.eventData = eventData;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventAge() {
		return eventAge;
	}

	public void setEventAge(String eventAge) {
		this.eventAge = eventAge;
	}

	public String getRecoveryData() {
		return recoveryData;
	}

	public void setRecoveryData(String recoveryData) {
		this.recoveryData = recoveryData;
	}

	public String getRecoveryTime() {
		return recoveryTime;
	}

	public void setRecoveryTime(String recoveryTime) {
		this.recoveryTime = recoveryTime;
	}

	public String getMonitorIp() {
		return monitorIp;
	}

	public void setMonitorIp(String monitorIp) {
		this.monitorIp = monitorIp;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void upgrade(){
		Byte before = Byte.parseByte(this.severity);
		if(before < 5){
			before++;
			this.severity = before.toString();
		}
	}
	public String toAlarmString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[eventId=");
		builder.append(eventId);
		builder.append(", itemKey=");
		builder.append(itemKey);
		builder.append(", title=");
		builder.append(title);
		builder.append(", content=");
		builder.append(content);
		builder.append(", monitorIp=");
		builder.append(monitorIp);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append(", hostIp=");
		builder.append(hostIp);
		builder.append(", severity=");
		builder.append(severity);
		builder.append(", eventData=");
		builder.append(eventData);
		builder.append(", eventTime=");
		builder.append(eventTime);
		builder.append("]");
		return builder.toString();
	}

	public String toRecoveryString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[eventId=");
		builder.append(eventId);
		builder.append(", eventRecoveryId=");
		builder.append(eventRecoveryId);
		builder.append(", monitorIp=");
		builder.append(monitorIp);
		builder.append(", eventAge=");
		builder.append(eventAge);
		builder.append(", recoveryData=");
		builder.append(recoveryData);
		builder.append(", recoveryTime=");
		builder.append(recoveryTime);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AlarmMessageInput [touser=");
		builder.append(touser);
		builder.append(", template_id=");
		builder.append(template_id);
		builder.append(", url=");
		builder.append(url);
		builder.append(", eventId=");
		builder.append(eventId);
		builder.append(", eventRecoveryId=");
		builder.append(eventRecoveryId);
		builder.append(", itemKey=");
		builder.append(itemKey);
		builder.append(", title=");
		builder.append(title);
		builder.append(", content=");
		builder.append(content);
		builder.append(", monitorIp=");
		builder.append(monitorIp);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append(", hostIp=");
		builder.append(hostIp);
		builder.append(", severity=");
		builder.append(severity);
		builder.append(", eventData=");
		builder.append(eventData);
		builder.append(", eventTime=");
		builder.append(eventTime);
		builder.append(", eventAge=");
		builder.append(eventAge);
		builder.append(", recoveryData=");
		builder.append(recoveryData);
		builder.append(", recoveryTime=");
		builder.append(recoveryTime);
		builder.append(", details=");
		builder.append(details);
		builder.append(", token=");
		builder.append(token);
		builder.append("]");
		return builder.toString();
	}
	
}
