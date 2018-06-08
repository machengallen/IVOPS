package com.iv.operation.script.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPoolUtil {
	
	
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
	private ThreadPoolUtil() {
		
	}
	public static ExecutorService getInstance() {
		return EXECUTOR_SERVICE;
	}

	/*private static CompletionService<String> completionService = null;
	static {
		final BlockingQueue<Future<String>> queue = new LinkedBlockingDeque<Future<String>>(  
                10);  
		ThreadPoolUtil.completionService = new ExecutorCompletionService<String>(  
        		Executors.newCachedThreadPool(), queue);
	}
	private ThreadPoolUtil() {
		
	}
	
	public static CompletionService<String> getInstance() {
		return completionService;
	}*/
	
}
