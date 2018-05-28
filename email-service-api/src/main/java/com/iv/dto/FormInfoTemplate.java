package com.iv.dto;

public class FormInfoTemplate {
	private String[] toEmails;
	private FormSendType formSendType;
	private FormInfoEntityDto formInfoEntityDto;
	public String[] getToEmails() {
		return toEmails;
	}
	public void setToEmails(String[] toEmails) {
		this.toEmails = toEmails;
	}
	
	public FormSendType getFormSendType() {
		return formSendType;
	}
	public void setFormSendType(FormSendType formSendType) {
		this.formSendType = formSendType;
	}
	public FormInfoEntityDto getFormInfoEntityDto() {
		return formInfoEntityDto;
	}
	public void setFormInfoEntityDto(FormInfoEntityDto formInfoEntityDto) {
		this.formInfoEntityDto = formInfoEntityDto;
	}
	
}
