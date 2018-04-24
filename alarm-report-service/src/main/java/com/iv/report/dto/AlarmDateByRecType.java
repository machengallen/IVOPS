package com.iv.report.dto;

public class AlarmDateByRecType {		
	//平均恢复时间
	private String mttr;		
	//总故障时长
	private String faultTimes;
	//故障时长占比
	private Float FaultDutyRatio;
	//压缩告警数、人为处理告警数
	private long alarmCounts;
	//事件量占比
	private Float alarmsDutyRatio;
	public String getMttr() {
		return mttr;
	}
	public void setMttr(String mttr) {
		this.mttr = mttr;
	}
	public String getFaultTimes() {
		return faultTimes;
	}
	public void setFaultTimes(String faultTimes) {
		this.faultTimes = faultTimes;
	}
	public Float getFaultDutyRatio() {
		return FaultDutyRatio;
	}
	public void setFaultDutyRatio(Float faultDutyRatio) {
		FaultDutyRatio = faultDutyRatio;
	}
	public long getAlarmCounts() {
		return alarmCounts;
	}
	public void setAlarmCounts(long alarmCounts) {
		this.alarmCounts = alarmCounts;
	}
	public Float getAlarmsDutyRatio() {
		return alarmsDutyRatio;
	}
	public void setAlarmsDutyRatio(Float alarmsDutyRatio) {
		this.alarmsDutyRatio = alarmsDutyRatio;
	}
	
}
