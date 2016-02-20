package com.caiyu.entity;

/**
 * Created by 渝 on 2016/2/20.
 */
public class SubjectEntity {

    private Long id;  //课程id值

    private String name;  //课程名

    public SubjectEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

