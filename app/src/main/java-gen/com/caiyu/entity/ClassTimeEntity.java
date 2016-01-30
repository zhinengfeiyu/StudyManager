package com.caiyu.entity;

/**
 * Created by 渝 on 2016/1/30.
 */
public class ClassTimeEntity {

    private long order;  //第几节课

    private int startHour;  //开始的时

    private int startMinute;//开始的分钟

    private int endHour;    //结束的时

    private int endMinute;  //结束的分钟

    public ClassTimeEntity(long order, int startHour, int startMinute, int endHour, int endMinute) {
        this.order = order;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
}
