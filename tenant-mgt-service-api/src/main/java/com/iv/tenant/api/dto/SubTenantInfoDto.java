package com.iv.tenant.api.dto;

import java.util.List;

public class SubTenantInfoDto {

	private String name;
	private List<Integer> userIds;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	
}
