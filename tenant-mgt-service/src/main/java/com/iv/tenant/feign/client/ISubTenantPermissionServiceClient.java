package com.iv.tenant.feign.client;

import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.PageQueryDto;
import com.iv.permission.api.dto.SubTenantRoleDto;
import com.iv.permission.api.dto.SubTenantUserRoleDto;
import com.iv.permission.api.service.ISubTenantPermissionService;

@FeignClient(value = "permission-service", fallback = SubTenantPermissionServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface ISubTenantPermissionServiceClient extends ISubTenantPermissionService {

}
@Component
class SubTenantPermissionServiceClientFallBack implements ISubTenantPermissionServiceClient {

	@Override
	public ResponseDto createSubEnterpriseAdmin(String subTenantId, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<LocalAuthDto> getSubEnterpriseApprovalPersons(String subTenantId, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getAllPermissionStatus(PageQueryDto pageQueryDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createSubTenantRole(SubTenantRoleDto createRoleDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getSubTenantPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto deleteSubTenantRole(int roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto editSubTenantRole(SubTenantRoleDto createRoleDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getSubTenantRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto editSubTenantPersonRole(SubTenantUserRoleDto subTenantUserRoleDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
}