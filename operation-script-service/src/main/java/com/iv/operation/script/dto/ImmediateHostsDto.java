package com.iv.operation.script.dto;

import java.io.Serializable;
import java.util.List;

public class ImmediateHostsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8273292545933122416L;
	private int scheduleId;// 目标任务id
	private List<HostDto> targetHosts;
	
	public int getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	public List<HostDto> getTargetHosts() {
		return targetHosts;
	}
	public void setTargetHosts(List<HostDto> targetHosts) {
		this.targetHosts = targetHosts;
	}
	
}
