package com.caiyu.studymanager.constant;

/**
 * Created by 渝 on 2016/2/21.
 */
public class Server {

    public static final String BASE_URL = "http://192.168.31.131:8080/StudyManager";

    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String REGISTER_URL = BASE_URL + "/register";
    public static final String SHOW_TOPICS_URL = BASE_URL + "/showTopics";
    public static final String SHOW_DISCUSS_URL = BASE_URL + "/showDiscuss";
    public static final String CREATE_TOPIC_URL = BASE_URL + "/createTopic";
    public static final String SEND_DISCUSS_URL = BASE_URL + "/sendDiscuss";
    public static final String SHOW_TOPIC_DETAIL_URL = BASE_URL + "/getTopicDetail";
    public static final String GET_CLASS_URL = BASE_URL + "/getClasses";
    public static final String USER_INFO_URL = BASE_URL + "/userInfo";

    public static final String REQ_USER_NAME = "user_name";
    public static final String REQ_PSW = "psw";
    public static final String REQ_SUBJECT_ID = "subject_id";
    public static final String REQ_USER_ID = "user_id";
    public static final String REQ_TITLE = "title";
    public static final String REQ_CONTENT = "content";
    public static final String REQ_TOPIC_ID = "topic_id";
    public static final String REQ_REPLY_DISCUSS_ID = "reply_discuss_id";
    public static final String REQ_DISCUSS_ID = "discuss_id";

    public static final String RES_LOGIN_RESULT = "login_result";
    public static final String RES_USER_ID = "user_id";
    public static final String RES_TOPIC_ID = "topic_id";
    public static final String RES_DISCUSS_ID = "discuss_id";
    public static final String RES_TITLE = "title";
    public static final String RES_AUTHOR = "author";
    public static final String RES_TIME = "time";
    public static final String RES_CONTENT = "content";
    public static final String RES_REPLY_TO = "reply_to";
    public static final String RES_CLASS_NAME = "class_name";
    public static final String RES_CLASS_ROOM = "class_room";
    public static final String RES_TEACHER = "teacher";
    public static final String RES_START_WEEK = "start_week";
    public static final String RES_END_WEEK = "end_week";
    public static final String RES_REAL_NAME = "real_name";
    public static final String RES_STUDY_NO = "study_no";
    public static final String RES_USER_NAME = "user_name";
    public static final String RES_SEX = "sex";
    public static final String RES_PROFESSION = "profession";
}
