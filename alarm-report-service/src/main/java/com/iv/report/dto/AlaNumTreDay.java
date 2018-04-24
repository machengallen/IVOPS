package com.iv.report.dto;

public class AlaNumTreDay implements Comparable<AlaNumTreDay>,ITrendData{

	private int day;
	private int hour;
	private Object data;
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public int compareTo(AlaNumTreDay arg0) {
		/*if(this.getDay() > arg0.getDay()){
			return 1;
		}else if(this.getDay() == arg0.getDay()){
			if(this.getHour() > arg0.getHour()){
				return 1;
			}else if(this.getHour() < arg0.getHour()){
				return -1;
			}else{
				return 0;
			}
		}else{
			return -1;
		}*/
		
		if(this.getHour() > arg0.getHour()){
			return 1;
		}else if(this.getHour() < arg0.getHour()){
			return -1;
		}else{
			return 0;
		}
	}
	
}
