package com.iv.common.util.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import net.sf.json.JSONObject;

public class JWTUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);
	 
	public static JSONObject getJWtJson(String authorization) {
		if(null == authorization || "".equals(authorization)) {
			LOGGER.error("Authorization 不能为空");
			return null;
		}
		try {
			String token = authorization.split(" ")[1];
			Jwt jwt = JwtHelper.decode(token);			
			String jwt1 = jwt.getClaims();	
			JSONObject jsonObject = JSONObject.fromObject(jwt1);
			return jsonObject;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("解析JWT失败");
			return null;
		}		
	}
}
