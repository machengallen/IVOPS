package com.iv.permission.feign.fallback;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.GroupIdsDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.OpsGroupDto;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.permission.feign.client.GroupServiceClient;

@Component
public class GroupServiceClientFallBack implements GroupServiceClient {

	@Override
	public GroupEntityDto selectGroupInfo(String subTenantId, short groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupUsersInfo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupUsersPageInfo(HttpServletRequest request, GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto createGroup(OpsGroupDto opsGroupDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto deleteGroup(short groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto memberInGroup(OpsGroupDto opsGroupDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto memberOutGroup(OpsGroupDto opsGroupDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupNameMod(short groupId, String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto selectUsersInfoByTenantId(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupsUsersInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GroupEntityDto> groupsInfo(GroupIdsDto groupIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto selectTenantUserPageInfo(HttpServletRequest request, GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> selectGroupUserIds(String subTenantId, short groupId) {
		// TODO Auto-generated method stub
		return null;
	}

}
