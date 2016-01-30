package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.manager.DaoLoader;

/**
 * Created by 渝 on 2016/1/23.
 */
public class LoginActivity extends Activity {

    private EditText userNameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private TextView registerTv;
    private TextView offlineUseTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameEt = (EditText) findViewById(R.id.userNameEt);
        passwordEt = (EditText) findViewById(R.id.passwordEt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerTv = (TextView) findViewById(R.id.registerTv);
        offlineUseTv = (TextView) findViewById(R.id.offlineUseTv);
        initViews();
    }

    private void initViews() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserInfoCorrect()) {
                    Intent intent = new Intent(LoginActivity.this, ClassTableActivity.class);
                    startActivity(intent);
                }
            }
        });
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        offlineUseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoLoader.init(getApplicationContext(), 0);
                Intent intent = new Intent(LoginActivity.this, ClassTableActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isUserInfoCorrect() {
        String userNameStr = userNameEt.getText().toString();
        String passwordStr = passwordEt.getText().toString();
        return true;
    }
}
