package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by 渝 on 2016/1/31.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViewCreated();
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        afterViewCreated();
    }

    protected abstract int getContentViewId();

    protected void beforeViewCreated() {}

    protected void afterViewCreated() {}

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
