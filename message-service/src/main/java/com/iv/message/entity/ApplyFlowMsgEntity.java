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
 * 申请流审批结果消息体
 * @author macheng
 * 2018年1月26日
 * aggregation-1.3.0-SNAPSHOT
 * 
 */
@Entity
@Table(indexes = {@Index(columnList = "user_id")})
public class ApplyFlowMsgEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8395567024955116185L;
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
	private long approveTime;// 审批时间
	private boolean approved;// 审批结果
	private String remark;// 审批人备注
	private String enterprise;// 企业名称
	
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
	public WorkflowType getType() {
		return type;
	}
	public void setType(WorkflowType type) {
		this.type = type;
	}
	public long getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(long approveTime) {
		this.approveTime = approveTime;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	
}
