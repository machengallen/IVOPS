package com.iv.report.dto;

public class AlaNumTreMonths implements Comparable<AlaNumTreMonths>,ITrendData{

	private int month;
	private int week;
	private Object data;
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public int compareTo(AlaNumTreMonths arg0) {
		if(this.getMonth() - arg0.getMonth() > 9) {
			return -1;
		}
		if(this.getMonth() - arg0.getMonth() < -9) {
			return 1;
		}
		if(this.getMonth() > arg0.getMonth()){
			return 1;
		}else if(this.getMonth() == arg0.getMonth()){
			if(this.getWeek() > arg0.getWeek()){
				return 1;
			}else if(this.getWeek() < arg0.getWeek()){
				return -1;
			}else{
				return 0;
			}
		}else{
			return -1;
		}
	}
	
}
