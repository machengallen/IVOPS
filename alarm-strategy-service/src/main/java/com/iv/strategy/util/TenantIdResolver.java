package com.iv.strategy.util;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.JWTUtil;

@Component
public class TenantIdResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(null == requestAttributes) {
			String tenantId = TenantIdHolder.get();
			if(!StringUtils.isEmpty(tenantId)) {
				return ConstantContainer.ALARM_STRATEGY_DB + "_" + tenantId;
			}
			return ConstantContainer.ALARM_STRATEGY_DB;
		}
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		String token = request.getHeader("Authorization");
		
		if (StringUtils.isEmpty(token)) {
			if(!StringUtils.isEmpty(TenantIdHolder.get())) {
				return ConstantContainer.ALARM_STRATEGY_DB + "_" + TenantIdHolder.get();
			}
			return ConstantContainer.ALARM_STRATEGY_DB;
		} else {
			return ConstantContainer.ALARM_STRATEGY_DB + "_" + JWTUtil.getJWtJson(token).getString("curTenantId");
		}
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
	
}
