package com.iv.report.dto;

import java.util.Map;

public class GroupByHostDto implements Comparable<GroupByHostDto>{

	private long count;
	private String hostIp;
	private String hostName;
	private Map<String,Long> itemTypeMap;
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	
	
	public Map<String, Long> getItemTypeMap() {
		return itemTypeMap;
	}
	public void setItemTypeMap(Map<String, Long> itemTypeMap) {
		this.itemTypeMap = itemTypeMap;
	}
	@Override
	public int compareTo(GroupByHostDto arg0) {
		// TODO Auto-generated method stub
		if(this.getCount() > arg0.getCount()){
			return -1;
		}else{
			return 1;
		}
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GroupByHostDto [count=");
		builder.append(count);
		builder.append(", hostIp=");
		builder.append(hostIp);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append("]");
		return builder.toString();
	}
	
}
