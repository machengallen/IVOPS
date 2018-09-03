package com.iv.aggregation.feign.fallback;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.ITenantServiceClient;
import com.iv.common.response.ResponseDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.UserListDto;

@Component
public class TenantServiceClientFallBack implements ITenantServiceClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(AlarmStrategyClientFallBack.class);

	@Override
	public ResponseDto getEnterprisesAll() {
		// TODO Auto-generated method stub
		LOGGER.error("租户管理服务调用失败");
		return null;
	}

	@Override
	public ResponseDto getEnterprisesCondition(QueryEnterReq req) {
		// TODO Auto-generated method stub
		LOGGER.error("租户管理服务调用失败");
		return null;
	}

	@Override
	public List<SubEnterpriseInfoDto> getSubEnterpriseAll() {
		// TODO Auto-generated method stub
		LOGGER.error("租户管理服务调用失败");
		return null;
	}

	@Override
	public SubEnterpriseInfoDto getCurrentSubEnterpriseBack(HttpServletRequest request) {
		// TODO Auto-generated method stub
		LOGGER.error("租户管理服务调用失败");
		return null;
	}

	@Override
	public UserListDto getCurrentSubEnterpriseUserList(HttpServletRequest request, int page, int items) {
		// TODO Auto-generated method stub
		LOGGER.error("租户管理服务调用失败");
		return null;
	}

	@Override
	public SubEnterpriseInfoDto getSubEnterpriseByTenantIdBack(String tenantId) {
		// TODO Auto-generated method stub
		LOGGER.error("租户管理服务调用失败");
		return null;
	}

}
