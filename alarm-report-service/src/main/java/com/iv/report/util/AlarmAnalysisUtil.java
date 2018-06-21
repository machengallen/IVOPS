package com.iv.report.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iv.common.enumeration.ItemType;

@Service
public class AlarmAnalysisUtil {
	
	/**
	 * 分析告警字段：各类型故障时间（totalFaultMap）、平均恢复时间（avgRecMap）、各类型故障数（itempAlarmsMap）
	 *            各类型恢复故障数（recAlarmstMap）、各类型平均响应时间（avgResMap）
	 * @param objects
	 * @return
	 */
	public List<Map<String,Float>> getAlarmListByItemType(List<Object[]> objects){	
		List<Map<String,Float>> list = new ArrayList<Map<String,Float>>();		
		//存放告警故障时间
		Map<String,Float> totalFaultMap = new HashMap<String,Float>();
		//存放告警总响应时间
		Map<String,Float> totalResMap = new HashMap<String,Float>();
		//存放告警平均响应时间
		Map<String,Float> avgResMap = new HashMap<String,Float>();
		//存放各类型告警数量
		Map<String,Float> itempAlarmsMap = new HashMap<String,Float>();		
		//存放告警平均恢复时间
		Map<String,Float> avgRecMap = new HashMap<String,Float>();
		//存放恢复告警数
		Map<String,Float> recAlarmstMap = new HashMap<String,Float>();	
		//存放告警相应字段总数
		Map<String,Float> alarmTotalInfo = new HashMap<String,Float>();
		//存放各字段总数
		float totalfaultTimes = 0;					
		float totalmtta = 0;			
		float totalmttr = 0;		
		// 初始化监控类型
		List<String> keys = getItemTypeList();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String itemType = it.next();
			totalFaultMap.put(itemType, (float)0);
			avgRecMap.put(itemType, (float)0);
			itempAlarmsMap.put(itemType, (float)0);
			recAlarmstMap.put(itemType, (float)0);		
			totalResMap.put(itemType, (float)0);
			avgResMap.put(itemType, (float)0);
		}				
		for (Object[] objects2 : objects) {		
			String itempType = objects2[0].toString();
			BigInteger triDate0 = (java.math.BigInteger) objects2[1];
			BigInteger recDate0 = (java.math.BigInteger) objects2[2];
			BigInteger resDate0 = (java.math.BigInteger) objects2[3];
			Long triDate = triDate0.longValue();
			Long recDate = recDate0.longValue();			
			Long resDate = resDate0.longValue();
			//平均恢复时间:已恢复告警
			if((float)recDate != 0) {				
				avgRecMap.put(itempType, avgRecMap.get(itempType) + (recDate - triDate));
				recAlarmstMap.put(itempType, recAlarmstMap.get(itempType) + 1);
				totalmttr += (recDate - triDate);
			}						
			//未恢复告警，取当前时间节点为告警延续时间
			if((float)recDate == 0) {
				recDate = new Date().getTime();
			}
			//告警无响应时间，未恢复取当前时间
			if((float)resDate == 0 && (float)recDate == 0) {
				resDate = new Date().getTime();
			}
			//告警无响应时间，且已恢复，响应时间为恢复时间
			if((float)resDate == 0 && (float)recDate != 0) {
				resDate = recDate;
			}
			//总故障时间		
			totalFaultMap.put(itempType, totalFaultMap.get(itempType) + (recDate - triDate));
			totalfaultTimes += (recDate - triDate);
			//总响应时间
			totalResMap.put(itempType, totalResMap.get(itempType) + (resDate - triDate));	
			totalmtta += (resDate - triDate);
			//各类型告警数量
			itempAlarmsMap.put(itempType, itempAlarmsMap.get(itempType) + 1);			
		}	
		Iterator<String> itemTypes = keys.iterator();
		while (itemTypes.hasNext()) {
			String item = itemTypes.next().toString();
			//平均恢复时间：总故障恢复时间除以总恢复故障数
			if(recAlarmstMap.get(item) > 0) {
				avgRecMap.put(item, avgRecMap.get(item)/recAlarmstMap.get(item));
			}	
			//平均响应时间：总响应时间除以各类型告警数
			if(totalResMap.get(item) > 0) {
				avgResMap.put(item, totalResMap.get(item)/itempAlarmsMap.get(item));
			}
		}
		alarmTotalInfo.put("totalfaultTimes", totalfaultTimes);
		alarmTotalInfo.put("totalmtta", totalmtta);
		alarmTotalInfo.put("totalmttr", totalmttr);
		list.add(totalFaultMap);
		list.add(avgRecMap);	
		list.add(itempAlarmsMap);
		list.add(recAlarmstMap);
		list.add(avgResMap);
		list.add(alarmTotalInfo);			
		return list;
		
	}

	
	/**
	 * 获取各条件下告警总量：1.各类型总量 2.各类型升级总量 3.各类型压缩总量
	 */
	public Map<String,Long> countAlarmsByItemType(List<Object[]> objects) {
		Map<String,Long> map = new HashMap<>();
		// 初始化监控类型
		List<String> keys = getItemTypeList();
		Iterator<String> it = keys.iterator();
		long count = 0L;
		while (it.hasNext()) {
			map.put(it.next(), 0L);
		}			
		for (Object[] objects2 : objects) {
			BigInteger object = (BigInteger) objects2[1];
			map.put(objects2[0].toString(),object.longValue());
			count += object.longValue();
		}
		map.put("count", count);
		return map;
	}

	
	/**
	 * 获取告警类型列表
	 */
	public List<String> getItemTypeList() {
		List<String> list = new ArrayList<String>();
		for (ItemType itemType : ItemType.values()) {
			list.add(itemType.toString().toLowerCase());
		}
		return list;	
	}
	
	/**
	 * 将毫秒转换为天、时、分、秒
	 */
	public String formatTime(Long ms) {
		Integer ss = 1000;  
	    Integer mi = ss * 60;  
	    Integer hh = mi * 60;  
	    Integer dd = hh * 24;  
	  
	    Long day = ms / dd;  
	    Long hour = (ms - day * dd) / hh;  
	    Long minute = (ms - day * dd - hour * hh) / mi;  
	    Long second = (ms - day * dd - hour * hh - minute * mi) / ss;  
	   // Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;  
	      
	    StringBuffer sb = new StringBuffer();  
	    if(day > 0) {  
	        sb.append(day+"天");  
	    }  
	    if(hour > 0) {  
	        sb.append(hour+"小时");  
	    }  
	    if(minute > 0) {  
	        sb.append(minute+"分");  
	    }  
	    if(second > 0) {  
	        sb.append(second+"秒");  
	    }  
	    /*if(milliSecond > 0) {  
	        sb.append(milliSecond+"毫秒");  
	    }  */
	    if(StringUtils.isEmpty(sb)) {
	    	return "0";
	    }
	    return sb.toString();  
	}
	
	/**
	 * 获得特定时间内所有告警的平均响应时间、平均恢复时间(a.triDate,a.recDate,a.resDate)
	 * @return
	 */
	public List<Float> getAlarmInfoList(List<Object[]> objects){
		List<Float> list = new ArrayList<Float>();
		//故障总数
		long count = objects.size();
		//响应时间之和
		long mttaTimes = 0;
		//恢复时间之和
		long mttrTimes = 0;
		//恢复告警数
		long alarmsRec = 0;

		for (Object[] objects2 : objects) {
			BigInteger triDate0 = (BigInteger) objects2[0];
			BigInteger recDate0 = (BigInteger) objects2[1];		
			BigInteger resDate0 = (BigInteger) objects2[2];						
			long triDate = triDate0.longValue();
			long recDate = recDate0.longValue();
			long resDate = resDate0.longValue();
			if(resDate == 0) {
				//无响应时间，有恢复时间为恢复时间，无恢复时间为当前时间
				if(recDate != 0) {
					resDate = recDate;
				}else {
					resDate = new Date().getTime();
				}					
			}
			//计算响应时间差和
			mttaTimes += (resDate - triDate);
			//计算恢复时间和
			if(recDate != 0) {
				alarmsRec += 1;
				mttrTimes += (recDate - triDate);
			}
			
		}
		list.add((float)count);
		list.add((float)mttaTimes/count);
		list.add((float)mttrTimes/alarmsRec);			
	
		return list;
	}
}
