package com.iv.permission.dto;

import java.util.List;

import com.iv.permission.entity.SubTenantRole;

public class SubTenantRolePageDto {
	private long totalCount;
	private List<SubTenantRole> subTenantRoles;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<SubTenantRole> getSubTenantRoles() {
		return subTenantRoles;
	}
	public void setSubTenantRoles(List<SubTenantRole> subTenantRoles) {
		this.subTenantRoles = subTenantRoles;
	}
	
}
