package com.caiyu.studymanager.activity;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 渝 on 2016/2/21.
 */
public class MyApplication extends Application {

    public static RequestQueue queue;

    public static int userId;

    public static String userName;

    public static String password;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue() {
        return queue;
    }

    public static int getUserId() {
        return userId;
    }
}
