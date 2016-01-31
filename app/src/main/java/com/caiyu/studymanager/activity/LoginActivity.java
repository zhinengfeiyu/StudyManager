package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.manager.DaoLoader;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/1/23.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.userNameEt)
    EditText userNameEt;
    @Bind(R.id.passwordEt)
    EditText passwordEt;
    @Bind(R.id.loginBtn)
    Button loginBtn;
    @Bind(R.id.registerTv)
    TextView registerTv;
    @Bind(R.id.offlineUseTv)
    TextView offlineUseTv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void afterViewCreated() {

    }

    @OnClick(R.id.loginBtn)
    void click_login() {
        if (isUserInfoCorrect()) {
            Intent intent = new Intent(LoginActivity.this, ClassTableActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.registerTv)
    void click_register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.offlineUseTv)
    void click_offline() {
        DaoLoader.init(getApplicationContext(), 0);
        Intent intent = new Intent(LoginActivity.this, ClassTableActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isUserInfoCorrect() {
        String userNameStr = userNameEt.getText().toString();
        String passwordStr = passwordEt.getText().toString();
        return true;
    }
}
