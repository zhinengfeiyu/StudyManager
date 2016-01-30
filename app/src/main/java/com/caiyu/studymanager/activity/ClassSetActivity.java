package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.os.Bundle;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;

/**
 * Created by 渝 on 2016/1/30.
 */
public class ClassSetActivity extends Activity{

    private ClassTableEntity tableEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_set);
    }
}
