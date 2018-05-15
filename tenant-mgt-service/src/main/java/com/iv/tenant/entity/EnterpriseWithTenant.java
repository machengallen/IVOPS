package com.iv.tenant.entity;

import java.util.List;

public class EnterpriseWithTenant {

	private EnterpriseEntity enterpriseEntity;
	private List<SubEnterpriseEntity> subEnterpriseEntities;
	public EnterpriseEntity getEnterpriseEntity() {
		return enterpriseEntity;
	}
	public void setEnterpriseEntity(EnterpriseEntity enterpriseEntity) {
		this.enterpriseEntity = enterpriseEntity;
	}
	public List<SubEnterpriseEntity> getSubEnterpriseEntities() {
		return subEnterpriseEntities;
	}
	public void setSubEnterpriseEntities(List<SubEnterpriseEntity> subEnterpriseEntities) {
		this.subEnterpriseEntities = subEnterpriseEntities;
	}
	
}
