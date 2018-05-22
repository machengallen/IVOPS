package com.iv.permission.api.dto;

import java.util.Set;

public class SubTenantUserRoleDto {
	private int userId;
	/*项目组下角色列表*/
	private Set<Integer> roleIds;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Set<Integer> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(Set<Integer> roleIds) {
		this.roleIds = roleIds;
	}
	
}
