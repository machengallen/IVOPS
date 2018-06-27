package com.iv.constant;

import com.iv.common.response.IErrorMsg;
import net.sf.json.JSONObject;

/**
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
public enum ErrorMsg implements IErrorMsg {



	OK(0,"成功"),
	FORM_INFO_SAVE_FAILED(50100,"保存失败"),
	FORM_HANDLER_ID_FAILED(50101,"服务器内部错误，请退出登录之后重试"),//用户UserId未获取到
	FORM_NULL_FAILED(50102,"请将工单填写完整"),//用户UserId未获取到
	FORM_INFO_FAILED(50103,"工单获取失败，请稍后重试"),//用户UserId未获取到
	FORM_DATA_FAILED(50104,"数据获取失败，请稍后重试"),//
	FORM_MARK_FAILED(50105,"工单标星异常"),//
	FORM_DEL_FAILED(50106,"删除异常"),//
	FORM_SUBMIT_FAILED(50107,"提交失败"),//
	UPLOAD_FILE_SIZE_FAILED(50108,"单个文件超出最大值！！！"),//
	UPLOAD_ALL_SIZE_FAILED(50109,"上传文件的总的大小超出限制的最大值！！！"),//
	UPLOAD_FAILED(50110,"文件上传失败"),//
	UPLOAD_FILE_HAS_DEL(50111,"您要下载的资源已被删除！"),//
	FORM_NO_AUDIT(50112,"请联系管理员配置审核人员"),//



	;



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

	@Override
	public String toString() {
		return toJsonObj().toString();
	}

	public JSONObject toJsonObj() {
		JSONObject object = new JSONObject();
		object.put("code", code);
		object.put("msg", msg);
		return object;
	}

}
