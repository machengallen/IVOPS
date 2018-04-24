package com.iv.report.dto;

public class AlaNumTreWeek implements Comparable<AlaNumTreWeek>,ITrendData{

	private int month;
	private int day;
	private Object data;
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public int compareTo(AlaNumTreWeek arg0) {
		// TODO Auto-generated method stub
		if(this.getMonth() > arg0.getMonth()){
			return 1;
		}else if(this.getMonth() == arg0.getMonth()){
			if(this.getDay() > arg0.getDay()){
				return 1;
			}else if(this.getDay() < arg0.getDay()){
				return -1;
			}else{
				return 0;
			}
		}else{
			return -1;
		}
	}
	
}
