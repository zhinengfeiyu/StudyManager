package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.manager.DaoLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    public void beforeViewCreated() {
        if (MyApplication.userId != 0) {
            enter();
            finish();
        }
    }

    @OnClick(R.id.loginBtn)
    void click_login() {
        if (isUserInfoCorrect()) {
            final String userName = userNameEt.getText().toString();
            final String password = passwordEt.getText().toString();
            StringRequest request = new StringRequest(Request.Method.POST, Server.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String loginResult = jsonObject.getString(Server.RES_LOGIN_RESULT);
                                if (!"success".equals(loginResult)) {
                                    showToast(loginResult);
                                }
                                else {
                                    showToast("登录成功");
                                    MyApplication.userId = jsonObject.getInt(Server.RES_USER_ID);
                                    MyApplication.userName = userName;
                                    MyApplication.password = password;
                                    enter();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    map.put(Server.REQ_USER_NAME, userName);
                    map.put(Server.REQ_PSW, password);
                    return map;
                }
            };
            request.setTag("login");
            MyApplication.getRequestQueue().add(request);
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

    private void enter() {
        DaoLoader.init(getApplicationContext(), MyApplication.userId);
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
        finish();
    }
}
