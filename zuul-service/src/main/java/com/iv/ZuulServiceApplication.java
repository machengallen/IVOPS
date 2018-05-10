package com.iv;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringCloudApplication
@EnableZuulProxy
//@EnableOAuth2Sso
//@EnableOAuth2Client
@EnableResourceServer
public class ZuulServiceApplication  {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServiceApplication.class, args);
	}
	
	/*@Bean
	UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer(LoadBalancerInterceptor loadBalancerInterceptor) {
	
	return template -> {

	List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();

	interceptors.add(loadBalancerInterceptor);

	AccessTokenProviderChain accessTokenProviderChain = Stream

	.of(new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),

	new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider())

	.peek(tp -> tp.setInterceptors(interceptors))

	.collect(Collectors.collectingAndThen(Collectors.toList(), AccessTokenProviderChain::new));

	template.setAccessTokenProvider(accessTokenProviderChain);

	};

	}*/
}
