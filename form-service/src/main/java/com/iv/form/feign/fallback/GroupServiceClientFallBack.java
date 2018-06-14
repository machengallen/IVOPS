package com.iv.form.feign.fallback;

import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.GroupIdsDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.OpsGroupDto;
import com.iv.form.feign.clients.GroupServiceClient;
import com.iv.outer.dto.GroupEntityDto;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author liangk
 * @create 2018年 05月 23日
 **/
@Component
public class GroupServiceClientFallBack implements GroupServiceClient {
    @Override
    public GroupEntityDto selectGroupInfo(String subTenantId, short groupId) {
        return null;
    }

    @Override
    public ResponseDto groupUsersInfo(HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseDto groupUsersPageInfo(HttpServletRequest request, GroupQuery groupQuery) {
        return null;
    }

    @Override
    public ResponseDto createGroup(OpsGroupDto opsGroupDto, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseDto deleteGroup(short groupId) {
        return null;
    }

    @Override
    public ResponseDto memberInGroup(OpsGroupDto opsGroupDto) {
        return null;
    }

    @Override
    public ResponseDto memberOutGroup(OpsGroupDto opsGroupDto) {
        return null;
    }

    @Override
    public ResponseDto groupNameMod(short groupId, String groupName) {
        return null;
    }

    @Override
    public ResponseDto selectUsersInfoByTenantId(HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseDto groupsUsersInfo() {
        return null;
    }

    @Override
    public List<GroupEntityDto> groupsInfo(GroupIdsDto groupIds) {
        return null;
    }

    @Override
    public ResponseDto selectTenantUserPageInfo(HttpServletRequest request, GroupQuery groupQuery) {
        return null;
    }
}
