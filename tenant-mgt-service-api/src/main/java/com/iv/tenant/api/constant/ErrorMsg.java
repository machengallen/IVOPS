package com.iv.tenant.api.constant;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	OK(0,"ok"),
	
	ENTER_LIST_GET_FAILED(42300,"获取企业信息失败"),
	
	GET_SUBTENANT_INFO_FAILED(42301,"获取项目组信息失败"),
	
	TENANT_CREATE_FAILED(42302,"申请注册租户失败"),
	
	TENANT_APPROVE_FAILED(42303,"租户审批失败"),
	
	APPLY_JOIN_ENTER_FAILED(42304,"申请加入租户失败"),
	
	TENANT_USERADD_FAILED(42305,"添加用户失败"),
	
	DEL_SUBTENANT_FAILED(42306,"删除项目组失败"),
	
	SWITCH_TENANT_FAILED(42307,"切换租户失败"),
	
	CREATE_SUBTENANT_FAILED(42308,"创建项目组失败"),
	
	TENANT_EXIST(42309,"租户已存在"),
	
	TENANT_APPLY_EXIST(42310,"该申请已在审批中"),
	
	ALREADY_IN_TENANT(42311,"您已加入该项目组，请勿重复申请"),
	
	TENANT_INFO_GET_FAILED(42312,"查询企业信息失败"),
	
	TENANT_NOT_EXIST(42313,"租户不存在"),
	
	SUB_ENTERPRISE_EXIST(42314, "项目组已存在");
	
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
