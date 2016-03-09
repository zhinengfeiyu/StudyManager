package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.Server;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/2/27.
 */
public class CreateTopicActivity extends BaseActivity {

    @Bind(R.id.titleEt)
    EditText titleEt;
    @Bind(R.id.contentEt)
    EditText contentEt;

    @Override
    public int getContentViewId() {
        return R.layout.activity_create_topic;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_create_topic));
    }

    @OnClick(R.id.confirmBtn)
    void click_submit() {
        String titleStr = titleEt.getText().toString();
        String contentStr = contentEt.getText().toString();
        requestCreateTopic(titleStr, contentStr);
    }

    private void requestCreateTopic(final String title, final String content) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.CREATE_TOPIC_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ("success".equals(response)) {
                            showToast("帖子创建成功");
                            setResult(Activity.RESULT_OK);
                            finish();
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
                map.put(Server.REQ_SUBJECT_ID, ""+getIntent().getLongExtra(ExtraKeys.SUBJECT_ID, 1L));
                map.put(Server.REQ_USER_ID, ""+MyApplication.userId);
                map.put(Server.REQ_TITLE, title);
                map.put(Server.REQ_CONTENT, content);
                return map;
            }
        };
        request.setTag("createTopic");
        MyApplication.getRequestQueue().add(request);
    }
}
