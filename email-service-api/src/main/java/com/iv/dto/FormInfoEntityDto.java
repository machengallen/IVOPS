package com.iv.dto;

public class FormInfoEntityDto {
	/*工单状态*/
	private String 	formState;
	/*需求单位*/
	private String unitCode;
	/*需求类别*/
	private String demandTypeCode;
	/*工单号*/
	private String id;
	/*工单级别*/
	private String priority;
	/*处理人*/
	private String handler;
	/*工单预期处理时间*/
	private String formExpectEndTime;
	/*工单预期结束时间*/
	private String formApplyTime;
	/*工单需求*/
	private String demandContent;
		
	public String getFormState() {
		return formState;
	}
	public void setFormState(String formState) {
		this.formState = formState;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getDemandTypeCode() {
		return demandTypeCode;
	}
	public void setDemandTypeCode(String demandTypeCode) {
		this.demandTypeCode = demandTypeCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public String getFormExpectEndTime() {
		return formExpectEndTime;
	}
	public void setFormExpectEndTime(String formExpectEndTime) {
		this.formExpectEndTime = formExpectEndTime;
	}
	public String getFormApplyTime() {
		return formApplyTime;
	}
	public void setFormApplyTime(String formApplyTime) {
		this.formApplyTime = formApplyTime;
	}
	public String getDemandContent() {
		return demandContent;
	}
	public void setDemandContent(String demandContent) {
		this.demandContent = demandContent;
	}
	
}