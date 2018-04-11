package com.iv.common.enumeration;

/**
 * 告警等级
 * @author zhangying
 * 2018年4月11日
 * aggregation-1.4.0-SNAPSHOT
 */
public enum Severity {


	未分类, 信息, 警告, 一般严重, 严重, 灾难;
	
	public static String getColor(int value) {
        switch (value) {
        case 0:
            return "#173177";
        case 1:
            return "#32CD32";
        case 2:
            return "#EEEE00";
        case 3:
        	return "#EEAD0E";
        case 4:
        	return "#FF0000";
        case 5:
        	return "#EE0000";
        default:
            return "#173177";
        }
    }


}
