package com.caiyu.entity;

/**
 * Created by 渝 on 2016/5/27.
 */
public class TaskEntity {
    private Long id;
    private Integer hour;
    private Integer minute;
    private String taskInfo;
    private String voicePath;

    public TaskEntity() {
    }

    public TaskEntity(Long id, Integer hour, Integer minute, String taskInfo, String voicePath) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.taskInfo = taskInfo;
        this.voicePath = voicePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }
}
