package com.iv.script.api.dto;

import com.iv.common.enumeration.ItemType;

public class ScriptDto {

	private int id;
	private String name;// 文件名，自动生成
	private String type;// 文本类型
	private String alias;// 用户自定义别名
	private ItemType itemType;// 业务类型
	private long creDate;
	private int creatorId;
	private String creatorName;
	private long modDate;
	private int modifierId;
	private String modifierName;
	
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
	public long getCreDate() {
		return creDate;
	}
	public void setCreDate(long creDate) {
		this.creDate = creDate;
	}
	public long getModDate() {
		return modDate;
	}
	public void setModDate(long modDate) {
		this.modDate = modDate;
	}
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public int getModifierId() {
		return modifierId;
	}
	public void setModifierId(int modifierId) {
		this.modifierId = modifierId;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	
}
