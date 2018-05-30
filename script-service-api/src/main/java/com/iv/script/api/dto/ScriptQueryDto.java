package com.iv.script.api.dto;

import com.iv.common.enumeration.ItemType;

public class ScriptQueryDto {

	private int id;
	private String type;// 文本类型
	private String alias;// 用户自定义别名
	private ItemType itemType;// 业务类型
	private long dateStart;
	private long dateEnd;
	private String creator;
	private int curPage;
	private int items;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public long getDateStart() {
		return dateStart;
	}
	public void setDateStart(long dateStart) {
		this.dateStart = dateStart;
	}
	public long getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(long dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
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
	
}
