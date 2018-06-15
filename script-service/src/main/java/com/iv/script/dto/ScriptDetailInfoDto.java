package com.iv.script.dto;


import org.springframework.http.ResponseEntity;

import com.iv.script.entity.ScriptEntity;

public class ScriptDetailInfoDto{

	private ScriptEntity scriptEntity;
	private ResponseEntity<byte[]> bytes;
	
	public ScriptDetailInfoDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ScriptDetailInfoDto(ScriptEntity scriptEntity, ResponseEntity<byte[]> bytes) {
		super();
		this.scriptEntity = scriptEntity;
		this.bytes = bytes;
	}
	public ScriptEntity getScriptEntity() {
		return scriptEntity;
	}
	public void setScriptEntity(ScriptEntity scriptEntity) {
		this.scriptEntity = scriptEntity;
	}
	public ResponseEntity<byte[]> getBytes() {
		return bytes;
	}
	public void setBytes(ResponseEntity<byte[]> bytes) {
		this.bytes = bytes;
	}
	
}
