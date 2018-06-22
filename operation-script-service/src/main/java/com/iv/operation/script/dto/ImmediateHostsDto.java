package com.iv.operation.script.dto;

import java.io.Serializable;
import java.util.List;

public class ImmediateHostsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8273292545933122416L;
	private int taskId;// 目标任务id
	private List<HostDto> targetHosts;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public List<HostDto> getTargetHosts() {
		return targetHosts;
	}
	public void setTargetHosts(List<HostDto> targetHosts) {
		this.targetHosts = targetHosts;
	}
	
}
