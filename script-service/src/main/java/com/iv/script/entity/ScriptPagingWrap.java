package com.iv.script.entity;

import java.util.List;

public class ScriptPagingWrap {

	private long totalCount;
	private List<ScriptEntity> entities;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<ScriptEntity> getEntities() {
		return entities;
	}
	public void setEntities(List<ScriptEntity> entities) {
		this.entities = entities;
	}
	
}
