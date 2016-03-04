package com.caiyu.studymanager.activity;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.studymanager.Adapter.DiscussAdapter;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.DiscussBean;
import com.caiyu.studymanager.common.Verifier;
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
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 渝 on 2016/2/27.
 */
public class DiscussActivity extends BaseActivity {

    @Bind(R.id.topicAuthorTimeTv)
    TextView topicAuthorTimeTv;
    @Bind(R.id.topicTitleTv)
    TextView topicTitleTv;
    @Bind(R.id.topicContentTv)
    TextView topicContentTv;
    @Bind(R.id.discussList)
    ListView listView;
    @Bind(R.id.inputLayout)
    View inputLayout;
    @Bind(R.id.hintTv)
    TextView hintTv;
    @Bind(R.id.contentEt)
    EditText contentEt;
    @Bind(R.id.confirmInputBtn)
    Button confirmBtn;

    private DiscussAdapter adapter;

    private int topicId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_discuss;
    }

    @Override
    public void afterViewCreated() {
        topicId = getIntent().getIntExtra(ExtraKeys.TOPIC_ID, 1);
        showTopic();
        DiscussBean bean = new DiscussBean();
        bean.setTopicId(topicId);
        requestShowDiscuss(bean);
    }

    @Override
    public void onBackPressed() {
        if (inputLayout.getVisibility() == View.VISIBLE)
            inputLayout.setVisibility(View.GONE);
        else
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_forum_discuss, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendDiscuss:
                startDiscuss();
                break;
            case R.id.refresh:
                DiscussBean bean = new DiscussBean();
                bean.setTopicId(topicId);
                requestShowDiscuss(bean);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.discussList)
    void click_list_item(int position) {
        DiscussBean bean = adapter.getData().get(position);
        if (MyApplication.userName.equals(bean.getAuthor())) {
            inputLayout.setVisibility(View.VISIBLE);
            hintTv.setText("编辑回复：");
            contentEt.setText(bean.getContent());
            contentEt.setSelection(contentEt.getText().length());
            confirmBtn.setText("确认");
            confirmBtn.setOnClickListener(new ClickSendListener(bean.getId(), true));
        }
        else {
            inputLayout.setVisibility(View.VISIBLE);
            hintTv.setText(String.format("回复%1$s：", bean.getAuthor()));
            contentEt.setText("");
            confirmBtn.setOnClickListener(new ClickSendListener(bean.getId()));
        }
    }

    private void startDiscuss() {
        inputLayout.setVisibility(View.VISIBLE);
        contentEt.setText("");
        hintTv.setText("跟帖：");
        confirmBtn.setOnClickListener(new ClickSendListener());
    }

    private void showTopic() {
        topicAuthorTimeTv.setText(String.format("发帖人：%1$s   %2$s",
                getIntent().getStringExtra(ExtraKeys.TOPIC_AUTHOR),
                "1月1日"));
        topicTitleTv.setText(getIntent().getStringExtra(ExtraKeys.TOPIC_TITLE));
        requestTopicDetail(topicId);
    }

    private void refreshShowList(List<DiscussBean> discussList) {
        adapter = new DiscussAdapter(this, discussList);
        listView.setAdapter(adapter);
    }

    private void requestTopicDetail(final int topicId) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.SHOW_TOPIC_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        topicContentTv.setText(response);
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
                map.put(Server.REQ_TOPIC_ID, ""+topicId);
                return map;
            }
        };
        request.setTag("showTopicDetail");
        MyApplication.getRequestQueue().add(request);
    }

    private void requestShowDiscuss(final DiscussBean discussBean) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.SHOW_DISCUSS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<DiscussBean> discussList = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++) {
                                DiscussBean bean = new DiscussBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                bean.setId(jsonObject.getInt(Server.RES_DISCUSS_ID));
                                bean.setContent(jsonObject.getString(Server.RES_CONTENT));
                                bean.setAuthor(jsonObject.getString(Server.RES_AUTHOR));
                                bean.setReplyTo(jsonObject.getString(Server.RES_REPLY_TO));
                                bean.setTime(jsonObject.getLong(Server.RES_TIME));
                                discussList.add(bean);
                            }

                            refreshShowList(discussList);
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
                map.put(Server.REQ_TOPIC_ID, ""+discussBean.getTopicId());
                return map;
            }
        };
        request.setTag("showDiscuss");
        MyApplication.getRequestQueue().add(request);
    }

    private void requestSendDiscuss(final DiscussBean discussBean) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.SEND_DISCUSS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ("success".equals(response)) {
                            showToast("发表成功");
                            DiscussBean bean = new DiscussBean();
                            bean.setTopicId(getIntent().getIntExtra(ExtraKeys.TOPIC_ID, 1));
                            requestShowDiscuss(bean);
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
                map.put(Server.REQ_CONTENT, discussBean.getContent());
                if (discussBean.getId() == 0) {
                    map.put(Server.REQ_TOPIC_ID, "" + discussBean.getTopicId());
                    map.put(Server.REQ_USER_ID, "" + discussBean.getAuthorId());
                    map.put(Server.REQ_REPLY_DISCUSS_ID, "" + discussBean.getReplyDiscussId());
                }
                else {
                    map.put(Server.REQ_DISCUSS_ID, "" + discussBean.getId());
                }
                return map;
            }
        };
        request.setTag("sendDiscuss");
        MyApplication.getRequestQueue().add(request);
    }

    private class ClickSendListener implements View.OnClickListener {
        private int discussId = -1;
        private int replyId = -1;
        public ClickSendListener() {

        }
        public ClickSendListener(int discussId, boolean update) {
            this.discussId = discussId;
        }
        public ClickSendListener(int replyId) {
            this.replyId = replyId;
        }

        @Override
        public void onClick(View v) {
            String input = contentEt.getText().toString();
            if (!Verifier.isEffectiveStr(input)) {
                showToast("内容不能为空");
                return;
            }
            DiscussBean bean = new DiscussBean();
            bean.setContent(input);
            if (discussId != -1) {
                bean.setId(discussId);
            }
            else {
                bean.setAuthorId(MyApplication.userId);
                bean.setTopicId(getIntent().getIntExtra(ExtraKeys.TOPIC_ID, 1));
                bean.setReplyDiscussId(replyId);
            }
            requestSendDiscuss(bean);
            inputLayout.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
        }
    }

}
