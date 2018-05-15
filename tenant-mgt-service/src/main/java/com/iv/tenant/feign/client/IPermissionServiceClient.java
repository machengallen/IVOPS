package com.iv.tenant.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.service.IPermissionService;

@FeignClient(value = "permission-service", fallback = PermissionServiceClientFallBack.class)
public interface IPermissionServiceClient extends IPermissionService{

}

@Component
class PermissionServiceClientFallBack implements IPermissionServiceClient{

	@Override
	public ResponseDto createEnterpriseAdmin(int tenantId, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createSubEnterpriseAdmin(int subTenantId, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createSubEnterpriseCommonUser(int subTenantId, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalAuthDto getEnterpriseAdmin(int tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalAuthDto getSubEnterpriseAdmin(int subTenantId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
