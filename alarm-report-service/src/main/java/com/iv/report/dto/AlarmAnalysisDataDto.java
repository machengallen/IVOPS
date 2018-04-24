package com.iv.report.dto;

public class AlarmAnalysisDataDto {
	private String itemType;
	//按类型或者级别分类后的告警总数
	private long alarmCounts;
	//总故障时长
	private String faultTimes;
	//故障时长占比
	private float faultDutyRatio;
	//平均响应时间
	private String mtta;
	//平均恢复时间
	private String mttr;
	//升级事件量
	private long upAlarms;
	//事件升级率
	private float escalationRate;
	//事件压缩量
	private long pressAlarms;
	//事件压缩比
	private float pressDutyRatio;
	//总事件占比
	private float totalDutyRatio;
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public long getAlarmCounts() {
		return alarmCounts;
	}
	public void setAlarmCounts(long alarmCounts) {
		this.alarmCounts = alarmCounts;
	}
	public String getFaultTimes() {
		return faultTimes;
	}
	public void setFaultTimes(String faultTimes) {
		this.faultTimes = faultTimes;
	}
	public float getFaultDutyRatio() {
		return faultDutyRatio;
	}
	public void setFaultDutyRatio(float faultDutyRatio) {
		this.faultDutyRatio = faultDutyRatio;
	}
	public String getMtta() {
		return mtta;
	}
	public void setMtta(String mtta) {
		this.mtta = mtta;
	}
	public String getMttr() {
		return mttr;
	}
	public void setMttr(String mttr) {
		this.mttr = mttr;
	}
	public long getUpAlarms() {
		return upAlarms;
	}
	public void setUpAlarms(long upAlarms) {
		this.upAlarms = upAlarms;
	}
	public float getEscalationRate() {
		return escalationRate;
	}
	public void setEscalationRate(float escalationRate) {
		this.escalationRate = escalationRate;
	}
	public long getPressAlarms() {
		return pressAlarms;
	}
	public void setPressAlarms(long pressAlarms) {
		this.pressAlarms = pressAlarms;
	}
	public float getPressDutyRatio() {
		return pressDutyRatio;
	}
	public void setPressDutyRatio(float pressDutyRatio) {
		this.pressDutyRatio = pressDutyRatio;
	}
	public float getTotalDutyRatio() {
		return totalDutyRatio;
	}
	public void setTotalDutyRatio(float totalDutyRatio) {
		this.totalDutyRatio = totalDutyRatio;
	}
	
	
	
}
