package com.iv.operation.script.dto;

import java.util.List;

import com.iv.operation.script.entity.SingleTaskEntity;

public class SingleTaskPageDto {

	private long totalCount;
	private List<SingleTaskEntity> entries;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<SingleTaskEntity> getEntries() {
		return entries;
	}
	public void setEntries(List<SingleTaskEntity> entries) {
		this.entries = entries;
	}
	
}
