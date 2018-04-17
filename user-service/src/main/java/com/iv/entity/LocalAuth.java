package com.iv.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author zhangying
 * 2018年4月10日
 * aggregation-1.4.0-SNAPSHOT
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
	//private Set<RoleEntity> roles;
	private String realName;
	private String tel;
	private byte boundFlag;
	private Set<Integer> enterprisIds;
	private Set<Integer> subEnterpriIds;
	private String curTenantId;
	private Set<UserOauth> UserOauthes;

	public LocalAuth(int id, String userName, String passWord, String realName, String tel, byte boundFlag,
			Set<Integer> enterprisIds, Set<Integer> subEnterpriIds, String curTenantId, Set<UserOauth> userOauthes) {
		super();
		this.id = id;
		this.userName = userName;
		this.passWord = passWord;
		this.realName = realName;
		this.tel = tel;
		this.boundFlag = boundFlag;
		this.enterprisIds = enterprisIds;
		this.subEnterpriIds = subEnterpriIds;
		this.curTenantId = curTenantId;
		UserOauthes = userOauthes;
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

	public String getCurTenantId() {
		return curTenantId;
	}

	public void setCurTenantId(String curTenantId) {
		this.curTenantId = curTenantId;
	}
	
	public Set<Integer> getEnterprisIds() {
		return enterprisIds;
	}

	public void setEnterprisIds(Set<Integer> enterprisIds) {
		this.enterprisIds = enterprisIds;
	}

	public Set<Integer> getSubEnterpriIds() {
		return subEnterpriIds;
	}

	public void setSubEnterpriIds(Set<Integer> subEnterpriIds) {
		this.subEnterpriIds = subEnterpriIds;
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
