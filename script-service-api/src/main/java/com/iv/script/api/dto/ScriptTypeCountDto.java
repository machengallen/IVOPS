package com.iv.script.api.dto;

import com.iv.common.enumeration.ItemType;

public class ScriptTypeCountDto {
	private long count;//领域脚本量：网络、系统、数据库等
	private ItemType itemType;//脚本类型
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public ItemType getItemType() {
		return itemType;
	}
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	
}
