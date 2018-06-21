package com.iv.permission.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.iv.common.enumeration.ServiceType;
import com.iv.common.enumeration.YesOrNo;
/**
 * 功能描述（权限集合）
 * @author zhangying
 * 2018年5月18日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "function_info")
public class FunctionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 732945762821595063L;
	private int id;
	/*功能名称*/
	private String name;
	/*功能描述*/
	private String des;
	/*服务类别*/
	private ServiceType serviceType;
	/*接口集合*/
	private List<PermissionInfo> permissionInfos;
	/*是否需要订阅*/
	private YesOrNo needSubscribe;
	/*是否发布*/
	private YesOrNo releaseOrNot;
	/*是否有效*/
	private YesOrNo isValid;
	/*创建时间*/
	private long createDate;	
	/*创建人*/
	private int createBy;	

	public FunctionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FunctionInfo(int id, String name, ServiceType serviceType, List<PermissionInfo> permissionInfos,
			YesOrNo needSubscribe, YesOrNo releaseOrNot, YesOrNo isValid) {
		super();
		this.id = id;
		this.name = name;
		this.serviceType = serviceType;
		this.permissionInfos = permissionInfos;
		this.needSubscribe = needSubscribe;
		this.releaseOrNot = releaseOrNot;
		this.isValid = isValid;
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
	@Enumerated(EnumType.STRING)
	public ServiceType getServiceType() {
		return serviceType;
	}
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	@ManyToMany(fetch = FetchType.EAGER)
	public List<PermissionInfo> getPermissionInfos() {
		return permissionInfos;
	}
	public void setPermissionInfos(List<PermissionInfo> permissionInfos) {
		this.permissionInfos = permissionInfos;
	}
	@Enumerated(EnumType.ORDINAL)
	public YesOrNo getNeedSubscribe() {
		return needSubscribe;
	}
	public void setNeedSubscribe(YesOrNo needSubscribe) {
		this.needSubscribe = needSubscribe;
	}	
	
	@Enumerated(EnumType.ORDINAL)
	public YesOrNo getReleaseOrNot() {
		return releaseOrNot;
	}
	public void setReleaseOrNot(YesOrNo releaseOrNot) {
		this.releaseOrNot = releaseOrNot;
	}
	
	@Enumerated(EnumType.ORDINAL)
	public YesOrNo getIsValid() {
		return isValid;
	}

	public void setIsValid(YesOrNo isValid) {
		this.isValid = isValid;
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

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
