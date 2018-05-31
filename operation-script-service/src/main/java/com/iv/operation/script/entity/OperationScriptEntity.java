package com.iv.operation.script.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.iv.operation.script.util.ScriptSourceType;

@Entity
@Table(name = "operation_script_info")
public class OperationScriptEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 825116436471694606L;
	private int id;
	private String name;// 作业任务名
	private String hostIp;// 目标主机ip
	private String account;// 目标主机执行账户
	private ScriptSourceType scriptSrc;// 执行脚本来源
	private int scriptId;// 脚本库文件id
	private Set<String> scriptArgs;// 脚本执行传入参数
	private String timeout;// 目标主机连接超时时间(毫秒)
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
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public ScriptSourceType getScriptSrc() {
		return scriptSrc;
	}
	public void setScriptSrc(ScriptSourceType scriptSrc) {
		this.scriptSrc = scriptSrc;
	}
	public int getScriptId() {
		return scriptId;
	}
	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}
	public Set<String> getScriptArgs() {
		return scriptArgs;
	}
	public void setScriptArgs(Set<String> scriptArgs) {
		this.scriptArgs = scriptArgs;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
}
