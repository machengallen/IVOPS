package com.iv.common.util.hibernate;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.JWTUtil;

@Component
public class TenantIdResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("Authorization");

		if (StringUtils.isEmpty(token)) {
			return ConstantContainer.TENANT_SHARED_ID;
		} else {
			return JWTUtil.getJWtJson(token).getString("curTenantId");
		}
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
