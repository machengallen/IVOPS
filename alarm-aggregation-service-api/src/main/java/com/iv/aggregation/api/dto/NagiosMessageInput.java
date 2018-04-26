package com.iv.aggregation.api.dto;

import java.io.Serializable;

/**
 * nagios告警通知pojo
 * 
 * @author macheng
 * 2017-12-25，圣诞节
 */
public class NagiosMessageInput implements Serializable{

	private static final long serialVersionUID = 3608848751021696657L;
	
	private String monitorIp;// 监控服务器ip
	private String notificationType;// 通知类型
	private String token;//租户id
	private String details;
	/**
	 * 主机相关
	 */
	private String hostName;// 主机名
	private String hostIp;// 主机地址
	private String hostState;// 主机状态  0=运行、1=宕机、2=不可达
	private String hostProblemId;// 主机故障id
	private String lastHostProblemId;// 恢复通知关联的故障id
	private String hostEventId;// 主机状态变化事件id，可用作主机故障恢复事件id
	private String hostDuration;// 主机在当前状态下的持续时间
	private String hostOutput;// 故障文本内容第一行
	private String longHostOutput;// 剩余故障文本内容
	private String hostCheckCommand;// 主机检查命令名称，可作故障类型区分
	/**
	 * 服务相关
	 */
	private String serviceState;// 服务状态 0 = OK，1 =警告，2 =临界，3 =未知
	private String serviceProblemId;// 服务故障id
	private String lastServiceProblemId;// 恢复通知关联故障的id
	private String serviceEventId;// 服务状态变化的事件id，可用作恢复事件id
	private String serviceDuration;// 服务在当前状态下的持续时间
	private String serviceOutput;// 故障文本内容第一行
	private String longServiceOutput;// 剩余故障文本内容
	private String serviceCheckCommand;// 服务检查命令名称，可作故障类型区分
	/**
	 * 时间相关
	 */
	private String date;
	private String time;
	public String getMonitorIp() {
		return monitorIp;
	}
	public void setMonitorIp(String monitorIp) {
		this.monitorIp = monitorIp;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
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
	public String getHostState() {
		return hostState;
	}
	public void setHostState(String hostState) {
		this.hostState = hostState;
	}
	public String getHostProblemId() {
		return hostProblemId;
	}
	public void setHostProblemId(String hostProblemId) {
		this.hostProblemId = hostProblemId;
	}
	public String getLastHostProblemId() {
		return lastHostProblemId;
	}
	public void setLastHostProblemId(String lastHostProblemId) {
		this.lastHostProblemId = lastHostProblemId;
	}
	public String getHostEventId() {
		return hostEventId;
	}
	public void setHostEventId(String hostEventId) {
		this.hostEventId = hostEventId;
	}
	public String getHostDuration() {
		return hostDuration;
	}
	public void setHostDuration(String hostDuration) {
		this.hostDuration = hostDuration;
	}
	public String getHostOutput() {
		return hostOutput;
	}
	public void setHostOutput(String hostOutput) {
		this.hostOutput = hostOutput;
	}
	public String getLongHostOutput() {
		return longHostOutput;
	}
	public void setLongHostOutput(String longHostOutput) {
		this.longHostOutput = longHostOutput;
	}
	public String getHostCheckCommand() {
		return hostCheckCommand;
	}
	public void setHostCheckCommand(String hostCheckCommand) {
		this.hostCheckCommand = hostCheckCommand;
	}
	public String getServiceState() {
		return serviceState;
	}
	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}
	public String getServiceProblemId() {
		return serviceProblemId;
	}
	public void setServiceProblemId(String serviceProblemId) {
		this.serviceProblemId = serviceProblemId;
	}
	public String getLastServiceProblemId() {
		return lastServiceProblemId;
	}
	public void setLastServiceProblemId(String lastServiceProblemId) {
		this.lastServiceProblemId = lastServiceProblemId;
	}
	public String getServiceEventId() {
		return serviceEventId;
	}
	public void setServiceEventId(String serviceEventId) {
		this.serviceEventId = serviceEventId;
	}
	public String getServiceDuration() {
		return serviceDuration;
	}
	public void setServiceDuration(String serviceDuration) {
		this.serviceDuration = serviceDuration;
	}
	public String getServiceOutput() {
		return serviceOutput;
	}
	public void setServiceOutput(String serviceOutput) {
		this.serviceOutput = serviceOutput;
	}
	public String getLongServiceOutput() {
		return longServiceOutput;
	}
	public void setLongServiceOutput(String longServiceOutput) {
		this.longServiceOutput = longServiceOutput;
	}
	public String getServiceCheckCommand() {
		return serviceCheckCommand;
	}
	public void setServiceCheckCommand(String serviceCheckCommand) {
		this.serviceCheckCommand = serviceCheckCommand;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NagiosMessageInput [monitorIp=");
		builder.append(monitorIp);
		builder.append(", notificationType=");
		builder.append(notificationType);
		builder.append(", token=");
		builder.append(token);
		builder.append(", details=");
		builder.append(details);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append(", hostIp=");
		builder.append(hostIp);
		builder.append(", hostState=");
		builder.append(hostState);
		builder.append(", hostProblemId=");
		builder.append(hostProblemId);
		builder.append(", lastHostProblemId=");
		builder.append(lastHostProblemId);
		builder.append(", hostEventId=");
		builder.append(hostEventId);
		builder.append(", hostDuration=");
		builder.append(hostDuration);
		builder.append(", hostOutput=");
		builder.append(hostOutput);
		builder.append(", longHostOutput=");
		builder.append(longHostOutput);
		builder.append(", hostCheckCommand=");
		builder.append(hostCheckCommand);
		builder.append(", serviceState=");
		builder.append(serviceState);
		builder.append(", serviceProblemId=");
		builder.append(serviceProblemId);
		builder.append(", lastServiceProblemId=");
		builder.append(lastServiceProblemId);
		builder.append(", serviceEventId=");
		builder.append(serviceEventId);
		builder.append(", serviceDuration=");
		builder.append(serviceDuration);
		builder.append(", serviceOutput=");
		builder.append(serviceOutput);
		builder.append(", longServiceOutput=");
		builder.append(longServiceOutput);
		builder.append(", serviceCheckCommand=");
		builder.append(serviceCheckCommand);
		builder.append(", date=");
		builder.append(date);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}
	
}
