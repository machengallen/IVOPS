package com.iv.script.api.constant;

import com.iv.common.response.IErrorMsg;

public enum ErrorMsg implements IErrorMsg {

	OK(0,"ok"),
	
	UPLOAD_FAILED(46100, "上传失败"),
	
	LIST_GET_FAILED(46101, "获取列表失败"),
	
	DOWNLOAD_FAILED(46102, "下载失败"),
	
	DELETE_FAILED(46103, "删除失败"),
	
	SCRIPT_NOT_EMPTY(46104, "文件不能为空"),
	
	SCRIPT_NOT_EXIST(46105, "脚本不存在"),
	
	SCRIPT_DEL_PARTIAL_FAILED(46106, "脚本删除部分失败"),
	
	UPDATE_FAILED(46107, "更新失败"),
	
	ITEMTYPE_SCRIPT_FAILED(46108, "领域脚本量统计"),
	
	PERSONAL_SCRIPT_FAILED(46109, "个人脚本贡献量统计"),
	
	SCRIPT_APPLY_FAILED(46110, "提交脚本审核失败"),
	
	APPROVE_SCRIPT_FAILED(46111, "脚本审核失败"),	
	
	MY_SCRIPT_APPLY_FAILED(46112, "我的脚本申请列表查询失败"),
	
	MY_APPLIED_SCRIPT_FAILED(46113, "我审批的脚本申请列表查询失败"),
	
	SELECT_SCRIPT_INFO_FAILED(46114, "获取脚本详细信息失败"),
	
	DELETE_TEMPORARY_SCRIPT_FAILED(46115, "删除临时文件失败");
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
