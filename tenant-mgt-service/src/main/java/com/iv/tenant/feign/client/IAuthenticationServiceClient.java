package com.iv.tenant.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "authentication-server", fallback = AuthenticationServiceClientFallBack.class)
public interface IAuthenticationServiceClient {

	@RequestMapping(value = "/uaa/oauth/token", method = RequestMethod.POST)
	Object refreshToken(@RequestHeader("Authorization") String authorization,
			@RequestParam("grant_type") String grant_type, @RequestParam("refresh_token") String refresh_token);
}

@Component
class AuthenticationServiceClientFallBack implements IAuthenticationServiceClient {

	@Override
	public Object refreshToken(String authorization, String grant_type, String refresh_token) {
		// TODO Auto-generated method stub
		return null;
	}

}
