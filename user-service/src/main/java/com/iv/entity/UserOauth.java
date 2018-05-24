package com.iv.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.iv.enumeration.LoginType;

/**
 * 用户的多种三方登录方式
 * @author zhangying
 * 2018nian 4月4日
 * aggregation-1.4.0-SNAPSHOT
 */
@Entity
@Table(name = "user_oauth")
public class UserOauth implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6863210001150090725L;
	private String id;
	/**用户Id*/
	private int userId;
	/**用户三方登录类型*/
	private LoginType loginType ;
	/**三方登录的唯一标识*/
	private String unionid;
	public UserOauth() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserOauth(int userId, LoginType loginType, String unionid) {
		super();
		this.userId = userId;
		this.loginType = loginType;
		this.unionid = unionid;
	}
	@Id
	@GenericGenerator(name = "idGen", strategy = "uuid")
	@GeneratedValue(generator = "idGen")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Enumerated(EnumType.STRING) 
	public LoginType getLoginType() {
		return loginType;
	}
	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	
}
