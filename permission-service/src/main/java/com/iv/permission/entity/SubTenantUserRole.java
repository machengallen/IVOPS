package com.iv.permission.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.context.annotation.Primary;
/**
 * 项目组下人员角色表
 * @author zhangying
 * 2018年5月7日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "SubTenant_User_Role")
public class SubTenantUserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8506005577351384424L;
	private int userId;
	/*项目组下角色列表*/
	private List<SubTenantRole> subTenantRoles;
	
	public SubTenantUserRole() { 
		super();
		// TODO Auto-generated constructor stub
	}
		
	public SubTenantUserRole(int userId, List<SubTenantRole> subTenantRoles) {
		super();
		this.userId = userId;
		this.subTenantRoles = subTenantRoles;
	}

	@Id
	@Primary
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@ManyToMany(fetch = FetchType.EAGER)
	public List<SubTenantRole> getSubTenantRoles() {
		return subTenantRoles;
	}
	public void setSubTenantRoles(List<SubTenantRole> subTenantRoles) {
		this.subTenantRoles = subTenantRoles;
	}
	
	
}
