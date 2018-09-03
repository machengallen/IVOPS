package com.iv.strategy.api.constant;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	OK(0,"ok"),
	
	NOT_ZERO_ERROR(44400,"升级时间不能设置为0"),

	CONFIG_STRATEGY_FAILED(44401,"配置分派策略失败"),
	
	GET_STRATEGY_FAILED(44402,"获取升级策略失败"),
	
	DELETE_STRATRGY_FAILED(44403,"删除策略失败"),
	
	ALARM_CLEAN_SET_FAILED(44404,"更新告警清理配置失败"),
	
	NOT_FOCUS_WECHAT(44405,"未关注公众号");
	
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
