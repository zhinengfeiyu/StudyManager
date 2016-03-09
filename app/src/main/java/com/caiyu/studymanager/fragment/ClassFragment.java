package com.caiyu.studymanager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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

    private int tableWidth;
    private int tableHeight;

    private List<ClassTableEntity> classList;
    private int currentClassId;
    private boolean isInClass;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_class_table;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_TABLE)
                refreshShowTable();
            else if (requestCode == REQ_TIME)
                refreshShowTime();
        }
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_class_table));
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        tableWidth = screenWidth;
        int screenHeight = metrics.heightPixels;
        //获取状态栏高度
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        tableHeight = screenHeight - statusBarHeight;
        initWeekdayTitle();
        refreshShowTime();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCurrentClass();
        refreshShowTable();
    }

    @OnClick(R.id.timeLayout)
    void click_class_time() {
        Intent intent = new Intent(getActivity(), ClassTimeSetActivity.class);
        startActivityForResult(intent, REQ_TIME);
    }

    private void refreshCurrentClass() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            currentClassId = 1;
            isInClass = false;
            return;
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
                return;
            }
            else if (startMinutes <= curMinutes && curMinutes <= endMinutes) {
                order = (int) timeEntity.getOrder();
                isInClass = true;
                currentClassId = (dayOfWeek - Calendar.MONDAY) * 5 + order;
                return;
            }
        }
        isInClass = false;
        currentClassId = (dayOfWeek + 1 - Calendar.MONDAY) * 5 + 1;
    }

    private void initWeekdayTitle() {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
        ViewGroup.LayoutParams childParams = new ViewGroup.LayoutParams(
                (tableWidth - layoutParams.leftMargin) / 5, layoutParams.height);
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(childParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setText(tableManager.getShowWeekday(i + 1));
            if (i % 2 == 0)
                textView.setBackgroundColor(getResources().getColor(R.color.table_color_2));
            else
                textView.setBackgroundColor(getResources().getColor(R.color.table_color_1));
            titleLayout.addView(textView);
        }
    }

    private void refreshShowTime() {
        timeLayout.removeAllViews();
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) timeLayout.getLayoutParams();
        ViewGroup.LayoutParams childParams = new ViewGroup.LayoutParams(
                layoutParams.width, (tableHeight - layoutParams.topMargin) / 5);
        List<String> timeList = timeManager.getAllByString();
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(childParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setText(timeList.get(i * 2) + "\n-\n" + timeList.get(i * 2 + 1));
            if (i % 2 == 0)
                textView.setBackgroundResource(R.drawable.selector_class_bg_2);
            else
                textView.setBackgroundResource(R.drawable.selector_class_bg_1);
            timeLayout.addView(textView);
        }
    }

    private void refreshShowTable() {
        classList = tableManager.getAll();
        if (!Verifier.isEffectiveList(classList) || classList.size() != 25) {
            tableManager.fixDao();
            classList = tableManager.getAll();
            loadFromServer();
        }
        if (tableGv.getAdapter() == null) {
            int classContentWidth = tableWidth - timeLayout.getLayoutParams().width;
            int classContentHeight = tableHeight - titleLayout.getLayoutParams().height;
            tableGv.setAdapter(new ClassTableAdapter(getActivity(), classList, currentClassId, isInClass,
                            classContentWidth, classContentHeight));
        }
        else {
            ClassTableAdapter adapter = (ClassTableAdapter) tableGv.getAdapter();
            adapter.setData(classList);
            adapter.setCurrentClass(currentClassId, isInClass);
            adapter.notifyDataSetChanged();
        }
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
}
