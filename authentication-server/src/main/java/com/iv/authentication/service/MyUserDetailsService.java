package com.iv.authentication.service;

import com.iv.authentication.feign.client.ISubTenantPermissionServiceClient;
import com.iv.authentication.feign.client.IUserServiceClient;
import com.iv.authentication.pojo.LocalUser;
import com.iv.common.enumeration.YesOrNo;
import com.iv.common.util.spring.Constants;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.PermissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private ISubTenantPermissionServiceClient subTenantPermissionServiceClient;
	@Autowired
	private Md5PasswordEncoder md5PasswordEncoder;
	@Autowired
	private RedisTemplate redisTemplate;
	@Value("${iv.redis.expireTime}")
	private Long expireTime;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		
		boolean f = name.endsWith(Constants.THREE_PARTY_LOGIN);
		int lenth = Constants.THREE_PARTY_LOGIN.length();
		LocalAuthDto authDto = null;
		if(f) {
			authDto = userServiceClient.selectLocalauthInfoByName(name.substring(0,name.length()-lenth));
		} else {
			authDto = userServiceClient.selectLocalauthInfoByName(name);
		}
		if (null == authDto) {
			throw new UsernameNotFoundException("UserName " + name + " not found");
		}
		String passWord = authDto.getPassWord();
		if(f) {
			passWord = md5PasswordEncoder.encodePassword(authDto.getPassWord(), null);
		}





		LocalUser localUser = new LocalUser(name, passWord, new HashSet<GrantedAuthority>());
		localUser.setCurTenantId(authDto.getCurTenantId());
		localUser.setEmail(authDto.getEmail());
		localUser.setRealName(authDto.getRealName());
		localUser.setTel(authDto.getTel());
		localUser.setUserId(authDto.getId());



		//查询权限部分，将权限存入redis
		try {
			List<PermissionDto> personPermissions = subTenantPermissionServiceClient.getPersonPermissions(authDto.getId(), authDto.getCurTenantId());

			List<String> apis = new ArrayList<>();
			if (personPermissions!=null){
				for(PermissionDto permissionDto:personPermissions){
					apis.add(permissionDto.getUrl());
				}
			}

			int userId = authDto.getId();
			String key="authentication"+userId;
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, apis,expireTime, TimeUnit.HOURS);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
