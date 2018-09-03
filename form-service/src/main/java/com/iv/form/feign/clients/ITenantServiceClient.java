package com.iv.form.feign.clients;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.UserListDto;
import com.iv.tenant.api.service.ITenantFacadeService;

@FeignClient(value = "tenant-mgt-service", fallback = TenantServiceClientFallBack.class)
public interface ITenantServiceClient extends ITenantFacadeService {

}

@Component
class TenantServiceClientFallBack implements ITenantServiceClient {



	@Override
	public ResponseDto getEnterprisesAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getEnterprisesCondition(QueryEnterReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubEnterpriseInfoDto> getSubEnterpriseAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubEnterpriseInfoDto getCurrentSubEnterpriseBack(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserListDto getCurrentSubEnterpriseUserList(HttpServletRequest request, int page, int items) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubEnterpriseInfoDto getSubEnterpriseByTenantIdBack(String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

}

