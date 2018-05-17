package com.iv.common.util.spring;

/**
 * 常量
 * @author 
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
	 * init.properties文件路径
	 */
	public static String INIT_PROPERTIES_PATH = "initialization.properties";
	/**
	 * redis 微信token key
	 */
	public static String ACCESS_TOKEN_KEY = "token";
	/**
	 * redis 微信token超时 key
	 */
	public static String ACCESS_TIMEOUT_KEY = "timeout";
	
	/**
	 * oauth默认内部客户端id
	 */
	public static String OAUTH2_CLIENT_ID = "client";
	/**
	 * oauth默认内部客户端密码
	 */
	public static String OAUTH2_CLIENT_SECRET = "secret";
	/**
	 * oauth默认内部客户端加密凭证
	 */
	public static String OAUTH2_CLIENT_BASIC = "Basic Y2xpZW50OnNlY3JldA==";
	
	/**
	 * 三方登录标记
	 */
	public static String THREE_PARTY_LOGIN = "_auto";
}
