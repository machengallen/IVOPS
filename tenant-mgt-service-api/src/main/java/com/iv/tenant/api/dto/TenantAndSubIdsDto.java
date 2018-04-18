package com.iv.tenant.api.dto;

import java.util.List;

public class TenantAndSubIdsDto {

	private List<String> tenantIds;
	private List<String> subTenantIds;
	public List<String> getTenantIds() {
		return tenantIds;
	}
	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}
	public List<String> getSubTenantIds() {
		return subTenantIds;
	}
	public void setSubTenantIds(List<String> subTenantIds) {
		this.subTenantIds = subTenantIds;
	}
	
}
