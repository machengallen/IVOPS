package com.iv.aggregation.api.dto;

/**
 * 告警通知消息业务数据
 * @author macheng
 * 2018年7月17日
 * alarm-aggregation-service-api
 * 
 */
public class AlarmMsgDto {

	private String tenantId;// 项目组id
	private String tenantName;// 项目组名字标签
	private String alarmId;// 告警单号
	private String type;// 告警类型（触发/恢复）
	private String title;// 告警标题
	private String hostName;// 故障对象
	private String hostIp;// 故障ip
	private long triDate;// 告警发生时间
	private String alarmStatus;// 告警当前状态
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	
}
