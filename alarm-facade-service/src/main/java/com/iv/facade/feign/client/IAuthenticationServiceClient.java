package com.iv.facade.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "authentication-server", fallback = AuthenticationServiceClientFallBack.class)
public interface IAuthenticationServiceClient {

	@RequestMapping(value = "/uaa/oauth/token", method = RequestMethod.POST)
	Object token(@RequestHeader("Authorization") String authorization,
			@RequestParam("grant_type") String grant_type, @RequestParam("username") String username, @RequestParam("password") String passWord);
}

@Component
class AuthenticationServiceClientFallBack implements IAuthenticationServiceClient {

	@Override
	public Object token(String authorization, String grant_type, String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
