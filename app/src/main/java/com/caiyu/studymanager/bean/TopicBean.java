package com.caiyu.studymanager.bean;

/**
 * Created by 渝 on 2016/2/27.
 */
public class TopicBean {

    //帖子ID
    private int topicId;

    //帖子标题
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
