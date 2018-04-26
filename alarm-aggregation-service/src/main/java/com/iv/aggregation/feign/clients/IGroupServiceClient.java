package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.common.response.ResponseDto;
import com.iv.dto.GroupUserInfosDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.service.IGroupService;

@FeignClient(value = "group-service", fallback = GroupServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IGroupServiceClient extends IGroupService {

}

@Component
class GroupServiceClientFallBack implements IGroupServiceClient {

	@Override
	public GroupEntityDto selectGroupInfo(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupUsersInfo(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupUsersPageInfo(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto groupOps(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto selectUsersInfoByTenantId(GroupQuery groupQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GroupUserInfosDto test() {
		// TODO Auto-generated method stub
		return null;
	}

	
}