package com.iv.message.api.dto;

import java.util.List;

import com.iv.message.api.constant.MsgSysType;


public class MsgClearReq {

	private MsgSysType type;
	private List<String> ids;
	public MsgSysType getType() {
		return type;
	}
	public void setType(MsgSysType type) {
		this.type = type;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
}
