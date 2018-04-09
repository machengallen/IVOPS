package com.iv.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	private String subIdentifier;// 项目组标识符
	private String subTenantId;//　子租户id
	//@JsonIgnore
	private EnterpriseEntity enterprise;
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
	@Column(unique = true, nullable = false)
	public String getSubIdentifier() {
		return subIdentifier;
	}
	public void setSubIdentifier(String subIdentifier) {
		this.subIdentifier = subIdentifier;
	}
	@Column(unique = true, nullable = false)
	public String getSubTenantId() {
		return subTenantId;
	}
	public void setSubTenantId(String subTenantId) {
		this.subTenantId = subTenantId;
	}
	@Column(unique = true, nullable = false)
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
		SubEnterpriseEntity other = (SubEnterpriseEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
