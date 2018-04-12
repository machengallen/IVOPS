package com.iv.strategy.api.constant;

public enum StrategyCycle {

	MONTH(-1), THREE_MONTHS(-3), HALF_YEAR(-6), YEAR(-12), TWO_YEARS(-24), PERMANENT(0);
	
	private int months;
	StrategyCycle(int months) {
		this.months = months;
	}
	
	public int getMonths() {
		return months;
	}
	public void setMonths(int months) {
		this.months = months;
	}
	
}
