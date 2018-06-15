package com.iv.operation.script.dto;

import java.util.List;

public class ScheduleHostsDto {

	private int taskScheduleId;// 任务定时策略id
	private List<HostDto> targetHosts;
	public int getTaskScheduleId() {
		return taskScheduleId;
	}
	public void setTaskScheduleId(int taskScheduleId) {
		this.taskScheduleId = taskScheduleId;
	}
	public List<HostDto> getTargetHosts() {
		return targetHosts;
	}
	public void setTargetHosts(List<HostDto> targetHosts) {
		this.targetHosts = targetHosts;
	}

}
