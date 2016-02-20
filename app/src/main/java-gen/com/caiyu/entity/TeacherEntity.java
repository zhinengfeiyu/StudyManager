package com.caiyu.entity;

/**
 * Created by 渝 on 2016/2/20.
 */
public class TeacherEntity {
    private long id;    //教师id
    private String name;    //姓名
    private String sex;  //性别
    private String phone;   //手机号
    private String academy; //学院
    private String position;    //职称

    public TeacherEntity(long id, String name, String sex, String phone, String academy, String position) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.academy = academy;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
