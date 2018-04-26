package com.iv.aggregation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class AlarmProxyCerEntity {

	private String token;
	private String tenantId;
	@Id
	@GenericGenerator(name = "idGen", strategy = "uuid")
	@GeneratedValue(generator = "idGen")
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Column(unique = true, nullable = false)
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
}
