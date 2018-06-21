package com.iv.permission.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
/**
 * 项目组下角色权限表
 * @author zhangying
 * 2018年5月7日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "SubTenant_Role")
public class SubTenantRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2140767089559442483L;
	private int id;
	/*角色名称*/
	private String name;
	/*角色描述*/
	private String description;
	/*角色对应操作的团队id列表*/
	private Set<Short> groupIds;
	/*权限集合*/
	private List<SubTenantPermission> subTenantPermissions;
	/*创建时间*/
	private long createDate;
	/*创建人*/
	private int createBy;
	/*创建时间
	private Date createDate;
	创建人
	private int createBy;
	更新时间
	private Date updateDate;
	编辑人
	private int updateBy;*/
	
	public SubTenantRole() {
		super();
		// TODO Auto-generated constructor stub
	}
				
	public SubTenantRole(int id, String name, String description, Set<Short> groupIds,
			List<SubTenantPermission> subTenantPermissions) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.groupIds = groupIds;
		this.subTenantPermissions = subTenantPermissions;
	}

	@Id
	@GeneratedValue
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
			
	@ManyToMany(fetch = FetchType.EAGER)
	public List<SubTenantPermission> getSubTenantPermissions() {
		return subTenantPermissions;
	}	
	
	public void setSubTenantPermissions(List<SubTenantPermission> subTenantPermissions) {
		this.subTenantPermissions = subTenantPermissions;
	}
	
	@ElementCollection(fetch = FetchType.EAGER)
	public Set<Short> getGroupIds() {
		return groupIds;
	}
	
	public void setGroupIds(Set<Short> groupIds) {
		this.groupIds = groupIds;
	}

	public int getCreateBy() {
		return createBy;
	}

	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	
}
