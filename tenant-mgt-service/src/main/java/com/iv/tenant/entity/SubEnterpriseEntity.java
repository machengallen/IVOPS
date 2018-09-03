package com.iv.tenant.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sub_enterprise_tenant")
public class SubEnterpriseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7767847967625426357L;
	private int id;
	private String name;
	private String subIdentifier;// 项目组标识符,唯一
	private String tenantId;// 租户id,隔离租户数据,DB名
	//@JsonIgnore
	private EnterpriseEntity enterprise;
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
	@Column(unique = true, nullable = false)
	public String getSubIdentifier() {
		return subIdentifier;
	}
	public void setSubIdentifier(String subIdentifier) {
		this.subIdentifier = subIdentifier;
	}
	@Column(unique = true, nullable = false)
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	@Column(nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	public EnterpriseEntity getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(EnterpriseEntity enterprise) {
		this.enterprise = enterprise;
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
		SubEnterpriseEntity other = (SubEnterpriseEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
