package com.iv.report.dto;

import java.util.Set;

public class AlarmCountsAndMTTARDto {
	private Set<ITrendData> iTrendCounts;
	private Set<ITrendData> iTrendMttAR;
	public Set<ITrendData> getiTrendCounts() {
		return iTrendCounts;
	}
	public void setiTrendCounts(Set<ITrendData> iTrendCounts) {
		this.iTrendCounts = iTrendCounts;
	}
	public Set<ITrendData> getiTrendMttAR() {
		return iTrendMttAR;
	}
	public void setiTrendMttAR(Set<ITrendData> iTrendMttAR) {
		this.iTrendMttAR = iTrendMttAR;
	}
	
}
