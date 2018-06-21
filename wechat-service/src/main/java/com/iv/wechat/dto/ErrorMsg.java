package com.iv.wechat.dto;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	OK(0,"ok"),

	GET_WEXIN_LOGINCODE_FAILED(42400,"获取微信登录二维码失败"),
	
	WECHAT_CALLBACK_FAILURE(42401,"微信回调失败"),				
	
	WECHAT_UNBOUNDED(42404,"请绑定微信账号"),
	
	WECHAT_BINDING_ILLEGAL(42405,"该微信号已绑定其他账户"),

	QRCODE_CREATE_FAILED(42406,"二维码加载失败"),
	
	SEND_WECHATINFO_FAILED(42407,"发送微信消息失败"),
	
	SEND_FORM_WECHATINFO_FAILED(42408,"发送微信消息失败");
	
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
