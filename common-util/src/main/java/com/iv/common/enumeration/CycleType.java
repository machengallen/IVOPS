package com.iv.common.enumeration;

import java.util.Calendar;

public enum CycleType {

	DAY, WEEK, TWO_WEEKS, MONTH, TWO_MONTHS, HALF_YEAR, YEAR;
	
	public static Long getTimePoint(CycleType cycleType){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		switch (cycleType) {
		case DAY:
			calendar.set(Calendar.HOUR_OF_DAY, 0);  
			calendar.set(Calendar.MINUTE, 0);  
			calendar.set(Calendar.SECOND, 0);  
			calendar.set(Calendar.MILLISECOND, 0); 
			return calendar.getTimeInMillis();
		case WEEK:
			calendar.add(Calendar.WEEK_OF_MONTH, -1);
			return calendar.getTimeInMillis();	
		case TWO_WEEKS:
			calendar.add(Calendar.WEEK_OF_MONTH, -2);
			return calendar.getTimeInMillis();
		case MONTH:
			calendar.add(Calendar.MONTH, -1);
			return calendar.getTimeInMillis();
		case TWO_MONTHS:
			calendar.add(Calendar.MONTH, -2);
			return calendar.getTimeInMillis();
		case HALF_YEAR:
			calendar.add(Calendar.MONTH, -6);
			return calendar.getTimeInMillis();
		case YEAR:
			calendar.add(Calendar.YEAR, -1);
			return calendar.getTimeInMillis();
		default:
			break;
		}
		return null;
	}
	
	/*public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		calendar.set(Calendar.MILLISECOND, 0); 
		System.out.println(calendar.getTimeInMillis());
	}*/
	
}
