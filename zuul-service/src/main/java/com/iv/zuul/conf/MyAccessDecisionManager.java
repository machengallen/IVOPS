package com.iv.zuul.conf;

import com.iv.common.util.spring.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author liangk
 * @create 2018年 06月 04日
 **/
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${iv.noAuthentication.uris}")
    private String[] noAuthenticationUris;

    @Value("${iv.public.uris}")
    private String[] publicUris;

    //思路1
    // decide 方法是判定是否拥有权限的决策方法，
    //authentication 是释CustomUserService中循环添加到 GrantedAuthority 对象中的权限信息集合.
    //object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
    //configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。

    //思路2（简单化）
    //直接从request中取用户Id，查询用户权限，去匹配url
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {


        //将用户
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        String requestURI = request.getRequestURI();
        AntPathRequestMatcher matcher;
        for (String publicUri:publicUris){
            matcher = new AntPathRequestMatcher(publicUri);
            if (matcher.matches(request)) {
                return;
            }
        }


        //不需要验证的url 正式版本
//        if (Arrays.asList(noAuthenticationUris).contains(requestURI)) {
//            return;
//        }
        // 不需要验证的url 临时版本
        List<String> strings = Arrays.asList(noAuthenticationUris);
        AntPathRequestMatcher matcherUrl;
        for (String permissionUrl:strings){
            matcherUrl = new AntPathRequestMatcher(permissionUrl);
            if (matcherUrl.matches(request)) {
                return;
            }
        }



        int userId;
        try{
            userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        }catch (Exception e){
            throw new AccessDeniedException("no right");
        }


        //admin可以全部放出
        String userName = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userName");
        if (userName.equals("admin")){
            return;
        }



         // 读取缓存
        String key="authentication"+userId;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        List<String> apis = (List<String>)operations.get(key);
        if (apis.contains(requestURI)) {
            return;
        }

        // 读取缓存   官方的匹配方法
//        String key="authentication"+userId;
//        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//        List<String> apis = (List<String>)operations.get(key);
//        AntPathRequestMatcher matcherUrl;
//        for (String permissionUrl:apis){
//            matcherUrl = new AntPathRequestMatcher(permissionUrl);
//            if (matcherUrl.matches(request)) {
//                return;
//            }
//        }



        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}