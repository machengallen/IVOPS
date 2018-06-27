package com.iv.form.config;

import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.JWTUtil;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class TenantIdResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null == requestAttributes) {
			String tenantId = TenantIdHolder.get();
			if(!StringUtils.isEmpty(tenantId)) {
				return ConstantContainer.FORM_SERVICE + "_" + tenantId;
			}
			return ConstantContainer.FORM_SERVICE;
		}
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		String token = request.getHeader("Authorization");

		if (StringUtils.isEmpty(token)) {
			return ConstantContainer.FORM_SERVICE;
		} else {
			return ConstantContainer.FORM_SERVICE + "_" + JWTUtil.getJWtJson(token).getString("curTenantId");
		}
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
