package com.iv.operation.script.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "single_task_schedule")
public class SingleTaskScheduleEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2704151605323403590L;
	private int id;
	@JsonIgnore
	private SingleTaskEntity singleTask;
	private String cronExp;// cron触发器表达式
	private String creator;// 作业创建人
	private Long creaDate;// 创建时间
	private String modifier;// 作业最后修改人
	private Long modDate;// 最后修改时间
	//private String executor;// 最后执行人
	//private Long execDate;// 最后执行日期
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public SingleTaskEntity getSingleTask() {
		return singleTask;
	}
	public void setSingleTask(SingleTaskEntity singleTask) {
		this.singleTask = singleTask;
	}
	public String getCronExp() {
		return cronExp;
	}
	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Long getCreaDate() {
		return creaDate;
	}
	public void setCreaDate(Long creaDate) {
		this.creaDate = creaDate;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Long getModDate() {
		return modDate;
	}
	public void setModDate(Long modDate) {
		this.modDate = modDate;
	}
	
}
