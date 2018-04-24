package com.iv.report.dto;

import java.util.Map;

public class OverviewDto {

	// 平均响应时间（分钟）
	private float MTTA;
	// 平均解决时间（分钟）
	private float MTTR;
	// 告警数量
	private long alarmNum;
	// 故障主机数量
	private long hostNum;
	// 各监控类型的告警数量
	private Map<String, Integer> itemsTypeNum;

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

	public long getAlarmNum() {
		return alarmNum;
	}

	public void setAlarmNum(long alarmNum) {
		this.alarmNum = alarmNum;
	}

	public long getHostNum() {
		return hostNum;
	}

	public void setHostNum(long hostNum) {
		this.hostNum = hostNum;
	}

	public Map<String, Integer> getItemsTypeNum() {
		return itemsTypeNum;
	}

	public void setItemsTypeNum(Map<String, Integer> itemsTypeNum) {
		this.itemsTypeNum = itemsTypeNum;
	}

}
