package com.iv.aggregation.feign.clients;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.common.response.ResponseDto;
import com.iv.enter.dto.GroupIdsDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.OpsGroupDto;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.service.IGroupService;

@FeignClient(value = "group-service", fallback = GroupServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IGroupServiceClient extends IGroupService {

}

@Component
class GroupServiceClientFallBack implements IGroupServiceClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(GroupServiceClientFallBack.class);
	@Override
	public ResponseDto groupUsersInfo(HttpServletRequest requests) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto groupUsersPageInfo(HttpServletRequest request, GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}


	@Override
	public ResponseDto selectUsersInfoByTenantId(HttpServletRequest request) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto groupsUsersInfo() {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public List<GroupEntityDto> groupsInfo(GroupIdsDto groupIds) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public GroupEntityDto selectGroupInfo(String subTenantId, short groupId) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto deleteGroup(short groupId) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto memberInGroup(OpsGroupDto opsGroupDto) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto memberOutGroup(OpsGroupDto opsGroupDto) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto groupNameMod(short groupId, String groupName) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto createGroup(OpsGroupDto opsGroupDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public ResponseDto selectTenantUserPageInfo(HttpServletRequest request, GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	@Override
	public List<Integer> selectGroupUserIds(String subTenantId, short groupId) {
		// TODO Auto-generated method stub
		LOGGER.error("用户组服务调用失败");
		return null;
	}

	
}