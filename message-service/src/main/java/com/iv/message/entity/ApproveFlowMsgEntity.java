package com.iv.message.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iv.common.enumeration.WorkflowType;

/**
 * 待审批消息体
 * @author macheng
 * 2018年1月26日
 * aggregation-1.3.0-SNAPSHOT
 * 
 */
@Entity
@Table(indexes = {@Index(columnList = "user_id")})
public class ApproveFlowMsgEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7529175435450614072L;
	/*
	 * 消息类属性
	 */
	private String id;
	@JsonIgnore
	private int userId;
	private boolean confirmed;// 消息是否被确认
	@JsonIgnore
	private long msgDate;// 消息创建时间
	/*
	 * 业务类属性
	 */
	private WorkflowType type;// 工作流类型
	private long applyTime;// 申请时间
	private String applicant;// 申请人
	private String enterprise;// 企业名称
	private String subEnterprise;// 项目组名称
	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "user_id")
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public long getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(long msgDate) {
		this.msgDate = msgDate;
	}
	public String getSubEnterprise() {
		return subEnterprise;
	}
	public void setSubEnterprise(String subEnterprise) {
		this.subEnterprise = subEnterprise;
	}
	public WorkflowType getType() {
		return type;
	}
	public void setType(WorkflowType type) {
		this.type = type;
	}
	public long getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	
}
