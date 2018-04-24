package com.iv.report.dto;

import java.util.Map;
import java.util.Set;

public class Overview1Dto {

	private Set<GroupByHostDto> hostsTop;
	private Set<GroupBySeverityDto> severityRatio;
	private Map<String,GroupByItemTypeDto> itemTypeRatio;
	private AlarmAndHostNumDto unrecovered;
	public Set<GroupByHostDto> getHostsTop() {
		return hostsTop;
	}
	public void setHostsTop(Set<GroupByHostDto> hostsTop) {
		this.hostsTop = hostsTop;
	}
	public Set<GroupBySeverityDto> getSeverityRatio() {
		return severityRatio;
	}
	public void setSeverityRatio(Set<GroupBySeverityDto> severityRatio) {
		this.severityRatio = severityRatio;
	}
	public AlarmAndHostNumDto getUnrecovered() {
		return unrecovered;
	}
	public void setUnrecovered(AlarmAndHostNumDto unrecovered) {
		this.unrecovered = unrecovered;
	}
	public Map<String, GroupByItemTypeDto> getItemTypeRatio() {
		return itemTypeRatio;
	}
	public void setItemTypeRatio(Map<String, GroupByItemTypeDto> itemTypeRatio) {
		this.itemTypeRatio = itemTypeRatio;
	}
	
	
}
