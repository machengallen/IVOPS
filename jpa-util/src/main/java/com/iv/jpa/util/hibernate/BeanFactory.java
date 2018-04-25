package com.iv.jpa.util.hibernate;

import java.io.IOException;
import java.util.Properties;

public class BeanFactory {

	private static Properties pro = new Properties();
	
	static{
		try {
			pro.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));
			//pro.load(new FileInputStream("E:\\myworkspace\\bookManage\\src\\bean.properties"));  //ȫ·����
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object getInstance(String name){
		
		try {
			return Class.forName(pro.getProperty(name)).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
