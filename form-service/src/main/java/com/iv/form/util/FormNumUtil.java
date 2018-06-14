package com.iv.form.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工单号生成工具
 * @author liangk
 * @create 2018年 05月 08日
 **/
public class FormNumUtil extends Thread{

    private static long orderNum = 0l;
    private static String date ;

    public static void main(String[] args) throws InterruptedException {
        System.out.println( System.currentTimeMillis());
        for (int i = 0; i < 10000; i++) {
            System.out.println(FormNumUtil.getOrderNo());
            Thread.sleep(1000);
        }
    }

    public static synchronized String getOrderNo() {
        //部署时候将换成天为单位计工单
        //String str = new SimpleDateFormat("yyMMdd").format(new Date());
        String str = new SimpleDateFormat("yyMMddmmss").format(new Date());
        if(date==null||!date.equals(str)){
            date = str;
            orderNum  = 0l;
        }
        orderNum ++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNum;;
        return orderNo+"";
    }

}
