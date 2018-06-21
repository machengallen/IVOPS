package com.iv.permission.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.context.annotation.Primary;

/**
 * 用户角色表：基础库使用
 * @author zhangying
 *
 */
@Entity
@Table(name = "LocalAuth_Role")
public class LocalAuthRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799830316044955940L;
	/*用户主键*/
	private int userId;
	/*角色列表*/ 
	private List<GlobalRole> globalRoles;
	
	public LocalAuthRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LocalAuthRole(int userId, List<GlobalRole> globalRoles) {
		super();
		this.userId = userId;
		this.globalRoles = globalRoles;
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
	public List<GlobalRole> getGlobalRoles() {
		return globalRoles;
	}
	public void setGlobalRoles(List<GlobalRole> globalRoles) {
		this.globalRoles = globalRoles;
	}
	
}
