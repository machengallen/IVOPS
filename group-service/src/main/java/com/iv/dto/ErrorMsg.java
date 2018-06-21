package com.iv.dto;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {	
	
	OK(0,"ok"),
	
	GROUP_INFO_FAILED(42800,"用户组获取失败"),	
	
	GET_TENANTUSERS_FAILED(42801,"获取项目组下人员列表失败"),
	
	GROUP_CREATE_FAILED(42802,"用户组创建失败"),
	
	GROUP_DELETE_FAILED(42803,"用户组删除失败"),
	
	GROUP_MEMBERIN_FAILED(42804,"用户组添加人员失败"),
	
	GROUP_MEMBEROUT_FAILED(42805,"用户组删除人员失败"),
	
	GROUP_NAMEMOD_FAILED(42806,"修改组名称失败"),
	
	GROUP_NAME_ISEMPTY(42807,"组名称为空，请完善信息");

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
