package com.iv.aggregation.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.iv.common.util.spring.PropertiesUtil;


/**
 * 告警分派工具类
 * @author macheng
 * 2018年4月3日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public class DispatchUtil {
	
	private static final DispatchUtil dispatchUtil = new DispatchUtil();
	
	private DispatchUtil(){
		
	}

	public static DispatchUtil getInstance(){
		return dispatchUtil;
	}
	
	/**
	 * 根据监控类别获取监控项
	 * @param type
	 * @return
	 */
	public static ArrayList<String> getItems(String type){
		
		String itemType = PropertiesUtil.getInitializationProperty(type);
		ArrayList<String> items = new ArrayList<String>(Arrays.asList(itemType.split(";")));
		return items;
	}
	
	/**
	 * 获取微信openid
	 * @param ami
	 * @param groupId
	 * @return
	 */
	/*public Set<LocalAuth> getTargetUsers(AlarmMessageInput ami,short groupId){
		
		List<AlarmDispatchEntity> alarmDispatchEntities = dispatchDao.selectDispatchByCondition(ami, getItemType(ami.getItemKey()), groupId);
		Set<LocalAuth> users = new HashSet<LocalAuth>();
		if(!CollectionUtils.isEmpty(alarmDispatchEntities)){
			for (AlarmDispatchEntity alarmDispatchEntity : alarmDispatchEntities) {
				// 获取包含该告警级别的策略推送人
				if(alarmDispatchEntity.getSeverities().contains(Severity.values()[Byte.parseByte(ami.getSeverity())])){
					users.addAll(alarmDispatchEntity.getUsers());
				}
			}
		}
		return users;
	}*/
	
	/**
	 * 通过item获取监控类型
	 * @param itemKey
	 * @return
	 */
	public static String getItemType(String itemKey){
		
		String itemType = null;
		Iterator<Object> iterator = PropertiesUtil.getInitKeys().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			if(!"undefined".equals(key)) {
				ArrayList<String> Items = getItems(key);
				String regex = "(";
				for (String Item : Items) {
					regex = regex + Item + "|";
				}
				regex = regex.substring(0, regex.length()-1);
				regex += ")";
				Pattern p = Pattern.compile("^"+regex+"([\\s\\S]*)$"); 
				if(p.matcher(itemKey.toLowerCase()).matches()){										
					itemType = key;
					break;					
				}
			}			
		}
		if(null == itemType){ 
			itemType = "undefined";
		}
		return itemType;
	}
	
}
