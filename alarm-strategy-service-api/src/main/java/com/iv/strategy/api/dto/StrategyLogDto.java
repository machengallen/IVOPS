package com.iv.strategy.api.dto;

import java.util.Date;

import com.iv.strategy.api.constant.OpsStrategy;
public class StrategyLogDto {
	private String id;
	// 操作日期
	private Date date;
	// 操作类型
	private OpsStrategy opsStrategy;
	// 创建人
	private String operator;
	//备注
	private String remark;
	public StrategyLogDto() {
		super();
	}
	public StrategyLogDto(Date date, OpsStrategy opsStrategy, String operator, String remark) {
		super();
		this.date = date;
		this.opsStrategy = opsStrategy;
		this.operator = operator;
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public OpsStrategy getOpsStrategy() {
		return opsStrategy;
	}
	public void setOpsStrategy(OpsStrategy opsStrategy) {
		this.opsStrategy = opsStrategy;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
