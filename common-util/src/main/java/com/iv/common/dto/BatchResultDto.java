package com.iv.common.dto;

import java.util.List;

public class BatchResultDto<T> {

	private int total;
	private int failed;
	private List<T> failedMsg;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getFailed() {
		return failed;
	}
	public void setFailed(int failed) {
		this.failed = failed;
	}
	public List<T> getFailedMsg() {
		return failedMsg;
	}
	public void setFailedMsg(List<T> failedMsg) {
		this.failedMsg = failedMsg;
	}
	
}
