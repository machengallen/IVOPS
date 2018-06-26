package com.iv.operation.script.dto;

import java.io.Serializable;
import java.util.List;

public class ScheduleHostsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -764252758862906442L;
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
