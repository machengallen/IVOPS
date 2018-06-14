package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

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
