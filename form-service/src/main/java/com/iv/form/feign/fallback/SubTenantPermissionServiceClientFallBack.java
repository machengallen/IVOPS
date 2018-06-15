package com.iv.form.feign.fallback;

import com.iv.common.response.ResponseDto;
import com.iv.form.feign.clients.SubTenantPermissionServiceClient;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.IdsDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.SubTenantRoleDto;
import com.iv.permission.api.dto.SubTenantUserRoleDto;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author liangk
 * @create 2018年 06月 15日
 **/
@Component
public class SubTenantPermissionServiceClientFallBack implements SubTenantPermissionServiceClient {

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

    @Override
    public Set<LocalAuthDto> approveFormPerson(String code, String tenantId, short groupId) {
        return null;
    }


}