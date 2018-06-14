package com.iv.authentication.feign.client;

import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.IdsDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.SubTenantRoleDto;
import com.iv.permission.api.dto.SubTenantUserRoleDto;
import com.iv.permission.api.service.ISubTenantPermissionService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author liangk
 * @create 2018年 06月 13日
 **/
@FeignClient(value = "permission-service", fallback = SubTenantPermissionServiceClientFallBack.class)
public interface ISubTenantPermissionServiceClient extends ISubTenantPermissionService {
}


@Component
class SubTenantPermissionServiceClientFallBack implements ISubTenantPermissionServiceClient {

    @Override
    public ResponseDto createSubEnterpriseAdmin(String subTenantId, int userId) {
        return null;
    }

    @Override
    public Set<LocalAuthDto> getSubEnterpriseApprovalPersons(String subTenantId, String code) {
        return null;
    }

    @Override
    public ResponseDto getAllPermissionStatus() {
        return null;
    }

    @Override
    public ResponseDto createSubTenantRole(SubTenantRoleDto createRoleDto, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseDto getSubTenantPermissions() {
        return null;
    }

    @Override
    public ResponseDto deleteSubTenantRole(IdsDto ids) {
        return null;
    }

    @Override
    public ResponseDto editSubTenantRole(SubTenantRoleDto createRoleDto) {
        return null;
    }

    @Override
    public ResponseDto getSubTenantRoles() {
        return null;
    }

    @Override
    public ResponseDto editSubTenantPersonRole(SubTenantUserRoleDto subTenantUserRoleDto) {
        return null;
    }

    @Override
    public Set<com.iv.outer.dto.SubTenantRoleDto> selectPersonRole(int userId, String tenantId) {
        return null;
    }

    @Override
    public List<PermissionDto> getPersonPermissions(int userId, String tenantId) {
        return null;
    }
}