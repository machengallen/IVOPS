package com.iv.aggregation.feign.fallback;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.ITenantServiceClient;
import com.iv.common.dto.IdListDto;
import com.iv.common.response.ResponseDto;
import com.iv.tenant.api.dto.EnterpriseInfoDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.SubTenantInfoDto;
import com.iv.tenant.api.dto.TenantApproveReq;
import com.iv.tenant.api.dto.TenantInfoDto;

@Component
public class TenantServiceClientFallBack implements ITenantServiceClient {

	@Override
	public ResponseDto getEnterNameList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getTenant(QueryEnterReq req) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseDto newTenantApply(HttpServletRequest request, TenantInfoDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto taskApprove(HttpServletRequest request, TenantApproveReq dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getUserTasksRegis(HttpServletRequest request, int first, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getHistoryProcessRegis(HttpServletRequest request, int first, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto applyJoinTenant(HttpServletRequest request, String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getUserTasksJoin(HttpServletRequest request, int first, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getHistoryProcessJoin(HttpServletRequest request, int first, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto manuallyAddUser(HttpServletRequest request, IdListDto userIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto switchTenant(HttpServletRequest request, String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createSubTenant(HttpServletRequest request, SubTenantInfoDto dto) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseDto deleteSubTenant(HttpServletRequest request, int id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<SubEnterpriseInfoDto> getSubEnterpriseAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getTenantByTenantId(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
