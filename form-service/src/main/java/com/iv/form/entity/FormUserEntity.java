package com.iv.form.entity;

import javax.persistence.*;

/**
 * @author liangk
 * @create 2018年 05月 23日
 **/
@Entity
@Table(name = "form_user")
public class FormUserEntity {

    @Id
    private int id;
    private String userName;
    private String realName;
    private String nickName;
    private String email;
    private String tel;
    private String curTenantId;
    private String headImgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCurTenantId() {
        return curTenantId;
    }

    public void setCurTenantId(String curTenantId) {
        this.curTenantId = curTenantId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
