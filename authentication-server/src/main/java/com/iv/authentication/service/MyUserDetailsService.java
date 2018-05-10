package com.iv.authentication.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iv.authentication.feign.client.IUserServiceClient;
import com.iv.authentication.pojo.LocalUser;
import com.iv.outer.dto.LocalAuthDto;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private IUserServiceClient userServiceClient;
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		LocalAuthDto authDto = userServiceClient.selectLocalauthInfoByName(name);
		if(null == authDto) {
			throw new UsernameNotFoundException("UserName " + name + " not found");
		}
		LocalUser localUser = new LocalUser(name, authDto.getPassWord(), new HashSet<GrantedAuthority>());
		localUser.setCurTenantId(authDto.getCurTenantId());
		localUser.setEmail(authDto.getEmail());
		localUser.setRealName(authDto.getRealName());
		localUser.setTel(authDto.getTel());
		localUser.setUserId(authDto.getId());
		return localUser;
	}

	/*private LocalUser mockUser() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("admin"));//用户所拥有的角色信息
		LocalUser user = new LocalUser("admin","123456",authorities);
		user.setCurTenantId("t801800011");
		user.setRealName("马成");
		user.setUserId(2);
		return user;
	}*/
}
