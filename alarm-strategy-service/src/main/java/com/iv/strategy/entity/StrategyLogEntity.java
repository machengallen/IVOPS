package com.iv.strategy.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.iv.strategy.api.constant.OpsStrategy;

@Entity
@Table(name = "strategy_log_info")
public class StrategyLogEntity {
	private String id;
	// 操作日期
	private Date date;
	// 操作类型
	private OpsStrategy opsStrategy;
	// 创建人
	private String operator;
	//备注
	private String remark;
	public StrategyLogEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StrategyLogEntity(Date date, OpsStrategy opsStrategy, String operator, String remark) {
		super();
		this.date = date;
		this.opsStrategy = opsStrategy;
		this.operator = operator;
		this.remark = remark;
	}
	@Id
	@GenericGenerator(name = "idGen", strategy = "uuid")
	@GeneratedValue(generator = "idGen")
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
