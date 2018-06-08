package com.iv.operation.script.util;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	OK(0,"ok"),
	
	EXEC_TASK_FAILED(50200, "作业执行失败"),
	
	SCRIPT_SRCTYPE_UNKNOW(50201, "脚本上传类型未知"),
	
	CREATE_TASK_FAILED(50202, "脚本上传类型未知"),
	
	SSH_CONNECT_FAILED(50203, "获取ssh连接失败"),
	
	GET_TASKS_FAILED(50204, "获取任务列表失败"),
	
	GET_TASKS_TARGET_FAILED(50205, "获取任务执行对象列表失败");
	
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
