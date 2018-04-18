package com.iv.facade.response;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	OK(0,"ok"),
	
	CLAIM_FAILED(4007,"告警认领失败"),
	
	TRANSFER_FAILED(4008,"告警转让失败"),
	
	GET_ALARM_LIFE_FAILED(4009,"告警数据获取失败"),
	
	GET_ALARM_ANALYSIS_FAILED(4009,"获取告警分析数据失败"),
	
	ALARM_CLEAN_SET_FAILED(4044,"更新配置失败");
	
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
