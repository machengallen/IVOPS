package com.iv.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.iv.authentication.service.MyUserDetailsService;
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
	
	@Bean
	protected Md5PasswordEncoder md5PasswordEncoder() {
		return new Md5PasswordEncoder();
	}
	
	protected void config(HttpSecurity http) throws Exception {
		/*http.formLogin().loginPage("/login").permitAll().and().authorizeRequests().antMatchers("/health", "/css/**")
		.anonymous().and().authorizeRequests().anyRequest().permitAll();*/
		//http.logout().logoutSuccessUrl("/bye").permitAll();
		http.authorizeRequests().anyRequest().permitAll();
		http.csrf().disable();
	}
	
	protected void config(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
		auth.parentAuthenticationManager(authenticationManagerBean());
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailService).passwordEncoder(md5PasswordEncoder());

		/*
		 * auth.inMemoryAuthentication() .withUser("admin") .password("123@abcd")
		 * .roles("admin");
		 */
	}

	/*@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/oauth/**");
	}*/
	
}
