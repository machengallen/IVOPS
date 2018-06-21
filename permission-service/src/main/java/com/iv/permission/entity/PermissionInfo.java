package com.iv.permission.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.context.annotation.Primary;

import com.iv.common.enumeration.YesOrNo;

/**
 * 权限信息（所有api）
 * @author zhangying
 * 2018年5月4日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "Permission_Info")
public class PermissionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3285028164814913572L;
	/*权限编码，供前端使用，唯一*/
	private String code;	
	/*权限描述*/
	private String description;
	/*权限url*/
	private String url;	
	/*是否需要订阅
	private YesOrNo needSubscribe;
	是否发布
	private YesOrNo releaseOrNot;*/
	/*是否有效*/
	private YesOrNo isValid;
	/*服务类型*/
	private String serviceType;
	/*创建时间
	private long createDate;	
	创建人
	private int createBy;*/
	/*更新时间
	private Date updateDate;
	编辑人
	private int updateBy;*/
	public PermissionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}			
	
	
	public PermissionInfo(String code, String description, String url, String serviceType) {
		super();
		this.code = code;
		this.description = description;
		this.url = url;
		this.serviceType = serviceType;
	}

	@Id
	@Primary
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	/*@Enumerated(EnumType.ORDINAL)
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
	
	*/
	@Enumerated(EnumType.ORDINAL)
	public YesOrNo getIsValid() {
		return isValid;
	}

	public void setIsValid(YesOrNo isValid) {
		this.isValid = isValid;
	}
	
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	/*public long getCreateDate() {
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
	}*/

}
