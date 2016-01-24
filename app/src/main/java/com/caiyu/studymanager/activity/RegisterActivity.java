package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caiyu.studymanager.R;

/**
 * Created by 渝 on 2016/1/23.
 */
public class RegisterActivity extends Activity {

    private EditText userNameEt;
    private EditText passwordEt;
    private EditText repeatPasswordEt;
    private TextView userNameErrorTv;
    private TextView passwordErrorTv;
    private TextView repeatPasswordErrorTv;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userNameEt = (EditText) findViewById(R.id.userNameEt);
        passwordEt = (EditText) findViewById(R.id.passwordEt);
        repeatPasswordEt = (EditText) findViewById(R.id.repeatPasswordEt);
        userNameErrorTv = (TextView) findViewById(R.id.userNameErrorTv);
        passwordErrorTv = (TextView) findViewById(R.id.passwordErrorTv);
        repeatPasswordErrorTv = (TextView) findViewById(R.id.repeatPasswordErrorTv);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        initViews();
    }

    private void initViews() {
        userNameEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        passwordEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        repeatPasswordEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        userNameEt.setOnFocusChangeListener(new FocusChangeListener());
        passwordEt.setOnFocusChangeListener(new FocusChangeListener());
        repeatPasswordEt.setOnFocusChangeListener(new FocusChangeListener());
        registerBtn.setOnClickListener(new RegisterBtnClickListener());
        registerBtn.setEnabled(false);
    }

    /**
     * 验证用户名
     * @return
     */
    private boolean isUserNameValid() {
        return true;
    }

    /**
     * 验证密码
     * @return
     */
    private boolean isPasswordValid() {
        String input = passwordEt.getText().toString();
        if (input.length() < 6) {
            return false;
        }
        return true;
    }

    /**
     * 验证二次输入的密码
     * @return
     */
    private boolean isRepeatPasswordValid() {
        String originInput = passwordEt.getText().toString();
        String againInput = repeatPasswordEt.getText().toString();
        if (!originInput.equals(againInput))
            return false;
        return true;
    }

    /**
     * 验证所有信息
     * @return
     */
    private boolean isAllInfoValid() {
        if (!isUserNameValid())
            return false;
        if (!isPasswordValid())
            return false;
        if (!isRepeatPasswordValid())
            return false;
        return true;
    }

    private class FocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus){

            EditText editText = (EditText) v;

            if (editText == userNameEt) {
                if (hasFocus) {
                    userNameErrorTv.setVisibility(View.GONE);
                }
                else {
                    if (!isUserNameValid()) {
                        if (!editText.getText().toString().equals("")) {
                            userNameErrorTv.setVisibility(View.VISIBLE);
                        }
                        registerBtn.setEnabled(false);
                    }
                    else {
                        registerBtn.setEnabled(isAllInfoValid());
                    }
                }
            }
            else if (editText == passwordEt) {
                if (hasFocus) {
                    passwordErrorTv.setVisibility(View.GONE);
                }
                else {
                    if (!isPasswordValid()) {
                        if (!editText.getText().toString().equals("")) {
                            passwordErrorTv.setText("密码过短，至少6位");
                            passwordErrorTv.setVisibility(View.VISIBLE);
                        }
                        registerBtn.setEnabled(false);
                    }
                    else {
                        registerBtn.setEnabled(isAllInfoValid());
                    }
                }
            }
            else if (editText == repeatPasswordEt) {
                if (hasFocus) {
                    repeatPasswordErrorTv.setVisibility(View.GONE);
                }
                else {
                    if (!isRepeatPasswordValid()) {
                        if (!editText.getText().toString().equals("")) {
                            repeatPasswordErrorTv.setText("两次输入的密码不一致");
                            repeatPasswordErrorTv.setVisibility(View.VISIBLE);
                        }
                        registerBtn.setEnabled(false);
                    }
                    else {
                        registerBtn.setEnabled(isAllInfoValid());
                    }
                }
            }
        }
    }

    private class RegisterBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isAllInfoValid()) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
