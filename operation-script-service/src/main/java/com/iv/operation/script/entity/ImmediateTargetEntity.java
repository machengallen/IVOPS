package com.iv.operation.script.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "immediate_target")
public class ImmediateTargetEntity {

	@JsonIgnore
	private String id;
	@JsonIgnore
	private SingleTaskEntity task;
	private String hostIp;// 目标主机ip
	private int port;// 目标主机sshd端口号
	private String account;// 目标主机执行账户
	@JsonIgnore
	private String password;// 目标执行账户密码
	private boolean isSuccess;// 执行是否成功
	private String result;// 执行返回结果
	
	public ImmediateTargetEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImmediateTargetEntity(SingleTaskEntity task, String hostIp, int port, String account, String password,
			boolean isSuccess, String result) {
		super();
		this.task = task;
		this.hostIp = hostIp;
		this.port = port;
		this.account = account;
		this.password = password;
		this.isSuccess = isSuccess;
		this.result = result;
	}
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(generator = "idGenerator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public SingleTaskEntity getTask() {
		return task;
	}
	public void setTask(SingleTaskEntity task) {
		this.task = task;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
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
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
