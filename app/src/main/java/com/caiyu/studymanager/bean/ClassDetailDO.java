package com.caiyu.studymanager.bean;

/**
 * Created by 渝 on 2016/1/24.
 */
public class ClassDetailDO {

    //课程名称
    private String className;

    //教室
    private String classRoom;

    //教师
    private String teacher;

    //起始周
    private int startWeek;

    //结束周
    private int endWeek;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

}
