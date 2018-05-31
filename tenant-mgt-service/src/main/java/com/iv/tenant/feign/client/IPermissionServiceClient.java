package com.iv.tenant.feign.client;

import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.FunctionDto;
import com.iv.permission.api.dto.GlobalRoleCreate;
import com.iv.permission.api.dto.PageQueryDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.PersonRoleDto;
import com.iv.permission.api.service.IPermissionService;

@FeignClient(value = "permission-service", fallback = PermissionServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IPermissionServiceClient extends IPermissionService{

}

@Component
class PermissionServiceClientFallBack implements IPermissionServiceClient{

	@Override
	public Set<LocalAuthDto> getApprovalPersons(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createPermission(PermissionDto permissionDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto deletePermission(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto editPermission(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getPermissionPageList(PageQueryDto pageQueryDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createFunction(FunctionDto functionDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getPermissionList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto deleteFunction(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto editFunction(FunctionDto functionDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createGlobalRole(GlobalRoleCreate globalRoleCreate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto deleteGlobalRole(int roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto editGlobalRole(GlobalRoleCreate goleCreate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getGlobalRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto GivePersonRole(PersonRoleDto personRoleDto) {
		// TODO Auto-generated method stub
		return null;
	}


}
