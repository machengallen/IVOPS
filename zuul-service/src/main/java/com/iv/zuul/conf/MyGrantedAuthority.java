package com.iv.zuul.conf;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author liangk
 * @create 2018年 06月 05日
 **/
public class MyGrantedAuthority implements GrantedAuthority {

    private String url;


    public String getPermissionUrl() {
        return url;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.url = permissionUrl;
    }


    public MyGrantedAuthority(String url) {
        this.url = url;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
