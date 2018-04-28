package com.iv.aggregation.api.dto;

import com.iv.aggregation.api.constant.AlarmQueryType;
import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.Severity;


public class AlarmQueryDto {

	private int curPage;
	private int items;
	private String id;
	private AlarmStatus alarmStatus;
	private int handlerCurrent;
	private String hostIp;
	private String hostName;
	private Severity severity;
	private byte upgrade;
	private String itemType;
	private AlarmQueryType alarmQueryType;
	//private short groupId;
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getItems() {
		return items;
	}
	public void setItems(int items) {
		this.items = items;
	}
	public AlarmStatus getAlarmStatus() {
		return alarmStatus;
	}
	public void setAlarmStatus(AlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(byte upgrade) {
		this.upgrade = upgrade;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public AlarmQueryType getAlarmQueryType() {
		return alarmQueryType;
	}
	public void setAlarmQueryType(AlarmQueryType alarmQueryType) {
		this.alarmQueryType = alarmQueryType;
	}
	public int getHandlerCurrent() {
		return handlerCurrent;
	}
	public void setHandlerCurrent(int handlerCurrent) {
		this.handlerCurrent = handlerCurrent;
	}
	
}
