package com.iv.operation.script.constant;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	OK(0,"ok"),
	
	EXEC_TASK_FAILED(50200, "作业执行失败"),
	
	SCRIPT_SRCTYPE_UNKNOW(50201, "脚本上传类型未知"),
	
	CREATE_TASK_FAILED(50202, "作业创建失败"),
	
	SSH_CONNECT_FAILED(50203, "获取ssh连接失败"),
	
	GET_DATA_FAILED(50204, "获取数据失败"),
	
	GET_TASKS_TARGET_FAILED(50205, "获取任务执行对象列表失败"),
	
	SCRIPT_NOT_EXIST(50206, "脚本文件不存在或脚本库服务已停止工作"),
	
	MOD_TASK_FAILED(50207, "任务编辑失败"),
	
	CREATE_TASK_SCHEDULE_FAILED(50208, "定时任务创建失败"),
	
	MOD_TASK_SCHEDULE_FAILED(50209, "定时任务修改失败"),
	
	DEL_TASK_FAILED(50210, "任务删除失败"),
	
	DEL_SCHEDULE_FAILED(50211, "定时作业删除失败"),
	
	PAU_SCHEDULE_FAILED(50212, "定时作业暂停失败"),
	
	RES_SCHEDULE_FAILED(50213, "定时作业重启失败");
	
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
