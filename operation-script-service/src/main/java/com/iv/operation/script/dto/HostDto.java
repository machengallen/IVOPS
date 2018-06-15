package com.iv.operation.script.dto;

import java.io.Serializable;

public class HostDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7044973479683656637L;
	private String hostIp;// 目标主机ip
	private Integer port;// 目标主机sshd端口号
	private String account;// 目标主机执行账户
	private String password;// 目标执行账户密码
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
