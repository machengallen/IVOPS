package com.iv.common.dto;

import java.util.Collection;

public class ObjectPageDto {

	private long total;
	private Collection<Object> data;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public Collection<Object> getData() {
		return data;
	}
	public void setData(Collection<Object> data) {
		this.data = data;
	}
	
}
