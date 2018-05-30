package com.iv.script.api.dto;

import java.util.List;

public class ScriptPagingDto {

	private long totalCount;
	private List<ScriptDto> docInfoDtos;
	
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<ScriptDto> getDocInfoDtos() {
		return docInfoDtos;
	}
	public void setDocInfoDtos(List<ScriptDto> docInfoDtos) {
		this.docInfoDtos = docInfoDtos;
	}
	
}
