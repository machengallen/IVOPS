package com.iv.operation.script.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.iv.operation.script.util.ScriptSourceType;


@Entity
@Table(name = "single_task")
public class SingleTaskEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 825116436471694606L;
	private int id;
	private String taskName;// 作业任务名
	private String taskDescription;// 任务描述
	private ScriptSourceType scriptSrc;// 执行脚本来源
	private int scriptId;// 脚本库文件id
	private List<String> scriptArgs;// 脚本执行传入参数
	private int timeout;// 目标主机连接超时时间(毫秒)
	private SingleTaskLifeEntity taskLife;
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getScriptId() {
		return scriptId;
	}
	public ScriptSourceType getScriptSrc() {
		return scriptSrc;
	}
	public void setScriptSrc(ScriptSourceType scriptSrc) {
		this.scriptSrc = scriptSrc;
	}
	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}
	@ElementCollection(fetch = FetchType.EAGER)
	public List<String> getScriptArgs() {
		return scriptArgs;
	}
	public void setScriptArgs(List<String> scriptArgs) {
		this.scriptArgs = scriptArgs;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public SingleTaskLifeEntity getTaskLife() {
		return taskLife;
	}
	public void setTaskLife(SingleTaskLifeEntity taskLife) {
		this.taskLife = taskLife;
	}
	
	/*@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<TaskTargetEntity> getTargetHosts() {
		return targetHosts;
	}
	public void setTargetHosts(Set<TaskTargetEntity> targetHosts) {
		this.targetHosts = targetHosts;
	}*/
	
}
