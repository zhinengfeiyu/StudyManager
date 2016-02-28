package com.caiyu.studymanager.bean;

/**
 * Created by 渝 on 2016/2/27.
 */
public class DiscussBean {

    private int id;     //跟帖ID

    private int topicId;    //主题ID

    private String content; //跟帖内容

    private int authorId;   //跟帖者ID

    private String author;  //跟帖者昵称

    private int replyDiscussId; //回复的帖子ID

    private String replyTo; //回复的人

    private long time;  //跟帖时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getReplyDiscussId() {
        return replyDiscussId;
    }

    public void setReplyDiscussId(int replyDiscussId) {
        this.replyDiscussId = replyDiscussId;
    }
}
