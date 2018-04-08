package com.iv.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author macheng 2017年3月2日 上午11:14:02
 */
@Entity
@Table(name = "user_local_authenticate")
public class LocalAuth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7617899464978271941L;
	private int id;
	private String userName;
	@JsonIgnore
	private String passWord;
	private Set<RoleEntity> roles;
	private String realName;
	private String tel;
	//private UserWechatEntity wechatInfo;
	private byte boundFlag;
	private Set<EnterpriseEntity> enterprises;
	private Set<SubEnterpriseEntity> subEnterprise;
	private String curTenantId;
	private Set<UserOauth> UserOauthes;

	public LocalAuth(int id, String userName, String passWord, Set<RoleEntity> roles, String realName, String tel,
			UserWechatEntity wechatInfo, byte boundFlag, Set<EnterpriseEntity> enterprises,
			Set<SubEnterpriseEntity> subEnterprise, String curTenantId) {
		super();
		this.id = id;
		this.userName = userName;
		this.passWord = passWord;
		this.roles = roles;
		this.realName = realName;
		this.tel = tel;
		//this.wechatInfo = wechatInfo;
		this.boundFlag = boundFlag;
		this.enterprises = enterprises;
		this.subEnterprise = subEnterprise;
		this.curTenantId = curTenantId;
	}

	public LocalAuth() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/*@OneToOne
	@JoinColumn(name = "openId", unique = true)
	public UserWechatEntity getWechatInfo() {
		return wechatInfo;
	}

	public void setWechatInfo(UserWechatEntity wechatInfo) {
		this.wechatInfo = wechatInfo;
	}*/

	@Column(unique = true, nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public byte getBoundFlag() {
		return boundFlag;
	}

	public void setBoundFlag(byte boundFlag) {
		this.boundFlag = boundFlag;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	public Set<EnterpriseEntity> getEnterprises() {
		return enterprises;
	}

	public void setEnterprises(Set<EnterpriseEntity> enterprises) {
		this.enterprises = enterprises;
	}

	public String getCurTenantId() {
		return curTenantId;
	}

	public void setCurTenantId(String curTenantId) {
		this.curTenantId = curTenantId;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	public Set<SubEnterpriseEntity> getSubEnterprise() {
		return subEnterprise;
	}

	public void setSubEnterprise(Set<SubEnterpriseEntity> subEnterprise) {
		this.subEnterprise = subEnterprise;
	}
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	public Set<UserOauth> getUserOauthes() {
		return UserOauthes;
	}

	public void setUserOauthes(Set<UserOauth> userOauthes) {
		UserOauthes = userOauthes;
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
		/*if (getClass() != obj.getClass())
			return false;*/
		LocalAuth other = (LocalAuth) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalAuth [id=" + id + ", userName=" + userName + ", realName=" + realName + "]";
	}

}
