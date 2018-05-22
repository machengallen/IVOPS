package com.iv.tenant.api.dto;

import java.util.List;

public class UserListDto {

	private long total;
	private List<Integer> userIds;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	
}
