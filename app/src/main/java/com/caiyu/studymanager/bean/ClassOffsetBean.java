package com.caiyu.studymanager.bean;

/**
 * Created by 渝 on 2016/4/30.
 */
public class ClassOffsetBean {

    //当前是否在上课
    public boolean isInClass;

    //课程ID
    public int classId;

    //距下节课还有几天
    public int dayOffset;

    //距下节课还有几小时
    public int hoursOffset;

    //距下节课还有几分钟
    public int minutesOffset;
}
