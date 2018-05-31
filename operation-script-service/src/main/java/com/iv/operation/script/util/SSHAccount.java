package com.iv.operation.script.util;

/**
 * ssh连接账户信息
 * @author macheng
 * 2018年5月24日
 * operation-script-service
 * 
 */
public class SSHAccount {

	private String userName;
	private String passWord;
	private String ip;
	private int port;
	
	public SSHAccount(String userName, String passWord, String ip) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.ip = ip;
		this.port = Constant.DEFAULT_SSH_PORT;
	}
	
	public SSHAccount() {
		super();
	}

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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
