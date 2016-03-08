package com.caiyu.studymanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.activity.LoginActivity;
import com.caiyu.studymanager.activity.MyApplication;
import com.caiyu.studymanager.bean.ClassBean;
import com.caiyu.studymanager.bean.UserInfoBean;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.PrefKeys;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.widget.SettingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/2/26.
 */
public class MeFragment extends BaseFragment {

    @Bind(R.id.nameView)
    SettingView nameView;
    @Bind(R.id.studyNoView)
    SettingView studyNoView;
    @Bind(R.id.nickNameView)
    SettingView nickNameView;
    @Bind(R.id.sexView)
    SettingView sexView;
    @Bind(R.id.professionView)
    SettingView professionView;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_me;
    }

    @Override
    public void afterViewCreated() {
        SharedPreferences pref = getActivity().getSharedPreferences(PrefKeys.TABLE_USER, 0);
        String jsonInfo = pref.getString(String.format(PrefKeys.USER_INFO, MyApplication.userId), "");
        if (Verifier.isEffectiveStr(jsonInfo)) {
            UserInfoBean bean = jsonToBean(jsonInfo);
            refreshShowMyInfo(bean);
        }
        else {
            requestMyInfo();
        }
    }

    @OnClick(R.id.logoutBtn)
    void click_logout() {
        new AlertDialog.Builder(getActivity())
            .setMessage("确认退出登录？")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void logout() {
        MyApplication.userId = 0;
        MyApplication.userName = null;
        MyApplication.password = null;
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    private void requestMyInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.USER_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserInfoBean bean = jsonToBean(response);
                        SharedPreferences.Editor editor =
                                getActivity().getSharedPreferences(PrefKeys.TABLE_USER, 0).edit();
                        editor.putString(String.format(PrefKeys.USER_INFO, MyApplication.userId)
                                , response);
                        editor.commit();
                        refreshShowMyInfo(bean);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "网络错误");
                    }
                }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put(Server.REQ_USER_ID, MyApplication.userId + "");
                return map;
            }
        };
        request.setTag("getClass");
        MyApplication.getRequestQueue().add(request);
    }

    private void refreshShowMyInfo(UserInfoBean bean) {
        if (Verifier.isEffectiveStr(bean.getName()))
            nameView.setText(bean.getName());
        if (Verifier.isEffectiveStr(bean.getStudyNo()))
            studyNoView.setText(bean.getStudyNo());
        if (Verifier.isEffectiveStr(bean.getNickName()))
            nickNameView.setText(bean.getNickName());
        if (Verifier.isEffectiveStr(bean.getSex()))
            sexView.setText(bean.getSex());
        if (Verifier.isEffectiveStr(bean.getProfession()))
            professionView.setText(bean.getProfession());
    }

    private UserInfoBean jsonToBean(String jsonStr) {
        UserInfoBean bean = new UserInfoBean();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            bean.setName(jsonObject.getString(Server.RES_REAL_NAME));
            bean.setStudyNo(jsonObject.getString(Server.RES_STUDY_NO));
            bean.setNickName(jsonObject.getString(Server.RES_USER_NAME));
            bean.setSex(jsonObject.getString(Server.RES_SEX));
            bean.setProfession(jsonObject.getString(Server.RES_PROFESSION));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
