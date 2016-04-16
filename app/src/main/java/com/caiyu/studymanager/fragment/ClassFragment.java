package com.caiyu.studymanager.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.entity.ClassTableEntity;
import com.caiyu.entity.ClassTimeEntity;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.entity.TeacherEntity;
import com.caiyu.studymanager.Adapter.ClassTableAdapter;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.activity.ClassTimeSetActivity;
import com.caiyu.studymanager.activity.MyApplication;
import com.caiyu.studymanager.bean.ClassBean;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.manager.ClassTableManager;
import com.caiyu.studymanager.manager.ClassTimeManager;
import com.caiyu.studymanager.manager.SubjectManager;
import com.caiyu.studymanager.manager.TeacherManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/2/26.
 */
public class ClassFragment extends BaseFragment {

    @Bind(R.id.weekdayTitleLayout)
    LinearLayout titleLayout;
    @Bind(R.id.timeLayout)
    LinearLayout timeLayout;
    @Bind(R.id.tableGv)
    GridView tableGv;

    private ClassTableManager tableManager = ClassTableManager.getInstance();
    private ClassTimeManager timeManager = ClassTimeManager.getInstance();
    private SubjectManager subjectManager = SubjectManager.getInstance();
    private TeacherManager teacherManager = TeacherManager.getInstance();

    public static int REQ_TIME = 1;
    public static int REQ_TABLE = 2;

    private int tableWidth;     //课程表宽度，包括时间、星期栏
    private int tableHeight;    //课程表高度，包括时间、星期栏

    private List<ClassTableEntity> classList;
    private int currentClassId;
    private boolean isInClass;

