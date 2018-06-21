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
 * 全局角色表（共享库）
 * @author zhangying
 * 2018年5月4日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "Global_Role")
public class GlobalRole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2091144330657315569L;
	private int id;
	/*角色名称*/
	private String name;
	/*角色描述*/
	private String description;	
	/*项目组id列表*/
	private Set<Integer> subTenantIds;	
	/*创建时间*/
	private long createDate;
	/*创建人*/
	private int createBy;
	/*更新时间
	private Date updateDate;
	编辑人
	private int updateBy;*/
	/*权限集合*/
	private List<FunctionInfo> functionInfos;
	public GlobalRole() {
		super();
		// TODO Auto-generated constructor stub
	}	
	public GlobalRole(int id, String name, String description, Set<Integer> subTenantIds,
			List<FunctionInfo> functionInfos) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.subTenantIds = subTenantIds;
		this.functionInfos = functionInfos;
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
	public List<FunctionInfo> getFunctionInfos() {
		return functionInfos;
	}
	public void setFunctionInfos(List<FunctionInfo> functionInfos) {
		this.functionInfos = functionInfos;
	}
	
	@ElementCollection(fetch = FetchType.EAGER)
	public Set<Integer> getSubTenantIds() {
		return subTenantIds;
	}		
	public void setSubTenantIds(Set<Integer> subTenantIds) {
		this.subTenantIds = subTenantIds;
	}
	
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public int getCreateBy() {
		return createBy;
	}
	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}

}
