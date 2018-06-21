package com.iv.external.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.iv.common.response.ResponseDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.UserListDto;

public class TenantFacadeServiceFallBack implements TenantFacadeServiceClient {

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

}
