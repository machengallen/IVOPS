package com.iv.script.api.dto;

public class TemporaryScriptDto {
	private int id;
	private String name;// 文件名，自动生成
	private String type;// 文本类型
	private String alias;// 用户自定义别名
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
