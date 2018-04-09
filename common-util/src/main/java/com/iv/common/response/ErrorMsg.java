package com.iv.common.response;


public enum ErrorMsg implements IErrorMsg{

	OK(0,"ok"),
	
	UNKNOWN(4001,"系统未知错误");
	
	private int code;
    private String msg;
	
    ErrorMsg(int code , String msg){
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
