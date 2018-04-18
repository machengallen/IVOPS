package com.iv.enter.dto;

/**
 * 平台账号信息
 * 1)微信扫码登录后，无平台账号，新注册实体类
 * 2)微信扫码登录后，已存在平台账号，绑定平台账号
 * @author zhangying
 *
 */
public class AccountDto {	
	/**微信标识：unionid*/
	private String unionid;
	private int userId;
	/**平台用户名：用于登录*/
	private String userName;
	/**真实姓名*/
	private String realName;
	/**昵称*/
	private String nickName;
	/**原密码*/
	private String passWord;
	/**新密码*/
	private String passWord1;
	/**确认密码*/
	private String passWord2;
	private String email;
	/**电话*/
	private String tel;
	/**租户Id*/
	private String curTenantId;
	/**邮箱校验码*/
	private String vcode;
	/**登录方式*/
	private String loginType;
	
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getPassWord1() {
		return passWord1;
	}
	public void setPassWord1(String passWord1) {
		this.passWord1 = passWord1;
	}
	public String getPassWord2() {
		return passWord2;
	}
	public void setPassWord2(String passWord2) {
		this.passWord2 = passWord2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCurTenantId() {
		return curTenantId;
	}
	public void setCurTenantId(String curTenantId) {
		this.curTenantId = curTenantId;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
			
	
	
}
