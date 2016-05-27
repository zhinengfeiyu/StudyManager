package com.caiyu.studymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.entity.ClassTableEntity;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.entity.TeacherEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.activity.MyApplication;
import com.caiyu.studymanager.bean.DiscussBean;
import com.caiyu.studymanager.bean.StealDetailBean;
import com.caiyu.studymanager.common.Resolver;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.manager.ClassTableManager;
import com.caiyu.studymanager.manager.SubjectManager;
import com.caiyu.studymanager.manager.TeacherManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 渝 on 2016/4/16.
 */
public class StealDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<StealDetailBean> data;

    public StealDetailAdapter(Context context, List<StealDetailBean> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public StealDetailBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_steal_class, null);
        }
        ViewHolder holder;
        if (convertView.getTag() == null) {
            holder = new ViewHolder();
            holder.professionTv = (TextView) convertView.findViewById(R.id.professionTv);
            holder.classOrderTv = (TextView) convertView.findViewById(R.id.classOrderTv);
            holder.classRoomTv = (TextView) convertView.findViewById(R.id.classRoomTv);
            holder.teacherTv = (TextView) convertView.findViewById(R.id.teacherTv);
            holder.weeksTv = (TextView) convertView.findViewById(R.id.weeksTv);
            holder.addToTableBtn = (Button) convertView.findViewById(R.id.addToTableBtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StealDetailBean bean = getItem(position);
        holder.professionTv.setText("专业： " + bean.profession);
        holder.classOrderTv.setText("时间： " + Resolver.resolveClassOrder(bean.classOrder));
        holder.classRoomTv.setText("教室： " + bean.classRoom);
        holder.teacherTv.setText("教师： " + bean.teacher);
        holder.weeksTv.setText("周数： " + Resolver.resolveClassWeek(bean.startWeek, bean.endWeek));
        holder.addToTableBtn.setOnClickListener(new BtnClickListener(mContext, getItem(position)));
        return convertView;
    }

    static class ViewHolder {
        TextView professionTv;
        TextView classOrderTv;
        TextView classRoomTv;
        TextView teacherTv;
        TextView weeksTv;
        Button addToTableBtn;
    }

    static class BtnClickListener implements View.OnClickListener {

        private StealDetailBean stealDetailBean;
        private Context mContext;

        public BtnClickListener(Context mContext, StealDetailBean stealDetailBean) {
            this.mContext = mContext;
            this.stealDetailBean = stealDetailBean;
        }

        @Override
        public void onClick(View v) {
            ClassTableManager classTableManager = ClassTableManager.getInstance();
            ClassTableEntity classTableEntity = classTableManager.getDataById(stealDetailBean.classOrder);
            if (!"".equals(classTableEntity.getClassName())) {  //该时间段已存在课程
                Toast.makeText(mContext, "该时间段已存在课程！", Toast.LENGTH_SHORT).show();
            }
            else {
                requestSubjectId();
                requestTeacherInfo();
                classTableEntity.setClassName(stealDetailBean.subjectName);
                classTableEntity.setClassRoom(stealDetailBean.classRoom);
                classTableEntity.setTeacher(stealDetailBean.teacher);
                classTableEntity.setStartWeek(stealDetailBean.startWeek);
                classTableEntity.setEndWeek(stealDetailBean.endWeek);
                classTableEntity.setSubjectId(SubjectManager.getInstance().
                                    getIdByName(stealDetailBean.subjectName));
                classTableManager.update(classTableEntity);
                Toast.makeText(mContext, "成功添加到我的课程表！", Toast.LENGTH_SHORT).show();
            }
        }

        //保存新教师信息到本地数据库
        private void requestTeacherInfo() {
            final TeacherManager teacherManager = TeacherManager.getInstance();
            String teacherName = stealDetailBean.teacher;
            if (teacherManager.getDataByName(teacherName) == null) {
                StringRequest request = new StringRequest(Request.Method.POST, Server.GET_TEACHER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int id = jsonObject.getInt(Server.RES_TEACHER_ID);
                                    String sex = jsonObject.getString(Server.RES_SEX);
                                    String phone = jsonObject.getString(Server.RES_PHONE);
                                    String academy = jsonObject.getString(Server.RES_ACADEMY);
                                    String position = jsonObject.getString(Server.RES_POSITION);
                                    TeacherEntity teacherEntity =
                                            new TeacherEntity(id, stealDetailBean.teacher,
                                                        sex, phone, academy, position);
                                    teacherManager.addData(teacherEntity);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<>();
                        map.put(Server.REQ_TEACHER_NAME, stealDetailBean.teacher);
                        return map;
                    }
                };
                request.setTag("getTeacher");
                MyApplication.getRequestQueue().add(request);
            }
        }

        //保存新科目信息到本地数据库
        private void requestSubjectId() {
            final SubjectManager subjectManager = SubjectManager.getInstance();
            String subjectName = stealDetailBean.subjectName;
            if (subjectManager.getIdByName(subjectName) == -1L) {
                StringRequest request = new StringRequest(Request.Method.POST, Server.GET_SUBJECT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    long subjectId = jsonObject.getInt(Server.RES_SUBJECT_ID);
                                    SubjectEntity subjectEntity =
                                            new SubjectEntity(subjectId, stealDetailBean.subjectName);
                                    subjectManager.addData(subjectEntity);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<>();
                        map.put(Server.REQ_SUBJECT_NAME, stealDetailBean.subjectName);
                        return map;
                    }
                };
                request.setTag("getSubject");
                MyApplication.getRequestQueue().add(request);
            }
        }
    }
}
