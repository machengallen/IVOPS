package com.iv.dto;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	GROUP_INFO_FAILED(42800,"用户组获取失败"),
	
	GROUP_IN_STRATEGY(42801,"请先删除关联策略"),
	
	GROUP_OPS_FAILED(42802,"用户组操作失败");

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
