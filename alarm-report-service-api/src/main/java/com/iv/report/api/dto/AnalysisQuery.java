package com.iv.report.api.dto;
/**
 * 报表分析，起始与结束时间
 * @author zhangying
 * 2018年4月23日
 * aggregation-1.4.0-SNAPSHOT
 */
public class AnalysisQuery {
	private String startTime;
	private String endTime;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	

}
