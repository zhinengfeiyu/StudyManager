package com.caiyu.studymanager.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.Adapter.StealDetailAdapter;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.DiscussBean;
import com.caiyu.studymanager.bean.StealDetailBean;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 渝 on 2016/4/16.
 */
public class StealDetailActivity extends BaseActivity {


    @Bind(R.id.lessonTv)
    TextView lessonTv;
    @Bind(R.id.stealDetailLv)
    ListView stealDetailLv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_steal_detail;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_steal_class));
        listResult();
    }

    private void listResult() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.STEAL_CLASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelDialog();
                        try {
                            List<StealDetailBean> stealDetailList = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++) {
                                StealDetailBean bean = new StealDetailBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                bean.profession = jsonObject.getString(Server.RES_PROFESSION);
                                bean.classOrder = jsonObject.getInt(Server.RES_CLASS_ORDER);
                                bean.subjectName = jsonObject.getString(Server.RES_SUBJECT_NAME);
                                bean.classRoom = jsonObject.getString(Server.RES_CLASS_ROOM);
                                bean.teacher = jsonObject.getString(Server.RES_TEACHER);
                                bean.startWeek = jsonObject.getInt(Server.RES_START_WEEK);
                                bean.endWeek = jsonObject.getInt(Server.RES_END_WEEK);
                                stealDetailList.add(bean);
                            }
                            refreshShowList(stealDetailList);
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
                map.put(Server.REQ_SUBJECT_ID,
                        getIntent().getIntExtra(ExtraKeys.SUBJECT_ID, 0) + "");
                return map;
            }
        };
        request.setTag("stealClass");
        MyApplication.getRequestQueue().add(request);
        showDialog("正在查找");
    }

    private void refreshShowList(List<StealDetailBean> data) {
        stealDetailLv.setAdapter(new StealDetailAdapter(this, data));
    }

}
