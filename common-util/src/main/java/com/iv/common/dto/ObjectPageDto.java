package com.iv.common.dto;

import java.util.Collection;

public class ObjectPageDto<T> {

	private long total;
	private Collection<T> data;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public Collection<T> getData() {
		return data;
	}
	public void setData(Collection<T> data) {
		this.data = data;
	}
	
}
