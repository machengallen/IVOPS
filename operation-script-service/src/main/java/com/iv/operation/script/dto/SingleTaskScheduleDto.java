package com.iv.operation.script.dto;

import java.util.List;

import org.quartz.Trigger.TriggerState;

import com.iv.operation.script.entity.ScheduleTargetEntity;

public class SingleTaskScheduleDto {

	private int id;
	private String name;// 定时作业名
	private String taskName;// 单脚本任务名
	private int taskId;// 单脚本任务id
	private String cronExp;// cron触发器表达式
	private String creator;// 定时作业创建人
	private Long creaDate;// 创建时间
	private String modifier;// 定时作业最后修改人
	private Long modDate;// 最后修改时间
	private TriggerState state;// 当前作业执行状态
	private Long nextFireTime;// 下一次触发时间（不再执行则为null）
	private Long previousFireTime;// 上一次触发时间（未执行过则为null）
	private Long startTime;// 作业提交时间（不为null）
	private Long endTime;// 结束时间（未结束的则为null）
	private List<ScheduleTargetEntity> results;// 最近执行结果
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
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
	public List<ScheduleTargetEntity> getResults() {
		return results;
	}
	public void setResults(List<ScheduleTargetEntity> results) {
		this.results = results;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public TriggerState getState() {
		return state;
	}
	public void setState(TriggerState state) {
		this.state = state;
	}
	public Long getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Long nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public Long getPreviousFireTime() {
		return previousFireTime;
	}
	public void setPreviousFireTime(Long previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
}
