package com.iv.tenant.api.dto;

import java.util.List;

public class ProcessDataDto {

	private List data;
	private long totalNum;
	
	public ProcessDataDto(List data, long totalNum) {
		super();
		this.data = data;
		this.totalNum = totalNum;
	}
	
	public ProcessDataDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public long getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}
	
}
