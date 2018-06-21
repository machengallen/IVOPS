package com.iv.permission.response;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {
	
	OK(0,"ok"),
	
	GET_OPERATION_AUTHORITY(82900,"获取权限列表失败"),
	
	CREATE_SUBTENANT_ROLE_FAILED(82901,"角色创建失败"),
	
	CREATE_SUBTENANT_ADMIN_FAILED(82902,"预设项目组创建者失败"),
	
	CREATE_FUNCTION_FAILED(82903,"功能创建失败"),
	
	INFORMATION_INCOMPLETE(82904,"信息不完整"),
	
	EDIT_SUBTENANT_ROLE_FAILED(82905,"角色编辑失败"),
	
	DELETE_SUBTENANT_ROLE_FAILED(82906,"角色删除失败"),
	
	GET_SUBTENANT_ROLE_FAILED(82907, "查询角色列表失败"),
	
	GIVE_USER_ROLE_FAILED(82908, "用户角色赋予失败 "),
	
	CREATE_PERMISSION_FAILED(82909, "创建权限失败"),
	
	DELETE_PERMISSION_FAILED(82910, "删除权限失败"),
	
	EDIT_PERMISSION_FAILED(82911, "编辑权限失败"),
	
	DELETE_FUNCTION_FAILED(82912,"功能删除失败"),
	
	EDIT_FUNCTION_FAILED(82913,"功能删除失败"),
	
	GET_GLOBAL_ROLE_FAILED(82914,"获取角色列表失败");
	
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
