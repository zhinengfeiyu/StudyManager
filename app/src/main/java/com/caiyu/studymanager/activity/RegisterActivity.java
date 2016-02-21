package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/1/23.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.userNameEt)
    EditText userNameEt;
    @Bind(R.id.passwordEt)
    EditText passwordEt;
    @Bind(R.id.repeatPasswordEt)
    EditText repeatPasswordEt;
    @Bind(R.id.userNameErrorTv)
    TextView userNameErrorTv;
    @Bind(R.id.passwordErrorTv)
    TextView passwordErrorTv;
    @Bind(R.id.repeatPasswordErrorTv)
    TextView repeatPasswordErrorTv;
    @Bind(R.id.registerBtn)
    Button registerBtn;

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void afterViewCreated() {
        userNameEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        passwordEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        repeatPasswordEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        userNameEt.setOnFocusChangeListener(new FocusChangeListener());
        passwordEt.setOnFocusChangeListener(new FocusChangeListener());
        repeatPasswordEt.setOnFocusChangeListener(new FocusChangeListener());
        registerBtn.setEnabled(false);
    }

    @OnClick(R.id.registerBtn)
    void click_register() {
        if (isAllInfoValid()) {
            StringRequest request = new StringRequest(Request.Method.POST, Server.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if ("success".equals(response)) {
                                showToast("注册成功");
                                finish();
                            }
                            else {
                                showToast(response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showToast("网络错误");
                        }
                    }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Server.REQ_USER_NAME, userNameEt.getText().toString());
                    map.put(Server.REQ_PSW, passwordEt.getText().toString());
                    return map;
                }
            };
            request.setTag("register");
            MyApplication.getRequestQueue().add(request);
        }
    }

    /**
     * 验证用户名
     *
     * @return
     */
    private boolean isUserNameValid() {
        return true;
    }

    /**
     * 验证密码
     *
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
     *
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
     *
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
        public void onFocusChange(View v, boolean hasFocus) {

            EditText editText = (EditText) v;

            if (editText == userNameEt) {
                if (hasFocus) {
                    userNameErrorTv.setVisibility(View.GONE);
                } else {
                    if (!isUserNameValid()) {
                        if (!editText.getText().toString().equals("")) {
                            userNameErrorTv.setVisibility(View.VISIBLE);
                        }
                        registerBtn.setEnabled(false);
                    } else {
                        registerBtn.setEnabled(isAllInfoValid());
                    }
                }
            } else if (editText == passwordEt) {
                if (hasFocus) {
                    passwordErrorTv.setVisibility(View.GONE);
                } else {
                    if (!isPasswordValid()) {
                        if (!editText.getText().toString().equals("")) {
                            passwordErrorTv.setText("密码过短，至少6位");
                            passwordErrorTv.setVisibility(View.VISIBLE);
                        }
                        registerBtn.setEnabled(false);
                    } else {
                        registerBtn.setEnabled(isAllInfoValid());
                    }
                }
            } else if (editText == repeatPasswordEt) {
                if (hasFocus) {
                    repeatPasswordErrorTv.setVisibility(View.GONE);
                } else {
                    if (!isRepeatPasswordValid()) {
                        if (!editText.getText().toString().equals("")) {
                            repeatPasswordErrorTv.setText("两次输入的密码不一致");
                            repeatPasswordErrorTv.setVisibility(View.VISIBLE);
                        }
                        registerBtn.setEnabled(false);
                    } else {
                        registerBtn.setEnabled(isAllInfoValid());
                    }
                }
            }
        }
    }

}
