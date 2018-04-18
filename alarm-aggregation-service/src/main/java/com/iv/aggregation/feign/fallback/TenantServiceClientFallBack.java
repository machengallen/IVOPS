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
	public ResponseDto getSubTenant(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto newTenantApply(TenantInfoDto dto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto taskApprove(TenantApproveReq dto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getUserTasksRegis(int first, int max, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getHistoryProcessRegis(int first, int max, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto applyJoinTenant(String tenantId, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getUserTasksJoin(int first, int max, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getHistoryProcessJoin(int first, int max, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto manuallyAddUser(HttpServletRequest request, IdListDto userIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto switchTenant(String tenantId, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto deleteSubTenant(int id, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EnterpriseInfoDto> getEnterpriseAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubEnterpriseInfoDto> getSubEnterpriseAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createSubTenant(HttpServletRequest request, SubTenantInfoDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto manuallyAddUserToSubTenant(HttpServletRequest request, IdListDto userIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getSubTenant(String subTenantId) {
		// TODO Auto-generated method stub
		return null;
	}


}
