package com.iv.script.api.dto;

import com.iv.common.enumeration.ItemType;

public class ScriptDto {

	private int id;
	private String name;// 文件名，自动生成
	private String type;// 文本类型
	private String alias;// 用户自定义别名
	private ItemType itemType;// 业务类型	
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
	public ItemType getItemType() {
		return itemType;
	}
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	
}
