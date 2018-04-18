package com.iv.facade.dto;

import java.util.List;

public class AlarmPagingDto {
	private long totalCount;
	private List<AlarmLifeDto> entries;
	
	public AlarmPagingDto() {
		super();
	}
	
	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<AlarmLifeDto> getEntries() {
		return entries;
	}
	public void setEntries(List<AlarmLifeDto> entries) {
		this.entries = entries;
	}
	
}
