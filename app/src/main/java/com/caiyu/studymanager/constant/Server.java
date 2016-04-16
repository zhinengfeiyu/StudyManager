package com.caiyu.studymanager.constant;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 渝 on 2016/2/21.
 */
public class Server {

    public static String IP;
    public static String BASE_URL;

    public static String LOGIN_URL;
    public static String REGISTER_URL;
    public static String SHOW_TOPICS_URL;
    public static String SHOW_DISCUSS_URL;
    public static String CREATE_TOPIC_URL;
    public static String SEND_DISCUSS_URL;
    public static String SHOW_TOPIC_DETAIL_URL;
    public static String GET_CLASS_URL;
    public static String USER_INFO_URL;
    public static String UPDATE_USER_INFO_URL;
    public static String SEARCH_URL;
    public static String IMAGE_URL;
    public static String STEAL_CLASS_URL;

    public static void initURLs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PrefKeys.TABLE_USER, 0);
        IP = prefs.getString(PrefKeys.SERVER_IP, "10.1.81.88");
        BASE_URL = "http://" + IP + ":8080/StudyManager";
        LOGIN_URL = BASE_URL + "/login";
        REGISTER_URL = BASE_URL + "/register";
        SHOW_TOPICS_URL = BASE_URL + "/showTopics";
        SHOW_DISCUSS_URL = BASE_URL + "/showDiscuss";
        CREATE_TOPIC_URL = BASE_URL + "/createTopic";
        SEND_DISCUSS_URL = BASE_URL + "/sendDiscuss";
        SHOW_TOPIC_DETAIL_URL = BASE_URL + "/getTopicDetail";
        GET_CLASS_URL = BASE_URL + "/getClasses";
        USER_INFO_URL = BASE_URL + "/userInfo";
        UPDATE_USER_INFO_URL = BASE_URL + "/updateUserInfo";
        SEARCH_URL = BASE_URL + "/search";
        IMAGE_URL = BASE_URL + "/images";
        STEAL_CLASS_URL = BASE_URL + "/stealClass";
    }

    public static final String REQ_USER_NAME = "user_name";
    public static final String REQ_STUDY_NO = "study_no";
    public static final String REQ_PSW = "psw";
    public static final String REQ_REAL_NAME = "real_name";
    public static final String REQ_SEX = "sex";
    public static final String REQ_SUBJECT_ID = "subject_id";
    public static final String REQ_USER_ID = "user_id";
    public static final String REQ_TITLE = "title";
    public static final String REQ_CONTENT = "content";
    public static final String REQ_TOPIC_ID = "topic_id";
    public static final String REQ_REPLY_DISCUSS_ID = "reply_discuss_id";
    public static final String REQ_DISCUSS_ID = "discuss_id";
    public static final String REQ_SEARCH_TYPE = "search_type";
    public static final String REQ_INPUT = "input";

    public static final String RES_TEACHER_ID = "teacher_id";
    public static final String RES_LOGIN_RESULT = "login_result";
    public static final String RES_USER_ID = "user_id";
    public static final String RES_TOPIC_ID = "topic_id";
    public static final String RES_DISCUSS_ID = "discuss_id";
    public static final String RES_SUBJECT_ID = "subject_id";
    public static final String RES_AUTHOR_ID = "author_id";
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
    public static final String RES_PHONE = "phone";
    public static final String RES_ACADEMY = "academy";
    public static final String RES_POSITION = "position";
    public static final String RES_SUBJECT_NAME = "subject_name";
    public static final String RES_CLASS_ORDER = "class_order";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
}
