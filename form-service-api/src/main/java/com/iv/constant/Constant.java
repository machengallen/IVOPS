package com.iv.constant;

/**
 * @author liangk
 * @create 2018年 05月 08日
 **/
public class Constant {
    //删除标志
    public static final Byte DEL_FLAG_FALSE = 0;//为删除
    public static final Byte DEL_FLAG_TRUE = 1;//已删除

    //工单状态
    public static final int FORM_STATE_INIT=0;//初始化  未接手处理
    public static final int FORM_STATE_IN_HAND=1;//处理中
    public static final int FORM_STATE_UPGRADE=2;//事件升级中
    public static final int FORM_STATE_RESOLVED=3;//已解决
    public static final int FORM_STATE_AUDIT=4;//待审核
    public static final int FORM_STATE_EVALUATE=5;//待评价
    public static final int FORM_STATE_END=6;//已结案
    public static final int FORM_STATE_BACK=7;//退回
    public static final int FORM_STATE_DEL=8;//已删除


    public static final String FORM_STATE_CONTENT_INIT="未接受处理";//初始化  未接手处理
    public static final String FORM_STATE_CONTENT_IN_HAND="处理中";//处理中
    public static final String FORM_STATE_CONTENT_UPGRADE="事件升级中";//事件升级中
    public static final String FORM_STATE_CONTENT_RESOLVED="已解决";//已解决
    public static final String FORM_STATE_CONTENT_AUDIT="待审核";//待审核
    public static final String FORM_STATE_CONTENT_EVALUATE="待评价";//待评价
    public static final String FORM_STATE_CONTENT_END="已结案";//已结案
    public static final String FORM_STATE_CONTENT_BACK="退回";//退回
    public static final String FORM_STATE_CONTENT_DEL="已删除";//已删除

    //工单来源
    public static final int FORM_SOURCE_CUSTOMER_SERVICE=0;//客服



}
