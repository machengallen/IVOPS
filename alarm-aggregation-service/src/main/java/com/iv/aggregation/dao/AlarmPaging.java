package com.iv.aggregation.dao;

import java.util.List;

import com.iv.aggregation.entity.AlarmLifeEntity;


public class AlarmPaging {

	private long totalCount;
	private List<AlarmLifeEntity> entries;
	
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<AlarmLifeEntity> getEntries() {
		return entries;
	}
	public void setEntries(List<AlarmLifeEntity> entries) {
		this.entries = entries;
	}
	
}
