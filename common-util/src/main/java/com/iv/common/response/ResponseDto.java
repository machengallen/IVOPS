package com.iv.common.response;

/**
 * @author macheng
 * 2017年3月3日 上午11:14:25
 * 2018年4月8日：修改ErrorMsg类型为interface
 */
public class ResponseDto {
	//返回状态码
	private int errcode;
	//返回错误消息
	private String errmsg;
	//返回消息体
	private Object data;
	public ResponseDto(int errcode, String errmsg, Object data) {
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
		this.data = data;
	}
	public ResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public void setErrorMsg(IErrorMsg errorMsg){
		setErrcode(errorMsg.getCode());
		setErrmsg(errorMsg.getMsg());
	}
	public static ResponseDto builder(IErrorMsg errorMsg) {
		ResponseDto dto = new ResponseDto();
		dto.setErrorMsg(errorMsg);
		return dto;
	}
	
}
