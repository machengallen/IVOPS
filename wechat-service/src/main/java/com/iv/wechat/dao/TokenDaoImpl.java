package com.iv.wechat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class TokenDaoImpl implements ITokenDao {

	@Autowired
	private StringRedisTemplate template;
	@Override
	public Long getTimeOut(String key) throws RuntimeException {
		// TODO Auto-generated method stub
		ValueOperations<String, String> operations = template.opsForValue();
		String timeOut = operations.get(key);
		if(!StringUtils.isEmpty(timeOut)) {
			return Long.parseLong(timeOut);
		}else {
			return null;
		}		
	}

	@Override
	public String getAccessToken(String key) throws RuntimeException {
		// TODO Auto-generated method stub
		ValueOperations<String, String> operations = template.opsForValue();
		String token = operations.get(key);
		return token;
	}

	@Override
	public void saveAccessToken(String key, String value) throws RuntimeException {
		// TODO Auto-generated method stub
		ValueOperations<String, String> operations = template.opsForValue();
		operations.set(key, value);
	}

}
