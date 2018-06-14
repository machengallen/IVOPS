package com.iv.zuul.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Configuration
//@EnableOAuth2Sso 开启则会重定向至认证服务
//@EnableOAuth2Client
@Order(value = 0)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
	private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

	@Autowired
	private ResourceServerTokenServices resourceServerTokenServices;
	@Value("${iv.public.uris}")
	private String[] publicUris;

	@Autowired
	private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers(publicUris)
				.permitAll().anyRequest().authenticated().and()/*.csrf()
				.requireCsrfProtectionMatcher(csrfRequestMatcher()).csrfTokenRepository(csrfTokenRepository()).and()
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)*/
				.addFilterAfter(oAuth2AuthenticationProcessingFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.logout().logoutSuccessUrl("/v1/authentication/uaa/login").permitAll();
		//http.csrf().disable();
		
		/*http
        .logout().logoutSuccessUrl("/").and()
        .authorizeRequests().antMatchers("/v1/wechat/**", "/login", "/v1/authentication/uaa/**", "/v1/alarm/aggregation/openalarm")
        .permitAll().anyRequest().authenticated().and()
        .csrf()
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable();*/
		//在过滤器之前添加自己的过滤器，查询请求的的uri用户是否有权限执行
		http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
	}

	private OAuth2AuthenticationProcessingFilter oAuth2AuthenticationProcessingFilter() {
		OAuth2AuthenticationProcessingFilter oAuth2AuthenticationProcessingFilter = new OAuth2AuthenticationProcessingFilter();
		oAuth2AuthenticationProcessingFilter.setAuthenticationManager(oauthAuthenticationManager());
		oAuth2AuthenticationProcessingFilter.setStateless(false);

		return oAuth2AuthenticationProcessingFilter;
	}

	private AuthenticationManager oauthAuthenticationManager() {
		OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
		oAuth2AuthenticationManager.setResourceId("zuul");
		oAuth2AuthenticationManager.setTokenServices(resourceServerTokenServices);
		oAuth2AuthenticationManager.setClientDetailsService(null);

		return oAuth2AuthenticationManager;
	}

	private RequestMatcher csrfRequestMatcher() {
		return new RequestMatcher() {
			// Always allow the HTTP GET method
			private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|OPTIONS|TRACE)$");

			// Disable CSFR protection on the following urls:
			private final AntPathRequestMatcher[] requestMatchers = { new AntPathRequestMatcher("/uaa/**") };

			@Override
			public boolean matches(HttpServletRequest request) {
				if (allowedMethods.matcher(request.getMethod()).matches()) {
					return false;
				}

				for (AntPathRequestMatcher matcher : requestMatchers) {
					if (matcher.matches(request)) {
						return false;
					}
				}
				return true;
			}
		};
	}

	private static Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = new Cookie(CSRF_COOKIE_NAME, csrf.getToken());
					cookie.setPath("/");
					cookie.setSecure(true);
					response.addCookie(cookie);
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private static CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName(CSRF_HEADER_NAME);
		return repository;
	}
}
