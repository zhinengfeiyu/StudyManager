package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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

    protected ProgressDialog progressDialog;

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
        if (showBack()) {
            backTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();    //保证与按下物理返回键效果相同
                }
            });
        }
        else {
            backTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView titleTv = (TextView) findViewById(R.id.commonTitleTv);
        if (titleTv == null) return;
        titleTv.setText(title);
    }

    protected boolean showBack() {
        return true;
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

    protected void setTitleRightText2(CharSequence rightText2) {
        TextView rightTv2 = (TextView) findViewById(R.id.commonRightTv2);
        if (rightTv2 == null) return;
        rightTv2.setText(rightText2);
        rightTv2.setVisibility(View.VISIBLE);
        rightTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick2();
            }
        });
    }

    protected void onRightClick() {

    }

    protected void onRightClick2() {

    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected void showDialog(String hint) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(hint);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    protected void cancelDialog() {
        if (progressDialog != null)
            progressDialog.cancel();
    }

    //强制隐藏键盘
    protected void hideSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
