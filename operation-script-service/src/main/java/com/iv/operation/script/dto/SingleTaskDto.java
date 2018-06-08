package com.iv.operation.script.dto;

import java.util.Set;

import com.iv.operation.script.util.ScriptSourceType;

public class SingleTaskDto {

	private String taskName;// 作业任务名
	private String taskDescription;// 任务描述
	private ScriptSourceType scriptSrc;// 执行脚本来源
	private int scriptId;// 脚本库文件id
	private Set<String> scriptArgs;// 脚本执行传入参数
	private int timeout;// 目标主机连接超时时间(毫秒)
	
	public SingleTaskDto(String taskName, String taskDescription, ScriptSourceType scriptSrc, int scriptId,
			Set<String> scriptArgs, int timeout) {
		super();
		this.taskName = taskName;
		this.taskDescription = taskDescription;
		this.scriptSrc = scriptSrc;
		this.scriptId = scriptId;
		this.scriptArgs = scriptArgs;
		this.timeout = timeout;
	}
	
	public SingleTaskDto() {
		super();
	}

	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
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
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
