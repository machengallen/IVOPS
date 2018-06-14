package com.iv.form.util;

import com.iv.common.enumeration.CycleType;
import com.iv.dto.*;

import java.math.BigInteger;
import java.util.*;

/**
 * 报表分析工具
 * 
 * @author macheng 2017年5月8日 下午3:45:09
 */
public class CycleUtil {

	public static Set<FormNumTreDto> padCycle(CycleType cycle, List<Object[]> list, long timePoint) {
		Set<FormNumTreDto> numTrendDtos = new TreeSet<FormNumTreDto>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(timePoint);

		// 获取分组查询数据
		for (Object[] objects : list) {
			FormNumTreDto formNumTreDto = new FormNumTreDto();
			if (objects[0] instanceof BigInteger) {

				formNumTreDto.setTime(((BigInteger) objects[0]).intValue());
				formNumTreDto.setNum(((BigInteger) objects[1]).intValue());
			} else {

				formNumTreDto.setTime((Integer) objects[0]);
				formNumTreDto.setNum((Integer) objects[1]);
			}
			formNumTreDto.setData(objects[2]);
			numTrendDtos.add(formNumTreDto);
		}
		
		switch (cycle) {
		case DAY:
			
			// 补充周期内空白数据
			Integer day = calendar.get(Calendar.DAY_OF_MONTH);
			for (int i = calendar.get(Calendar.HOUR_OF_DAY), j = 0; j < 25; j++, i++) {
				FormNumTreDto formNumTreDto = new FormNumTreDto();
				formNumTreDto.setTime(day);
				formNumTreDto.setNum(i);
				formNumTreDto.setData(0l);
				numTrendDtos.add(formNumTreDto);
				//calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
			return numTrendDtos;

		case WEEK:
			
			Integer month = calendar.get(Calendar.MONTH) + 1;
			for (int i = calendar.get(Calendar.DAY_OF_MONTH), j = 0; j < 8; j++, i++) {
				FormNumTreDto formNumTreDto = new FormNumTreDto();
				if (calendar.get(Calendar.MONTH) + 1 == month) {
					formNumTreDto.setTime(month);
				} else {
					month++;
					i = 1;
					formNumTreDto.setTime(month);
				}
				formNumTreDto.setNum(i);
				formNumTreDto.setData(0l);
				numTrendDtos.add(formNumTreDto);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			return numTrendDtos;

		case TWO_WEEKS:
			
			Integer month1 = calendar.get(Calendar.MONTH) + 1;
			for (int i = calendar.get(Calendar.DAY_OF_MONTH), j = 0; j < 15; j++, i++) {
				FormNumTreDto formNumTreDtos = new FormNumTreDto();
				if (calendar.get(Calendar.MONTH) + 1 == month1) {
					formNumTreDtos.setTime(month1);
				} else {
					month1++;
					i = 1;
					formNumTreDtos.setTime(month1);
				}
				formNumTreDtos.setNum(i);
				formNumTreDtos.setData(0l);
				numTrendDtos.add(formNumTreDtos);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			return numTrendDtos;

		case MONTH:
			
			int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			Integer month2 = calendar.get(Calendar.MONTH) + 1;
			for (int i = calendar.get(Calendar.DAY_OF_MONTH), j = 0; j <= daysOfMonth; j++, i++) {
				FormNumTreDto formNumTreDto = new FormNumTreDto();
				if (calendar.get(Calendar.MONTH) + 1 == month2) {
					formNumTreDto.setTime(month2);
				} else {
					if (12 == month2) {
						month2 = 1;
					} else {
						month2++;
					}
					i = 1;
					formNumTreDto.setTime(month2);
				}
				formNumTreDto.setNum(i);
				formNumTreDto.setData(0l);
				numTrendDtos.add(formNumTreDto);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			return numTrendDtos;

		case TWO_MONTHS:
			
			for (int i = calendar.get(Calendar.WEEK_OF_YEAR), j = 0; j < 8; j++, i++) {
				FormNumTreDto formNumTreDto = new FormNumTreDto();
				int temp = calendar.get(Calendar.MONTH);
				if (temp == 0 && i > 4) {
					i = 1;
				}
				formNumTreDto.setTime(temp + 1);
				formNumTreDto.setNum(i);
				formNumTreDto.setData(0l);
				numTrendDtos.add(formNumTreDto);
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
			}
			return numTrendDtos;

		case HALF_YEAR:
			
			Integer year = calendar.get(Calendar.YEAR);
			for (int i = calendar.get(Calendar.WEEK_OF_YEAR), j = 0; j < 25; j++, i++) {
				FormNumTreDto formNumTreDto = new FormNumTreDto();
				if (year == calendar.get(Calendar.YEAR)) {
					formNumTreDto.setTime(year);
				} else {
					year++;
					i = 1;
					formNumTreDto.setTime(year);
				}
				formNumTreDto.setTime(calendar.get(Calendar.MONTH) + 1);
				formNumTreDto.setNum(i);
				formNumTreDto.setData(0l);
				numTrendDtos.add(formNumTreDto);
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
			}
			return numTrendDtos;

		case YEAR:
			
			Integer year1 = calendar.get(Calendar.YEAR);
			for (int i = calendar.get(Calendar.MONTH) + 1, j = 0; j < 12; j++, i++) {
				FormNumTreDto formNumTreDto = new FormNumTreDto();
				if (year1 == calendar.get(Calendar.YEAR)) {
					formNumTreDto.setTime(year1);
				} else {
					year1++;
					i = 1;
					formNumTreDto.setTime(year1);
				}
				formNumTreDto.setNum(i);
				formNumTreDto.setData(0l);
				numTrendDtos.add(formNumTreDto);
				calendar.add(Calendar.MONTH, 1);
			}
			return numTrendDtos;

		default:
			break;
		}
		return null;
	}


	public static String getTimeUnit(CycleType cycleType){

		switch (cycleType) {
			case DAY:
				return "时";
			case WEEK:
			case TWO_WEEKS:
			case MONTH:
				return "日";
			case TWO_MONTHS:
				return "周";

			case HALF_YEAR:
			case YEAR:
				return "月";
			default:
				break;
		}
		return null;
	}

	public static List getTimePointList(CycleType cycleType){
		ArrayList<Object> objects = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		switch (cycleType) {
			case DAY:
				for(int i=1;i<25;i++){
					objects.add(i+"时");
				}
				return objects;
			case WEEK:
				for(int i=1;i<8;i++){
					objects.add(i+"天");
				}
				return objects;
			case TWO_WEEKS:
				for(int i=1;i<16;i++){
					objects.add(i+"天");
				}
				return objects;
			case MONTH:

				calendar.add(Calendar.MONTH, -1);
				calendar.setTimeInMillis(calendar.getTimeInMillis());
				int days = calendar.get(Calendar.DAY_OF_MONTH);
				for(int i=1;i<days+1;i++){
					objects.add(i+"天");
				}
				return objects;

			case TWO_MONTHS:

				calendar.add(Calendar.MONTH, -1);
				calendar.setTimeInMillis(calendar.getTimeInMillis());
				int months = calendar.get(Calendar.WEEK_OF_MONTH);
				for(int i=1;i<months+1;i++){
					objects.add(i+"周");
				}
				return objects;

			case HALF_YEAR:
				calendar.add(Calendar.MONTH, -6);
				calendar.setTimeInMillis(calendar.getTimeInMillis());
				for(int i=1;i<calendar.get(Calendar.MONTH)+1;i++){
					objects.add(i+"月");
				}
				return objects;
			//calendar.add(Calendar.MONTH, -6);
			//return calendar.getTimeInMillis();
			case YEAR:
				calendar.add(Calendar.MONTH, -6);
				calendar.setTimeInMillis(calendar.getTimeInMillis());
				for(int i=1;i<calendar.get(Calendar.MONTH)+1;i++){
					objects.add(i+"月");
				}
				return objects;
			default:
				break;
		}
		return null;
	}

}
