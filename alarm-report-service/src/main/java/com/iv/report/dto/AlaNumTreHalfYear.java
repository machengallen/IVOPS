package com.iv.report.dto;

public class AlaNumTreHalfYear implements Comparable<AlaNumTreHalfYear>,ITrendData{

	private int year;
	private int month;
	private int week;
	private Object data;
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
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
	public int compareTo(AlaNumTreHalfYear arg0) {
		// TODO Auto-generated method stub
		if(this.getYear() > arg0.getYear()){
			return 1;
		}else if(this.getYear() == arg0.getYear()){
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
		}else{
			return -1;
		}
	}
}
