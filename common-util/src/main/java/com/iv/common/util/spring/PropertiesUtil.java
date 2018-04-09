package com.iv.common.util.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件读取工具类
 * @author macheng
 * 2017年3月22日 下午2:45:44（调整）
 */
public class PropertiesUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Properties initialization;

	static {
		readProperties();
	}

	/**
	 * 描述：读取properties文件
	 * 
	 */
	synchronized static private void readProperties() {
		InputStream configStream = null;
		InputStream initializationStream = null;
		try {
			// 读取initialization.properties
			initialization = new Properties();
			initializationStream = PropertiesUtil.class.getClassLoader()
					.getResourceAsStream("initialization.properties");
			initialization.load(initializationStream);
		} catch (FileNotFoundException e) {
			LOGGER.error("配置文件未找到", e);
		} catch (IOException e) {
			LOGGER.error("配置文件读取失败", e);
		} finally {
			try {
				if (null != configStream) {
					configStream.close();
				}
				if (null != initializationStream) {
					initializationStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("properties文件流关闭异常", e);
			}
		}
	}

	/**
	 * 获取initialization.properties属性
	 */
	public static String getInitializationProperty(String key) {
		return initialization.getProperty(key);
	}
	
	/**
	 * 获取监控类型key
	 */
	public static Set<Object> getInitKeys(){
		return initialization.keySet();
	}

	/**
	 * 写入config.properties信息
	 */
	public static void writeProperties(String key, String value) {
		FileOutputStream fos = null;
		try {
			
			Properties config = new Properties();
			config.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties"));
			
			File file = new File(PropertiesUtil.class.getClassLoader().getResource("application.properties").getPath());
			fos = new FileOutputStream(file);
			config.setProperty(key, value);
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			config.store(fos, "『comments』Update key：" + key);
		} catch (IOException e) {
			LOGGER.error("properties写入失败", e);
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
				LOGGER.error("properties写入流关闭异常", e);
			}
		}
	}

	/**
	 * 获取所有属性，返回一个map,不常用 可以试试props.putAll(t)
	 */
	/*
	 * public Map getAllProperty(){ Map map=new HashMap(); Enumeration enu =
	 * props.propertyNames(); while (enu.hasMoreElements()) { String key =
	 * (String) enu.nextElement(); String value = props.getProperty(key);
	 * map.put(key, value); } return map; }
	 */

	/*
	 * public static void main(String[] args) { PropertiesUtil util=new
	 * PropertiesUtil(Constants.CONF_PROPERTIES_PATH);
	 * System.out.println("ACCESS_TOKEN=" + util.getProperty("ACCESS_TOKEN"));
	 * util.writeProperties("OVERDUE_ACCESS_TOKEN_TIME", "2015-03-25 16:36:37");
	 * Timestamp s = new Timestamp(); System.out.println(s.getDateTime());
	 * 
	 * }
	 */

	/**
	 * 在控制台上打印出所有属性，调试用。
	 */
	/*
	 * public void printProperties(){ props.list(System.out); }
	 */
	
	/*public static void main(String[] args) {
		Set<Object> s = initialization.keySet();
		Iterator<Object> i = s.iterator();
		while(i.hasNext()){
			System.out.println(i.next().toString());
		}
	}*/
}
