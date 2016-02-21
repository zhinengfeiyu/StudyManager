package com.caiyu.studymanager.constant;

/**
 * Created by 渝 on 2016/2/21.
 */
public class Server {

    public static final String BASE_URL = "http://192.168.0.131:8080/StudyManager";

    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String REGISTER_URL = BASE_URL + "/register";

    public static final String REQ_USER_NAME = "user_name";
    public static final String REQ_PSW = "psw";

    public static final String RES_LOGIN_RESULT = "login_result";
    public static final String RES_USER_ID = "user_id";
}
