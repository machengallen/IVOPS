package com.iv.report.dto;

import com.iv.common.enumeration.Severity;

public class GroupBySeverityDto implements Comparable<GroupBySeverityDto>{

	private Severity severity;
	private Long count;
	private Float proportion;
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Float getProportion() {
		return proportion;
	}
	public void setProportion(Float proportion) {
		this.proportion = proportion;
	}
	@Override
	public int compareTo(GroupBySeverityDto arg0) {
		// TODO Auto-generated method stub
		if(this.getCount() > arg0.getCount()){
			return 1;
		}else{
			return -1;
		}
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GroupBySeverityDto [severity=");
		builder.append(severity);
		builder.append(", count=");
		builder.append(count);
		builder.append(", proportion=");
		builder.append(proportion);
		builder.append("]");
		return builder.toString();
	}
	
}