    private TimechangeReceiver timeTickReceiver;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_class_table;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_TABLE)   //更改课程信息，刷新
                refreshShowTable();
            else if (requestCode == REQ_TIME) { //更改上课时间，刷新
                refreshShowTime();
                if (refreshCurrentClass() == true)
                    refreshShowTable();
            }
        }
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_class_table));
        refreshShowTime();
        initTableSize();
        refreshCurrentClass();
        refreshShowTable();
        timeTickReceiver = new TimechangeReceiver();
        getActivity().registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(timeTickReceiver);
    }

    /**
     * 计算tableWidth和tableHeight
     */
    private void initTableSize() {
        //获取手机参数
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;  //屏幕宽度
        int screenHeight = metrics.heightPixels;    //屏幕高度
        float density = metrics.density; // px与dp的比值，等于dpi/160
        float scaledDensity = metrics.scaledDensity; // px与sp的比值
        //获取状态栏高度
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        //获取标题栏高度
        int titleBarHeight = (int) (44 * density);
        //获取TabHost高度
        int tabHostHeight = (int) (20 * density + 20 * scaledDensity);

        tableWidth = screenWidth;
        tableHeight = screenHeight - statusBarHeight - titleBarHeight - tabHostHeight - 10;
    }

    /**
     * 判断现在或即将上的课，即刷新currentClassId和isInClass的值
     * @return
     *      如果当前上课信息有修改，返回true，否则返回false
     */
    private boolean refreshCurrentClass() {
//        showToast("刷新当前课程信息\n原来的classId="+currentClassId+"\n原来的isInClass="+isInClass);
        int originClassId = currentClassId;
        boolean originIsInClass = isInClass;
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            currentClassId = 1;
            isInClass = false;
            return currentClassId != originClassId || isInClass != originIsInClass;
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        List<ClassTimeEntity> timeList = timeManager.getAll();
        int order;
        int curMinutes = hour * 60 + minute;
        for (ClassTimeEntity timeEntity : timeList) {
            int startMinutes = timeEntity.getStartHour() * 60 + timeEntity.getStartMinute();
            int endMinutes = timeEntity.getEndHour() * 60 + timeEntity.getEndMinute();
            if (curMinutes < startMinutes) {
                order = (int) timeEntity.getOrder();
                isInClass = false;
                currentClassId = (dayOfWeek - Calendar.MONDAY) * 5 + order;
                return currentClassId != originClassId || isInClass != originIsInClass;
            }
            else if (startMinutes <= curMinutes && curMinutes <= endMinutes) {
                order = (int) timeEntity.getOrder();
                isInClass = true;
                currentClassId = (dayOfWeek - Calendar.MONDAY) * 5 + order;
                return currentClassId != originClassId || isInClass != originIsInClass;
            }
        }
        isInClass = false;
        currentClassId = ((dayOfWeek + 1 - Calendar.MONDAY) * 5 + 1) % 25;
        return currentClassId != originClassId || isInClass != originIsInClass;
    }

    private void refreshShowTime() {
        TextView am1Tv = (TextView) timeLayout.findViewById(R.id.am1TimeTv);
        TextView am2Tv = (TextView) timeLayout.findViewById(R.id.am2TimeTv);
        TextView pm1Tv = (TextView) timeLayout.findViewById(R.id.pm1TimeTv);
        TextView pm2Tv = (TextView) timeLayout.findViewById(R.id.pm2TimeTv);
        TextView nightTv = (TextView) timeLayout.findViewById(R.id.nightTimeTv);
        List<String> timeList = timeManager.getAllByString();
        am1Tv.setText(timeList.get(0) + "\n-\n" + timeList.get(1));
        am2Tv.setText(timeList.get(2) + "\n-\n" + timeList.get(3));
        pm1Tv.setText(timeList.get(4) + "\n-\n" + timeList.get(5));
        pm2Tv.setText(timeList.get(6) + "\n-\n" + timeList.get(7));
        nightTv.setText(timeList.get(8) + "\n-\n" + timeList.get(9));
        am1Tv.setOnClickListener(new ClickTimeListener());
        am2Tv.setOnClickListener(new ClickTimeListener());
        pm1Tv.setOnClickListener(new ClickTimeListener());
        pm2Tv.setOnClickListener(new ClickTimeListener());
        nightTv.setOnClickListener(new ClickTimeListener());
    }

    private void refreshShowTable() {
//        showToast("刷新table");
        classList = tableManager.getAll();
        if (!Verifier.isEffectiveList(classList) || classList.size() != 25) {
            tableManager.fixDao();
            classList = tableManager.getAll();
            loadFromServer();
        }
//        if (tableGv.getAdapter() == null) {
            int classContentWidth = tableWidth - timeLayout.getLayoutParams().width;
            int classContentHeight = tableHeight - titleLayout.getLayoutParams().height;
            tableGv.setAdapter(new ClassTableAdapter(getActivity(), classList, currentClassId, isInClass,
                            classContentWidth, classContentHeight));
//        }
//        else {
//            ClassTableAdapter adapter = (ClassTableAdapter) tableGv.getAdapter();
//            adapter.setData(classList);
//            adapter.setCurrentClass(currentClassId, isInClass);
//            adapter.notifyDataSetChanged();
//        }
    }

    private void loadFromServer() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.GET_CLASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<ClassBean> classList = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ClassBean classBean = new ClassBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (!"".equals(jsonObject.getString(Server.RES_CLASS_NAME))) {
                                    classBean.setSubjectId(jsonObject.getInt(Server.RES_SUBJECT_ID));
                                    classBean.setClassName(jsonObject.getString(Server.RES_CLASS_NAME));
                                    classBean.setClassRoom(jsonObject.getString(Server.RES_CLASS_ROOM));
                                    classBean.setTeacher(jsonObject.getString(Server.RES_TEACHER));
                                    classBean.setStartWeek(jsonObject.getInt(Server.RES_START_WEEK));
                                    classBean.setEndWeek(jsonObject.getInt(Server.RES_END_WEEK));
                                    long teacherId = jsonObject.getInt(Server.RES_TEACHER_ID);
                                    if (!teacherManager.hasKey(teacherId)) {
                                        TeacherEntity teacherEntity = new TeacherEntity(teacherId,
                                                jsonObject.getString(Server.RES_TEACHER),
                                                jsonObject.getString(Server.RES_SEX),
                                                jsonObject.getString(Server.RES_PHONE),
                                                jsonObject.getString(Server.RES_ACADEMY),
                                                jsonObject.getString(Server.RES_POSITION));
                                        teacherManager.addData(teacherEntity);
                                    }
                                }
                                classList.add(classBean);
                            }
                            saveToLocal(classList);
                            refreshShowTable();
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
                map.put(Server.REQ_USER_ID, MyApplication.userId + "");
                return map;
            }
        };
        request.setTag("getClass");
        MyApplication.getRequestQueue().add(request);
    }

    private void saveToLocal(List<ClassBean> beanList) {
        List<ClassTableEntity> entityList = tableManager.getAll();
        for (int i=0;i<beanList.size();i++) {
            ClassTableEntity entity = entityList.get(i);
            if (Verifier.isEffectiveStr(beanList.get(i).getClassName())) {
                String className = beanList.get(i).getClassName();
                entity.setClassName(className);
                entity.setClassRoom(beanList.get(i).getClassRoom());
                entity.setTeacher(beanList.get(i).getTeacher());
                entity.setStartWeek(beanList.get(i).getStartWeek());
                entity.setEndWeek(beanList.get(i).getEndWeek());
                long subjectId = beanList.get(i).getSubjectId();
                entity.setSubjectId(subjectId);
                tableManager.update(entity);
                if (!subjectManager.hasKey(subjectId)) {
                    SubjectEntity subjectEntity = new SubjectEntity(subjectId, className);
                    subjectManager.addData(subjectEntity);
                }
            }
            else {
                entity.setClassName("");
                entity.setClassRoom("");
                entity.setTeacher("");
                entity.setStartWeek(0);
                entity.setEndWeek(0);
                entity.setSubjectId(0L);
                tableManager.update(entity);
            }
        }
    }

    private class TimechangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (refreshCurrentClass() == true) {
                refreshShowTable();
            }
        }
    }

    private class ClickTimeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ClassTimeSetActivity.class);
            startActivityForResult(intent, REQ_TIME);
        }
    }
}
