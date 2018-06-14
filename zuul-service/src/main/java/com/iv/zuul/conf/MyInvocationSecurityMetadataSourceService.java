package com.iv.zuul.conf;

import com.iv.common.util.spring.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author liangk
 * @create 2018年 06月 04日
 **/
@Service
public class MyInvocationSecurityMetadataSourceService implements
        FilterInvocationSecurityMetadataSource {

//    @Autowired
//    private PermissionDao permissionDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private HashMap<String, Collection<ConfigAttribute>> map = null;

    /**
     * 加载权限表中所有权限
     */
    public void loadResourceDefine(HttpServletRequest request) {

        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        String userName = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userName");

        map = new HashMap<>();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
//        List<Permission> permissions = permissionDao.findAll();
//        for (Permission permission : permissions) {
//            array = new ArrayList<>();
//            cfg = new SecurityConfig(permission.getName());
//            //此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。
//              //此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
//            array.add(cfg);
//            //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
//            map.put(permission.getUrl(), array);
//        }
        //ValueOperations<String, String> operations = redisTemplate.opsForValue();
        //String token = operations.get(userId);

        for (int i=0 ;i<1;i++) {
            array = new ArrayList<>();
            //存入用户名，用于
            cfg = new SecurityConfig(userName);
            //此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
            array.add(cfg);
            //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
            //数据库查询将所有的Url放入集合中
            map.put("/v1/form/select/client", array);
        }


    }

    //此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        //object 中包含用户请求的request 信息
        /*HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        if (map == null) loadResourceDefine(request);
        AntPathRequestMatcher matcher;
        String resUrl;
        for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
            resUrl = iter.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if (matcher.matches(request)) {
                return map.get(resUrl);
            }
        }
        //TODO 查询通用的URl
        if(false){
            return null;
        }else{
            return new ArrayList<>();
        }*/
        Collection<ConfigAttribute> co=new ArrayList<>();
        co.add(new SecurityConfig("null"));
        return co;

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
