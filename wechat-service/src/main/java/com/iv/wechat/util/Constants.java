package com.iv.wechat.util;

/**
 * 常量
 * @author zhangying
 *
 */
public class Constants {
	
	/**
	 * ACCESS_TOKEN有效时间(单位：ms)
	 */
	public static int EFFECTIVE_TIME = 7000000;
	/**
	 * conf.properties文件路径
	 */
	public static String CONF_PROPERTIES_PATH = "config.properties";

	/**
	 * redis 微信token key
	 */
	public static String ACCESS_TOKEN_KEY = "token";
	/**
	 * redis 微信token超时 key
	 */
	public static String ACCESS_TIMEOUT_KEY = "timeout";
}
