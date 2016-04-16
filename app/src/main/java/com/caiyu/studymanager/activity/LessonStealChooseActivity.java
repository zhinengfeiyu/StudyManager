package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.entity.NoteEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.ClassBean;
import com.caiyu.studymanager.bean.SubjectBean;
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
 * Created by 渝 on 2016/4/16.
 */
public class LessonStealChooseActivity extends BaseActivity {

    @Bind(R.id.lessonChooseLv)
    ListView choiceLv;

    private static final int TYPE_CLASS = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_lesson_steal_choose;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_choose_lesson));
        listResult();
    }

    @OnItemClick(R.id.lessonChooseLv)
    void click_item(int position) {
        Intent intent = new Intent(this, StealDetailActivity.class);
        intent.putExtra(ExtraKeys.SUBJECT_ID,
                ((MyAdapter) choiceLv.getAdapter()).getData().get(position).getSubjectId());
        startActivity(intent);
    }

    private void listResult() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (Verifier.isEffectiveStr(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                List<SubjectBean> subjectList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    SubjectBean subjectBean = new SubjectBean();
                                    subjectBean.setSubjectId(jsonObject.getInt(Server.RES_SUBJECT_ID));
                                    subjectBean.setSubjectName(jsonObject.getString(Server.RES_SUBJECT_NAME));
                                    subjectList.add(subjectBean);
                                }
                                choiceLv.setAdapter(new MyAdapter(subjectList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
                map.put(Server.REQ_SEARCH_TYPE, TYPE_CLASS + "");
                return map;
            }
        };
        request.setTag("search");
        MyApplication.getRequestQueue().add(request);
    }

    private class MyAdapter extends BaseAdapter {

        private List<SubjectBean> data;

        public MyAdapter(List<SubjectBean> data) {
            this.data = data;
        }

        public List<SubjectBean> getData() {
            return data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public SubjectBean getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(LessonStealChooseActivity.this).
                        inflate(android.R.layout.simple_list_item_1, null);
            }
            ViewHolder holder;
            if (convertView.getTag() == null) {
                holder = new ViewHolder();
                holder.contentTv = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            SubjectBean subjectBean = getItem(position);
            holder.contentTv.setText(subjectBean.getSubjectName());
            return convertView;
        }

        class ViewHolder {
            TextView contentTv;
        }

    }

}
