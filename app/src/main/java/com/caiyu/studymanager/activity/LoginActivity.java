package com.caiyu.studymanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.PrefKeys;
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

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_login));
        setTitleRightText("退出");
        SharedPreferences pref = getSharedPreferences(PrefKeys.TABLE_USER, 0);
        userNameEt.setText(pref.getString(PrefKeys.LAST_LOGIN_NAME, ""));
        passwordEt.setText(pref.getString(PrefKeys.LAST_LOGIN_PSW, ""));
        userNameEt.setSelection(userNameEt.getText().length());
    }

    @Override
    public boolean showBack() {
        return false;
    }

    @Override
    public void onRightClick() {
        finish();
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
                            cancelDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String loginResult = jsonObject.getString(Server.RES_LOGIN_RESULT);
                                if (!"success".equals(loginResult)) {
                                    showToast(loginResult);
                                }
                                else {
                                    showToast("登录成功");
                                    MyApplication.userId = jsonObject.getInt(Server.RES_USER_ID);
                                    MyApplication.userName = jsonObject.getString(Server.RES_USER_NAME);
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
                            cancelDialog();
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
            showDialog("登录中");
        }
    }

    @OnClick(R.id.registerTv)
    void click_register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.offlineUseTv)
    void click_offline() {
        SharedPreferences pref = getSharedPreferences(PrefKeys.TABLE_USER, 0);
        int lastLoginId = pref.getInt(PrefKeys.LAST_LOGIN_ID, 0);
        DaoLoader.init(getApplicationContext(), lastLoginId);
        Intent intent = new Intent(LoginActivity.this, TabActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.changeIpTv)
    void click_ip() {
        final View ipInputView = LayoutInflater.from(this).inflate(R.layout.view_ip_address, null);
        ((ViewGroup) findViewById(R.id.loginContentView)).addView(ipInputView);
        Button cancelBtn = (Button) ipInputView.findViewById(R.id.cancelBtn);
        Button confirmBtn = (Button) ipInputView.findViewById(R.id.confirmIpBtn);
        final EditText ipEt = (EditText) ipInputView.findViewById(R.id.ipEt);
        ipEt.setText(Server.IP);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                ((ViewGroup) findViewById(R.id.loginContentView)).removeView(ipInputView);
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = ipEt.getText().toString().trim();
                if (!input.equals(Server.IP)) {
                    SharedPreferences.Editor editor = getSharedPreferences(PrefKeys.TABLE_USER, 0).edit();
                    editor.putString(PrefKeys.SERVER_IP, input);
                    editor.commit();
                    Server.initURLs(LoginActivity.this);
                    showToast("IP地址修改成功");
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                ((ViewGroup) findViewById(R.id.loginContentView)).removeView(ipInputView);
            }
        });
    }

    private boolean isUserInfoCorrect() {
        String userNameStr = userNameEt.getText().toString();
        String passwordStr = passwordEt.getText().toString();
        return true;
    }

    private void enter() {
        DaoLoader.init(getApplicationContext(), MyApplication.userId);
        SharedPreferences pref = getSharedPreferences(PrefKeys.TABLE_USER, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PrefKeys.LAST_LOGIN_ID, MyApplication.userId);
        editor.putString(PrefKeys.LAST_LOGIN_NAME, MyApplication.userName);
        editor.putString(PrefKeys.LAST_LOGIN_PSW, MyApplication.password);
        editor.commit();
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
        finish();
    }
}
