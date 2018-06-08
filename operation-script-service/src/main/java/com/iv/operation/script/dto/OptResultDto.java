package com.iv.operation.script.dto;

public class OptResultDto {

	private boolean isSuccess;
	private String result;
	
	public OptResultDto(boolean isSuccess, String result) {
		super();
		this.isSuccess = isSuccess;
		this.result = result;
	}
	public OptResultDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public static OptResultDto build(boolean isSuccess, String result) {
		return new OptResultDto(isSuccess, result);
	}
}
