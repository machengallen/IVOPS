package com.iv.springcloud.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.iv.springcloud.oauth2.service.MyUserDetailsService;
@Order(10)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyUserDetailsService userDetailService;
//	@Value("${iv.web.loginPage}")
//	private String loginPage;
	/**
	 * 需要配置这个支持password模式
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}
	
	/*@Bean
	protected Md5PasswordEncoder md5PasswordEncoder() {
		return new Md5PasswordEncoder();
	}*/
	
	protected void config(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").permitAll().and().authorizeRequests().antMatchers("/health", "/css/**")
		.anonymous().and().authorizeRequests().anyRequest().authenticated();
		http.csrf().disable();
	}
	
	protected void config(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
		auth.parentAuthenticationManager(authenticationManagerBean());
	}
	
}
