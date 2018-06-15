package com.iv.common.util.spring;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
	
	/**
	 * 获取token中封装的用户属性
	 * @param attribute 用户信息属性
	 * @return
	 */
	public static String getReqValue(String attribute) {
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		if (null == requestAttributes) {
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		String token = request.getHeader("Authorization");

		if (StringUtils.isEmpty(token)) {
			return null;
		} 
		return getJWtJson(token).getString(attribute);
	}
}
