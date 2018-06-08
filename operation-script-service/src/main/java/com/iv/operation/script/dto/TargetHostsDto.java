package com.iv.operation.script.dto;

import java.util.List;

public class TargetHostsDto {

	private int singleTaskId;// 目标任务id
	private List<HostDto> targetHosts;
	public int getSingleTaskId() {
		return singleTaskId;
	}
	public void setSingleTaskId(int singleTaskId) {
		this.singleTaskId = singleTaskId;
	}
	public List<HostDto> getTargetHosts() {
		return targetHosts;
	}
	public void setTargetHosts(List<HostDto> targetHosts) {
		this.targetHosts = targetHosts;
	}
	
}
