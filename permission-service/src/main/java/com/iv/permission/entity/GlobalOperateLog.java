package com.iv.permission.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.iv.permission.enumeration.OperateType;

/**
 * 全局权限操作日志
 * @author zhangying
 * 2018年5月7日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "Global_Operate_Log")
public class GlobalOperateLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 192771211622967603L;
	private String id;
	/*操作类型*/
	private OperateType operateType;
	/*操作时间*/
	private Date operateDate; 
	/*操作人
	private int operateBy;*/
	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Enumerated(EnumType.STRING)
	public OperateType getOperateType() {
		return operateType;
	}
	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
	}
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}


}
