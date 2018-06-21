package com.iv.permission.dto;

import java.util.List;

import com.iv.permission.entity.PermissionInfo;

public class PermissionPageDto {
	private long totalCount;
	private List<PermissionInfo> permissions;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<PermissionInfo> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<PermissionInfo> permissions) {
		this.permissions = permissions;
	}
	
}
