package com.iv.enumeration;

/**
 * @author liangk
 * @create 2018年 05月 22日
 **/
public enum ClientType {

    PRINCIPAL("0","负责人"),
    NORMAL("1","员工"),
    ;

    private String code;
    private String msg;

    ClientType(String code , String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
