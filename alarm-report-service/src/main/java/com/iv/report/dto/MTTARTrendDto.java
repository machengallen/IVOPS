package com.iv.report.dto;

public class MTTARTrendDto {

	// 平均响应时间（分钟）
	private float MTTA;
	// 平均解决时间（分钟）
	private float MTTR;
	//告警总数
	private long counts;
	public float getMTTA() {
		return MTTA;
	}
	public void setMTTA(float mTTA) {
		MTTA = mTTA;
	}
	public float getMTTR() {
		return MTTR;
	}
	public void setMTTR(float mTTR) {
		MTTR = mTTR;
	}
	public long getCounts() {
		return counts;
	}
	public void setCounts(long counts) {
		this.counts = counts;
	}
	
}
