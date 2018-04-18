package com.iv.tenant.api.dto;

import java.util.List;

public class EnterpriseInfoResp {

	private EnterpriseInfoDto enterprise;
	private List<SubEnterpriseInfoDto> subEnterprises;
	
	public EnterpriseInfoDto getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(EnterpriseInfoDto enterprise) {
		this.enterprise = enterprise;
	}
	public List<SubEnterpriseInfoDto> getSubEnterprises() {
		return subEnterprises;
	}
	public void setSubEnterprises(List<SubEnterpriseInfoDto> subEnterprises) {
		this.subEnterprises = subEnterprises;
	}
	
}
