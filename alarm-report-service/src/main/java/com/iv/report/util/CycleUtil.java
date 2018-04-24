package com.iv.report.util;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import com.iv.common.enumeration.CycleType;
import com.iv.report.dto.AlaNumTreDay;
import com.iv.report.dto.AlaNumTreHalfYear;
import com.iv.report.dto.AlaNumTreMonth;
import com.iv.report.dto.AlaNumTreMonths;
import com.iv.report.dto.AlaNumTreWeek;
import com.iv.report.dto.AlaNumTreYear;
import com.iv.report.dto.ITrendData;

/**
 * 报表分析工具
 * 
 * @author macheng 2017年5月8日 下午3:45:09
 */
public class CycleUtil {

	public static Set<ITrendData> padCycle(CycleType cycle, List<Object[]> list, long timePoint) {
		Set<ITrendData> numTrendDtos = new TreeSet<ITrendData>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(timePoint);
		switch (cycle) {
		case DAY:
			// 获取分组查询数据
			for (Object[] objects : list) {
				AlaNumTreDay alaNumTreDay = new AlaNumTreDay();
				if (objects[0] instanceof BigInteger) {

					alaNumTreDay.setDay(((BigInteger) objects[0]).intValue());
					alaNumTreDay.setHour(((BigInteger) objects[1]).intValue());
				} else {

					alaNumTreDay.setDay((Integer) objects[0]);
					alaNumTreDay.setHour((Integer) objects[1]);
				}
				alaNumTreDay.setData(objects[2]);
				numTrendDtos.add(alaNumTreDay);
			}
			// 补充周期内空白数据
			Integer day = calendar.get(Calendar.DAY_OF_MONTH);
			for (int i = calendar.get(Calendar.HOUR_OF_DAY), j = 0; j < 25; j++, i++) {
				AlaNumTreDay alaNumTreDay = new AlaNumTreDay();
				alaNumTreDay.setDay(day);
				alaNumTreDay.setHour(i);
				alaNumTreDay.setData(0l);
				numTrendDtos.add(alaNumTreDay);
				//calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
			return numTrendDtos;

		case WEEK:
			for (Object[] objects : list) {
				AlaNumTreWeek alaNumTreWeek = new AlaNumTreWeek();
				if (objects[0] instanceof BigInteger) {

					alaNumTreWeek.setMonth(((BigInteger) objects[0]).intValue());
					alaNumTreWeek.setDay(((BigInteger) objects[1]).intValue());
				} else {

					alaNumTreWeek.setMonth((Integer) objects[0]);
					alaNumTreWeek.setDay((Integer) objects[1]);
				}
				alaNumTreWeek.setData(objects[2]);
				numTrendDtos.add(alaNumTreWeek);
			}
			Integer month = calendar.get(Calendar.MONTH) + 1;
			for (int i = calendar.get(Calendar.DAY_OF_MONTH), j = 0; j < 8; j++, i++) {
				AlaNumTreWeek alaNumTreWeek = new AlaNumTreWeek();
				if (calendar.get(Calendar.MONTH) + 1 == month) {
					alaNumTreWeek.setMonth(month);
				} else {
					month++;
					i = 1;
					alaNumTreWeek.setMonth(month);
				}
				alaNumTreWeek.setDay(i);
				alaNumTreWeek.setData(0l);
				numTrendDtos.add(alaNumTreWeek);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			return numTrendDtos;

		case TWO_WEEKS:
			for (Object[] objects : list) {
				AlaNumTreWeek alaNumTreWeeks = new AlaNumTreWeek();
				if (objects[0] instanceof BigInteger) {

					alaNumTreWeeks.setMonth(((BigInteger) objects[0]).intValue());
					alaNumTreWeeks.setDay(((BigInteger) objects[1]).intValue());
				} else {

					alaNumTreWeeks.setMonth((Integer) objects[0]);
					alaNumTreWeeks.setDay((Integer) objects[1]);
				}
				alaNumTreWeeks.setData(objects[2]);
				numTrendDtos.add(alaNumTreWeeks);
			}
			Integer month1 = calendar.get(Calendar.MONTH) + 1;
			for (int i = calendar.get(Calendar.DAY_OF_MONTH), j = 0; j < 15; j++, i++) {
				AlaNumTreWeek alaNumTreWeeks = new AlaNumTreWeek();
				if (calendar.get(Calendar.MONTH) + 1 == month1) {
					alaNumTreWeeks.setMonth(month1);
				} else {
					month1++;
					i = 1;
					alaNumTreWeeks.setMonth(month1);
				}
				alaNumTreWeeks.setDay(i);
				alaNumTreWeeks.setData(0l);
				numTrendDtos.add(alaNumTreWeeks);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			return numTrendDtos;

		case MONTH:
			for (Object[] objects : list) {
				AlaNumTreMonth alaNumTreMonth = new AlaNumTreMonth();
				if (objects[0] instanceof BigInteger) {

					alaNumTreMonth.setMonth(((BigInteger) objects[0]).intValue());
					alaNumTreMonth.setDay(((BigInteger) objects[1]).intValue());
				} else {

					alaNumTreMonth.setMonth((Integer) objects[0]);
					alaNumTreMonth.setDay((Integer) objects[1]);
				}
				alaNumTreMonth.setData(objects[2]);
				numTrendDtos.add(alaNumTreMonth);
			}
			int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			Integer month2 = calendar.get(Calendar.MONTH) + 1;
			for (int i = calendar.get(Calendar.DAY_OF_MONTH), j = 0; j <= daysOfMonth; j++, i++) {
				AlaNumTreMonth alaNumTreMonth = new AlaNumTreMonth();
				if (calendar.get(Calendar.MONTH) + 1 == month2) {
					alaNumTreMonth.setMonth(month2);
				} else {
					if (12 == month2) {
						month2 = 1;
					} else {
						month2++;
					}
					i = 1;
					alaNumTreMonth.setMonth(month2);
				}
				alaNumTreMonth.setDay(i);
				alaNumTreMonth.setData(0l);
				numTrendDtos.add(alaNumTreMonth);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			return numTrendDtos;

		case TWO_MONTHS:
			for (Object[] objects : list) {
				AlaNumTreMonths alaNumTreMonths = new AlaNumTreMonths();
				if (objects[0] instanceof BigInteger) {

					alaNumTreMonths.setMonth(((BigInteger) objects[0]).intValue());
					alaNumTreMonths.setWeek(((BigInteger) objects[1]).add(new BigInteger("1")).intValue());

				} else {

					alaNumTreMonths.setMonth((Integer) objects[0]);
					alaNumTreMonths.setWeek((Integer) objects[1] + 1);
				}
				alaNumTreMonths.setData(objects[2]);
				numTrendDtos.add(alaNumTreMonths);
			}
			for (int i = calendar.get(Calendar.WEEK_OF_YEAR), j = 0; j < 8; j++, i++) {
				AlaNumTreMonths alaNumTreMonths = new AlaNumTreMonths();
				int temp = calendar.get(Calendar.MONTH);
				if (temp == 0 && i > 4) {
					i = 1;
				}
				alaNumTreMonths.setMonth(temp + 1);
				alaNumTreMonths.setWeek(i);
				alaNumTreMonths.setData(0l);
				numTrendDtos.add(alaNumTreMonths);
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
			}
			return numTrendDtos;

		case HALF_YEAR:
			for (Object[] objects : list) {
				AlaNumTreHalfYear alaNumTreHalfYear = new AlaNumTreHalfYear();
				if (objects[0] instanceof BigInteger) {

					alaNumTreHalfYear.setYear(((BigInteger) objects[0]).intValue());
					alaNumTreHalfYear.setMonth(((BigInteger) objects[1]).intValue());
					alaNumTreHalfYear.setWeek(((BigInteger) objects[2]).intValue());

				} else {

					alaNumTreHalfYear.setYear((Integer) objects[0]);
					alaNumTreHalfYear.setMonth((Integer) objects[1]);
					alaNumTreHalfYear.setWeek((Integer) objects[2]);
				}
				alaNumTreHalfYear.setData(objects[3]);
				numTrendDtos.add(alaNumTreHalfYear);
			}
			Integer year = calendar.get(Calendar.YEAR);
			for (int i = calendar.get(Calendar.WEEK_OF_YEAR), j = 0; j < 25; j++, i++) {
				AlaNumTreHalfYear alaNumTreHalfYear = new AlaNumTreHalfYear();
				if (year == calendar.get(Calendar.YEAR)) {
					alaNumTreHalfYear.setYear(year);
				} else {
					year++;
					i = 1;
					alaNumTreHalfYear.setYear(year);
				}
				alaNumTreHalfYear.setMonth(calendar.get(Calendar.MONTH) + 1);
				alaNumTreHalfYear.setWeek(i);
				alaNumTreHalfYear.setData(0l);
				numTrendDtos.add(alaNumTreHalfYear);
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
			}
			return numTrendDtos;

		case YEAR:
			for (Object[] objects : list) {
				AlaNumTreYear alaNumTreYear = new AlaNumTreYear();
				if (objects[0] instanceof BigInteger) {

					alaNumTreYear.setYear(((BigInteger) objects[0]).intValue());
					alaNumTreYear.setMonth(((BigInteger) objects[1]).intValue());

				} else {

					alaNumTreYear.setYear((Integer) objects[0]);
					alaNumTreYear.setMonth((Integer) objects[1]);
				}
				alaNumTreYear.setData(objects[2]);
				numTrendDtos.add(alaNumTreYear);
			}
			Integer year1 = calendar.get(Calendar.YEAR);
			for (int i = calendar.get(Calendar.MONTH) + 1, j = 0; j < 12; j++, i++) {
				AlaNumTreYear alaNumTreYear = new AlaNumTreYear();
				if (year1 == calendar.get(Calendar.YEAR)) {
					alaNumTreYear.setYear(year1);
				} else {
					year1++;
					i = 1;
					alaNumTreYear.setYear(year1);
				}
				alaNumTreYear.setMonth(i);
				alaNumTreYear.setData(0l);
				numTrendDtos.add(alaNumTreYear);
				calendar.add(Calendar.MONTH, 1);
			}
			return numTrendDtos;

		default:
			break;
		}
		return null;
	}
}
