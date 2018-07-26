package com.iv.message.api.dto;


public class MsgFrontQueryDto {

	private int curPage;
	private int items;
	private String type;// 所有消息-ALL， 告警-ALARM，  租户-TNT， 工单-FORM， 脚本库-SCRIPT
	private int isConfirmed;// 0-未读，1-已读，2-全部
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getItems() {
		return items;
	}
	public void setItems(int items) {
		this.items = items;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsConfirmed() {
		return isConfirmed;
	}
	public void setIsConfirmed(int isConfirmed) {
		this.isConfirmed = isConfirmed;
	}
	
}
