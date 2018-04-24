package com.iv.report.dto;

public class AlaNumTreMonth implements Comparable<AlaNumTreMonth>,ITrendData{

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
	public int compareTo(AlaNumTreMonth arg0) {
		if(this.getMonth() == 12 && arg0.getMonth() == 1) {
			return -1;
		}
		if(this.getMonth() == 1 && arg0.getMonth() == 12) {
			return 1;
		}
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
	
	public static void main(String[] args) {
		Integer i = new Integer(5);
		Integer i1 = new Integer(5);
		System.out.println(i == i1);
	}
	
}
