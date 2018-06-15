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
	
}
