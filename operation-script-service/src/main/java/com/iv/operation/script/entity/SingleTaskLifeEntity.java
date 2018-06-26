package com.iv.operation.script.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "single_task_life")
public class SingleTaskLifeEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1118232665514759958L;
	private int id;
	private String creator;// 任务创建人
	private Long creaDate;// 创建时间
	private String modifier;// 任务最后修改人
	private Long modDate;// 最后修改时间
	//private String executor;// 最后执行人
	//private Long execDate;// 最后执行日期
	private int execNum;// 总计执行次数
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	/*public String getExecutor() {
		return executor;
	}
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	public Long getExecDate() {
		return execDate;
	}
	public void setExecDate(Long execDate) {
		this.execDate = execDate;
	}*/
	public int getExecNum() {
		return execNum;
	}
	public void setExecNum(int execNum) {
		this.execNum = execNum;
	}
	public void execNumAdd(){
		this.execNum++;
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
