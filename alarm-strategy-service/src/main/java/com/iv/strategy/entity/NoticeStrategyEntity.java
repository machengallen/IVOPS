package com.iv.strategy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notice_strategy")
public class NoticeStrategyEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4116054993682772787L;
	private int userId;
	private boolean wechatNotice;
	private boolean emailNotice;
	@Id
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean isWechatNotice() {
		return wechatNotice;
	}
	public void setWechatNotice(boolean wechatNotice) {
		this.wechatNotice = wechatNotice;
	}
	public boolean isEmailNotice() {
		return emailNotice;
	}
	public void setEmailNotice(boolean emailNotice) {
		this.emailNotice = emailNotice;
	}
}
