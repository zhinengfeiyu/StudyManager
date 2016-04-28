package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
        ImageView backTv = (ImageView) findViewById(R.id.commonBackImg);
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

    /**
     * 设置标题栏的标题文字
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        TextView titleTv = (TextView) findViewById(R.id.commonTitleTv);
        if (titleTv == null) return;
        titleTv.setText(title);
    }

    /**
     * 标题栏左侧是否显示返回按钮
     * @return
     */
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

    protected void setTitleRightIcon(int resId) {
        ImageView rightImg = (ImageView) findViewById(R.id.commonRightImg);
        if (rightImg == null) return;
        rightImg.setImageResource(resId);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick();
            }
        });
    }

    protected void onRightClick() {

    }

    protected void onRightClick2() {

    }

    /**
     * 显示短时间的Toast
     * @param msg
     */
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长时间的Toast
     * @param msg
     */
    protected void showLongToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示转圈效果的进度条
     * @param hint
     *       显示进度条提示信息
     */
    protected void showDialog(String hint) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(hint);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 取消进度条
     */
    protected void cancelDialog() {
        if (progressDialog != null)
            progressDialog.cancel();
    }

    /**
     * 强制隐藏键盘
     */
    protected void hideSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
