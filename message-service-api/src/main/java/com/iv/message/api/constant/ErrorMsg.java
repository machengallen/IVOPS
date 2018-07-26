package com.iv.message.api.constant;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {

	OK(0,"ok"),
	
	GET_SYS_MESSAGE_FAILED(42701,"获取消息失败"),
	
	UPDATE_MSG_FAILED(42702,"操作更新失败"),
	
	PRODUCE_MSG_FAILED(42703,"消息处理失败");
	
	private int code;
    private String msg;
	
	ErrorMsg(int code , String msg){
		this.code = code;
		this.msg = msg;
	}
	
	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}

	@Override
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
