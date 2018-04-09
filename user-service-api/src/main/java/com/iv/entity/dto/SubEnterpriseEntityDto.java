package com.iv.entity.dto;
import java.util.Set;

public class SubEnterpriseEntityDto {

	private int id;
	private String name;
	private String subIdentifier;// 项目组标识符
	private String subTenantId;//　子租户id
	private EnterpriseEntityDto enterprise;	
	private Set<LocalAuthDto> localAuths;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubIdentifier() {
		return subIdentifier;
	}
	public void setSubIdentifier(String subIdentifier) {
		this.subIdentifier = subIdentifier;
	}
	public String getSubTenantId() {
		return subTenantId;
	}
	public void setSubTenantId(String subTenantId) {
		this.subTenantId = subTenantId;
	}
	public EnterpriseEntityDto getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(EnterpriseEntityDto enterprise) {
		this.enterprise = enterprise;
	}
	public Set<LocalAuthDto> getLocalAuths() {
		return localAuths;
	}
	public void setLocalAuths(Set<LocalAuthDto> localAuths) {
		this.localAuths = localAuths;
	}
	
}
