package com.iv.dto;

import java.util.List;

public class UserPagingDto {
	private long totalCount;
	private List<GroupUserInfosDto> users;

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<GroupUserInfosDto> getUsers() {
		return users;
	}

	public void setUsers(List<GroupUserInfosDto> users) {
		this.users = users;
	}

	

}
