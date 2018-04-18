package com.iv.dto;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	AUTH_ILLEGAL(42200,"用户名或密码错误"),
	
	USERNAME_EXIST(42201,"用户名已存在"),
	
	USER_NOT_EXIST(42202,"用户名不存在"),
	
	WECHAT_BINDING_ILLEGAL(42203,"该微信号已绑定其他账户"),
	
	ACCOUNT_HAS_WECHAT_BOUNDED(42204,"该账号已绑定微信"),
	
	INCOMPLETE_INFO(42205,"请输入完整信息"),
	
	WECHAT_BINDING_FAILED(42206,"微信绑定失败"),
	
	ACCOUNT_REGISTER_FAILED(42407,"账号注册失败"),
	
	PASSWORD_DIFF(42208,"密码确认失败"),
	
	UPDATE_USERINFO_FAILED(42209,"用户信息更新失败");

	private int code;
    private String msg;
	
	ErrorMsg(int code , String msg){
		this.code = code;
		this.msg = msg;
	}
	
	@Override
	public int getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public void setCode(int code) {
		// TODO Auto-generated method stub
		this.code = code;
	}

	@Override
	public String getMsg() {
		// TODO Auto-generated method stub
		return msg;
	}

	@Override
	public void setMsg(String msg) {
		// TODO Auto-generated method stub
		this.msg = msg;
	}

}
