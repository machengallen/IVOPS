package com.iv.dto;

/**
 * @author liangk
 * @create 2018年 05月 30日
 **/
public class FormNumTreDto implements Comparable<FormNumTreDto>,ITrendData{

    private int time;
    private int num;
    private Object data;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public int compareTo(FormNumTreDto o) {
        if(this.getNum() > o.getNum()){
            return 1;
        }else if(this.getNum() < o.getNum()){
            return -1;
        }else{
            return 0;
        }
    }
}