package com.iv.facade.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import javax.print.attribute.standard.Severity;

import com.iv.facade.constant.AgentType;


/**
 * 告警数据源实体类
 * 
 * @author macheng
 *
 */
public class AlarmSourceDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7270278770210943757L;
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
	// 三方监控工具
	private AgentType agentType;
	// 租户id
	private String tenantId;

	public AlarmSourceDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AlarmSourceDto(String alarmId, String monitorIp, Long eventId, String title, String content,
			String hostName, String hostIp, Severity severity, String itemKey, Date eventData, Time eventTime,
			String details, AgentType agentType, String tenantId) {
		super();
		this.alarmId = alarmId;
		this.monitorIp = monitorIp;
		this.eventId = eventId;
		this.title = title;
		this.content = content;
		this.hostName = hostName;
		this.hostIp = hostIp;
		this.severity = severity;
		this.itemKey = itemKey;
		this.eventData = eventData;
		this.eventTime = eventTime;
		this.details = details;
		this.agentType = agentType;
		this.tenantId = tenantId;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
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

	public AgentType getAgentType() {
		return agentType;
	}

	public void setAgentType(AgentType agentType) {
		this.agentType = agentType;
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

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
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

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
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

	/*public String getDetails() {
		if(null == details) {
			return null;
		}
		String reString = "";
		try {
			Reader is = details.getCharacterStream();// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
				sb.append(s);
				s = br.readLine();
			}
			reString = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reString;
	}

	public void setDetails(String details) {
		try {
			if (null != details) {
				this.details = new SerialClob(details.toCharArray());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.details = details;
	}*/

}
