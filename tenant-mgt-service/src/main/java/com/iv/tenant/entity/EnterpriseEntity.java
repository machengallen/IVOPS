package com.iv.tenant.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "enterprise_tenant")
public class EnterpriseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 805513058610566951L;
	private int id;
	private String name;
	private String identifier;// 租户号，对外租户唯一识别号(8位)
	/*
	 * macheng
	 * 改用项目组表维护tenantId
	 * 2018/4/19
	 * 
	 */
	//private String tenantId;// 租户id，系统内部使用，对应租户db名
	@JsonIgnore
	private String orgCode;
	private String address;
	private String industry;
	private String businessInvolves;
	private String staffSize;
	@JsonIgnore
	private Set<Integer> userIds;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(unique = true, nullable = false)
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/*@Column(unique = true, nullable = false)
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}*/
	@Column(unique = true)
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getBusinessInvolves() {
		return businessInvolves;
	}
	public void setBusinessInvolves(String businessInvolves) {
		this.businessInvolves = businessInvolves;
	}
	public String getStaffSize() {
		return staffSize;
	}
	public void setStaffSize(String staffSize) {
		this.staffSize = staffSize;
	}
	@ElementCollection(fetch = FetchType.EAGER)
	public Set<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(Set<Integer> userIds) {
		this.userIds = userIds;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnterpriseEntity other = (EnterpriseEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
