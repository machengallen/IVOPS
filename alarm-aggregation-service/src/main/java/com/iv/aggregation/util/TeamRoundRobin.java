package com.iv.aggregation.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 团队轮询工具类
 * @author macheng
 * 2018年4月8日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 2018年4月9日 适配多租户场景
 */
public class TeamRoundRobin {

	private static Map<String, HashMap<Short, Integer>> sortContainer = new HashMap<String, HashMap<Short, Integer>>();
	
	public static Integer getToUser(String tenantId, short groupId, List<Integer> groupMembers) {
		
		synchronized(sortContainer){
			
			HashMap<Short, Integer> sortGroup = null;
			if(null == sortContainer.get(tenantId)) {
				sortGroup = new HashMap<Short, Integer>();
				sortContainer.put(tenantId, sortGroup);
			}else {
				sortGroup = sortContainer.get(tenantId);
			}
			
			if(null == sortGroup.get(groupId)) {
				sortGroup.put(groupId, 0);
			}
			int sort = sortGroup.get(groupId);
			if(sort >= groupMembers.size()) {
				sort = 0;
			}
			
			int userId = groupMembers.get(sort);
			sort++;
			sortGroup.put(groupId, sort);
			sortContainer.put(tenantId, sortGroup);
			//System.out.println(tenantId +":"+groupId+":"+userId );
			return userId;
		}
	}
	

	public static void main(String[] args) throws Exception {
		List<String> tenants = Arrays.asList("t1","t2","t3","t4","t5","t6","t7","t8","t9","t10","t11","t12","t13","t14","t15");
		long start = System.currentTimeMillis();
		CountDownLatch latch=new CountDownLatch(1000);
		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					getToUser(tenants.get(new Random().nextInt(10)),(short)5,Arrays.asList(1,2,3,4,5));
					latch.countDown();
				}
			}).start();
		}
		latch.await();
		System.out.println(System.currentTimeMillis() - start);
	}
	
}
