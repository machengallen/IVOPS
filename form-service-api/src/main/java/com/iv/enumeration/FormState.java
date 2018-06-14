package com.iv.enumeration;

/**
 * @author liangk
 * @create 2018年 05月 22日
 **/
public enum  FormState {
    INIT("0","未接手处理"),
    INHAND("1","处理中"),
    UPGRADE("2","升级中"),
    RESOLVED("3","已解决"),
    AUDIT("4","审核中"),
    EVALUATE("5","待评论"),
    END("6","已结案"),
    BACK("7","已退回"),
    DEL("8","已删除"),
    ;

    private String code;
    private String msg;

    FormState(String code , String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return msg;
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
