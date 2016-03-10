package com.caiyu.studymanager.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.PrefKeys;
import com.caiyu.studymanager.constant.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/3/10.
 */
public class InputActivity extends BaseActivity {

    @Bind(R.id.hintTv)
    TextView hintTv;
    @Bind(R.id.inputEt)
    EditText inputEt;

    public static final int TYPE_REAL_NAME = 1;
    public static final int TYPE_NICK_NAME = 2;
    public static final int TYPE_SEX = 3;
    public static final int TYPE_OLD_PSW = 4;
    public static final int TYPE_NEW_PSW = 5;
    public static final int TYPE_REPEAT_PSW = 6;

    private int updateType;
    private String newPsw;

    @Override
    public int getContentViewId() {
        return R.layout.activity_input;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_setting));
        updateType = getIntent().getIntExtra(ExtraKeys.UPDATE_TYPE, 1);
        String hintText = "";
        switch (updateType) {
            case TYPE_NICK_NAME:
                hintText = "请输入新昵称";
                break;
            case TYPE_REAL_NAME:
                hintText = "请输入真实姓名";
                break;
            case TYPE_SEX:
                hintText = "请输入性别";
                inputEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case TYPE_OLD_PSW:
                hintText = "请输入旧密码";
        }
        hintTv.setText(hintText);
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm() {
        String inputRawStr = inputEt.getText().toString().trim();
        if (updateType == TYPE_OLD_PSW) {
            if (inputRawStr.equals(MyApplication.password)) {
                updateType = TYPE_NEW_PSW;
                hintTv.setText("请输入新密码");
                inputEt.setText("");
            }
            else {
                showToast("密码输入不正确");
            }
        }
        else if (updateType == TYPE_NEW_PSW) {
            if (inputRawStr.length() >= 6) {
                newPsw = inputRawStr;
                updateType = TYPE_REPEAT_PSW;
                hintTv.setText("请重复新密码");
                inputEt.setText("");
            }
            else {
                showToast("密码过短，至少6位");
            }
        }
        else {
            String inputStr = inputRawStr.trim();
            if (isValidInput(inputStr, updateType)) {
                submitInput(inputStr, updateType);
            } else {
                showToast("请输入有效值");
            }
        }
    }

    @OnClick(R.id.cancelBtn)
    void click_cancel() {
        hideSoftInput(inputEt);
        finish();
    }

    private boolean isValidInput(String inputStr, int updateType) {
        if (!Verifier.isEffectiveStr(inputStr))
            return false;
        if (updateType == TYPE_SEX) {
            if (!inputStr.equals("男") && !inputStr.equals("女"))
                return false;
        }
        else if (updateType == TYPE_REPEAT_PSW) {
            if (!inputEt.getText().toString().equals(newPsw))
                return false;
        }
        return true;
    }

    private void submitInput(final String inputStr, final int updateType) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.UPDATE_USER_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelDialog();
                        if (Server.SUCCESS.equals(response)) {
                            showToast("设置成功");
                            updatePrefs(inputStr, updateType);
                            if (updateType == TYPE_NICK_NAME)
                                MyApplication.userName = inputStr;
                            else if (updateType == TYPE_REPEAT_PSW)
                                MyApplication.password = newPsw;
                            setResult(RESULT_OK);
                            hideSoftInput(inputEt);
                            finish();
                        }
                        else if (Server.FAILURE.equals(response)) {
                            showToast("设置信息失败");
                        }
                        else {
                            showToast(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cancelDialog();
                        Log.e("Error", "网络错误");
                    }
                }){
            @Override
            public Map<String, String> getParams() {
                String key;
                switch (updateType) {
                    case TYPE_NICK_NAME:
                        key = Server.REQ_USER_NAME;
                        break;
                    case TYPE_REAL_NAME:
                        key = Server.REQ_REAL_NAME;
                        break;
                    case TYPE_SEX:
                        key = Server.REQ_SEX;
                        break;
                    case TYPE_REPEAT_PSW:
                        key = Server.REQ_PSW;
                        break;
                    default:
                        key = "";
                }
                String value;
                switch (updateType) {
                    case TYPE_REPEAT_PSW:
                        value = newPsw;
                        break;
                    default:
                        value = inputStr;
                }
                Map<String, String> map = new HashMap<>();
                map.put(Server.REQ_USER_ID, MyApplication.userId + "");
                map.put(key, value);
                return map;
            }
        };
        request.setTag("updateUserInfo");
        MyApplication.getRequestQueue().add(request);
        showDialog("加载中");
    }

    private void updatePrefs(String inputStr, int updateType) {
        SharedPreferences pref = getSharedPreferences(PrefKeys.TABLE_USER, 0);
        SharedPreferences.Editor editor = pref.edit();
        if (updateType == TYPE_NICK_NAME) {
            editor.putString(PrefKeys.LAST_LOGIN_NAME, inputStr);
        }
        else if (updateType == TYPE_REPEAT_PSW) {
            editor.putString(PrefKeys.LAST_LOGIN_PSW, newPsw);
        }
        if (updateType != TYPE_REPEAT_PSW) {
            String prefKey = String.format(PrefKeys.USER_INFO, MyApplication.userId);
            String oldJsonStr = pref.getString(prefKey, "");
            String newJsonStr = "";
            try {
                JSONObject jsonObject = Verifier.isEffectiveStr(oldJsonStr) ?
                        new JSONObject(oldJsonStr) : new JSONObject();
                switch (updateType) {
                    case TYPE_NICK_NAME:
                        jsonObject.put(Server.RES_USER_NAME, inputStr);
                        break;
                    case TYPE_REAL_NAME:
                        jsonObject.put(Server.RES_REAL_NAME, inputStr);
                        break;
                    case TYPE_SEX:
                        jsonObject.put(Server.RES_SEX, inputStr);
                        break;
                }
                newJsonStr = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editor.putString(prefKey, newJsonStr);
        }
        editor.commit();
    }
}
