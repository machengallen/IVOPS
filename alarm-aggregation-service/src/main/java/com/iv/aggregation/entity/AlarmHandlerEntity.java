package com.iv.aggregation.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户信息
 * @author macheng
 * 2018年5月14日
 * alarm-aggregation-service
 * 
 */
//@Entity
//@Table(name = "alarm_handler")
public class AlarmHandlerEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7929996377919411705L;
	private int handlerId;
	private String handlerName;
	private String email;
	private String tel;
	
	@Id
	public int getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(int handlerId) {
		this.handlerId = handlerId;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
