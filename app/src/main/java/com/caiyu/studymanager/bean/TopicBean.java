package com.caiyu.studymanager.bean;

/**
 * Created by 渝 on 2016/2/27.
 */
public class TopicBean {

    //帖子ID
    private int topicId;

    //科目ID
    private int subjectId;

    //科目名
    private String subjectName;

    //帖子标题
    private String title;

    //帖子内容
    private String content;

    //发帖人
    private String author;

    //发帖时间
    private long time;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
