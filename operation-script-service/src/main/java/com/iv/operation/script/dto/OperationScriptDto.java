package com.iv.operation.script.dto;

import java.util.Set;

import com.iv.operation.script.util.ScriptSourceType;

public class OperationScriptDto {

	private String name;// 作业任务名
	private String hostIp;// 目标主机ip
	private String account;// 目标主机执行账户
	private String password;// 目标主机执行账户密码
	private ScriptSourceType scriptSrc;// 执行脚本来源
	private String scriptContext;// 手动录入方式的脚本内容
	private int scriptId;// 脚本库文件id
	private Set<String> scriptArgs;// 脚本执行传入参数
	private String timeout;// 目标主机连接超时时间(毫秒)
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getScriptContext() {
		return scriptContext;
	}
	public void setScriptContext(String scriptContext) {
		this.scriptContext = scriptContext;
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
