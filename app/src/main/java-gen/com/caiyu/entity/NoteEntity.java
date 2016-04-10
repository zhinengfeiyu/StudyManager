package com.caiyu.entity;

/**
 * Created by 渝 on 2016/2/20.
 */
public class NoteEntity {

    private Long id;  //笔记id值

    private String content;  //笔记内容

    private long subjectId; //对应的科目ID

    private long lastEditTime; //上次修改时间，以毫秒为单位

    public NoteEntity() {}

    public NoteEntity(Long id, String content, long subjectId, long lastEditTime) {
        this.id = id;
        this.content = content;
        this.subjectId = subjectId;
        this.lastEditTime = lastEditTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(long lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}

