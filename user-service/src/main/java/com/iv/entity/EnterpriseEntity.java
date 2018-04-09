package com.iv.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	private String identifier;// 租户号，对外租户唯一识别号
	private String tenantId;// 租户id，系统内部使用，对应租户db名
	@JsonIgnore
	private String orgCode;
	private String address;
	private String industry;
	private String businessInvolves;
	private String staffSize;
	@JsonIgnore
	private Set<LocalAuth> localAuths;
	
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
	@Column(unique = true, nullable = false)
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
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
	@ManyToMany(fetch = FetchType.EAGER)
	public Set<LocalAuth> getLocalAuths() {
		return localAuths;
	}
	public void setLocalAuths(Set<LocalAuth> localAuths) {
		this.localAuths = localAuths;
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
