package com.iv.operation.script.dto;

import java.util.List;

public class ScheduleHostsDto {

	private int scheduleId;// 任务定时策略id
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
