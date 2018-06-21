package com.iv.dto;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	OK(0,"ok"),
	
	EMAIL_VCODE_SEND_FAILED(42500,"验证码发送失败"),
	
	EMAIL_SEND_INFo_FAILED(42501,"邮箱发送信息失败"),
	
	EMAIL_SEND_FORMINFo_FAILED(42502,"邮箱发送工单信息失败");
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
