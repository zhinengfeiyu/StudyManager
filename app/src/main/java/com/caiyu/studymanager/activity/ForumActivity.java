package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.studymanager.Adapter.TopicAdapter;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.TopicBean;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.manager.SubjectManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 渝 on 2016/3/7.
 */
public class ForumActivity extends BaseActivity {

    @Bind(R.id.subjectTv)
    TextView subjectTv;
    @Bind(R.id.topicList)
    ListView listView;

    private SubjectManager subjectManager = SubjectManager.getInstance();
    private SubjectEntity subjectEntity;

    private TopicAdapter adapter;

    private final int REQ_CODE_CREATE_TOPIC = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_forum;
    }

    @Override
    public void afterViewCreated() {
        long subjectId = getIntent().getLongExtra(ExtraKeys.SUBJECT_ID, 1L);
        subjectEntity = subjectManager.getDataById(subjectId);
        String subjectName = subjectEntity.getName();
        subjectTv.setText(subjectName);
        requestTopics();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE_CREATE_TOPIC && resultCode == Activity.RESULT_OK) {
            requestTopics();
        }
    }

    @OnClick(R.id.createTopicBtn)
    void click_create_topic() {
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra(ExtraKeys.SUBJECT_ID, subjectEntity.getId());
        startActivityForResult(intent, REQ_CODE_CREATE_TOPIC);
    }

    @OnItemClick(R.id.topicList)
    void click_list_item(int position) {
        Intent intent = new Intent(this, DiscussActivity.class);
        TopicBean topicBean = adapter.getData().get(position);
        intent.putExtra(ExtraKeys.TOPIC_ID, topicBean.getTopicId());
        intent.putExtra(ExtraKeys.TOPIC_TITLE, topicBean.getTitle());
        intent.putExtra(ExtraKeys.TOPIC_AUTHOR, topicBean.getAuthor());
        intent.putExtra(ExtraKeys.TOPIC_TIME, topicBean.getTime());
        startActivity(intent);
    }

    private void refreshShowList(List<TopicBean> topicList) {
        adapter = new TopicAdapter(this, topicList);
        listView.setAdapter(adapter);
    }

    private void requestTopics() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.SHOW_TOPICS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<TopicBean> topicList = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++) {
                                TopicBean topicBean = new TopicBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                topicBean.setTopicId(jsonObject.getInt(Server.RES_TOPIC_ID));
                                topicBean.setTitle(jsonObject.getString(Server.RES_TITLE));
                                topicBean.setAuthor(jsonObject.getString(Server.RES_AUTHOR));
                                topicBean.setTime(jsonObject.getLong(Server.RES_TIME));
                                topicList.add(topicBean);
                            }
                            refreshShowList(topicList);
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
                map.put(Server.REQ_SUBJECT_ID, subjectEntity.getId() + "");
                return map;
            }
        };
        request.setTag("getTopic");
        MyApplication.getRequestQueue().add(request);
    }
}
