package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.caiyu.studymanager.R;

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
        initTitle();
        afterViewCreated();
    }

    protected abstract int getContentViewId();

    protected void beforeViewCreated() {}

    protected void afterViewCreated() {}

    protected void initTitle() {
        TextView backTv = (TextView) findViewById(R.id.commonBackTv);
        if (backTv == null) return;
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView titleTv = (TextView) findViewById(R.id.commonTitleTv);
        if (titleTv == null) return;
        titleTv.setText(title);
    }

    protected void setTitleRightText(CharSequence rightText) {
        TextView rightTv = (TextView) findViewById(R.id.commonRightTv);
        if (rightTv == null) return;
        rightTv.setText(rightText);
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick();
            }
        });
    }

    protected void onRightClick() {

    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //强制隐藏键盘
    protected void hideSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
