package com.iv.permission.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

/**
 * 项目组_权限关系表（项目组DB）
 * @author zhangying
 * 2018年5月7日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "SubTenant_Permission")
public class SubTenantPermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8064558131932087952L;
	private int groupPermissionId;
	public SubTenantPermission() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	public SubTenantPermission(int groupPermissionId) {
		super();
		this.groupPermissionId = groupPermissionId;
	}
	
	@Id
	@Primary
	public int getGroupPermissionId() {
		return groupPermissionId;
	}

	public void setGroupPermissionId(int groupPermissionId) {
		this.groupPermissionId = groupPermissionId;
	}	
	
}
