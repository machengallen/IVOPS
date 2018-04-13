package com.iv.wechat.dao;

public interface ITokenDao {
	Long getTimeOut(String key) throws RuntimeException;
	
	String getAccessToken(String key) throws RuntimeException;
	
	void saveAccessToken(String key, String value) throws RuntimeException;

}
