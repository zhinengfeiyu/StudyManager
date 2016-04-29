package com.caiyu.studymanager.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.entity.ClassTimeEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Resolver;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.manager.ClassTimeManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by 渝 on 2016/4/29.
 */
public class EmptyClassroomResultActivity extends BaseActivity {

    @Bind(R.id.timeChoiceTv)
    TextView timeChoiceTv;
    @Bind(R.id.listView)
    ListView listView;

    private int weekday;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    @Override
    public int getContentViewId() {
        return R.layout.activity_empty_classroom_result;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_search_empty_classroom));
        Bundle bundle = getIntent().getExtras();
        weekday = bundle.getInt(ExtraKeys.WEEKDAY);
        startHour = bundle.getInt(ExtraKeys.START_HOUR);
        startMinute = bundle.getInt(ExtraKeys.START_MINUTE);
        endHour = bundle.getInt(ExtraKeys.END_HOUR);
        endMinute = bundle.getInt(ExtraKeys.END_MINUTE);
        initTimeTv();
        requestResult();
    }

    private void initTimeTv() {
        String formatStr = "自习时间： %1$s 从 %2$d：%3$d 到 %4$d：%5$d";
        timeChoiceTv.setText(String.format(formatStr, Resolver.resolveWeekday(weekday),
                            startHour, startMinute, endHour, endMinute));
    }

    private void requestResult() {
        int startEndClassOrder = getStartEndClassOrder();
        final int startClassOrder = startEndClassOrder / 100;
        final int endClassOrder = startEndClassOrder % 100;
//        showToast("startClassOrder:" + startClassOrder);
//        showToast("endClassOrder:" + endClassOrder);
        if (startClassOrder == 0 && endClassOrder == 0) {
            showLongToast("该时间段不在上课期间，所有教室都是空哒~~");
        }
        else {
            StringRequest request = new StringRequest(Request.Method.POST, Server.GET_EMPTY_CLASSROOM_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cancelDialog();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                List<String> classroomList = new ArrayList<>(jsonArray.length());
                                for (int i=0;i<jsonArray.length();i++) {
                                    classroomList.add(jsonArray.getString(i));
                                }
                                listView.setAdapter(new ArrayAdapter<>(EmptyClassroomResultActivity.this,
                                                    android.R.layout.simple_list_item_1, classroomList));
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
                    map.put(Server.REQ_START_CLASS_ORDER, ""+startClassOrder);
                    map.put(Server.REQ_END_CLASS_ORDER, ""+endClassOrder);
                    return map;
                }
            };
            request.setTag("getEmptyClassroom");
            MyApplication.getRequestQueue().add(request);
            showDialog("正在搜索可用教室");
        }
    }

    private int getStartEndClassOrder() {
        ClassTimeManager classTimeManager = ClassTimeManager.getInstance();
        int startClassOrder = (weekday - Calendar.MONDAY) * 5;
        int endClassOrder = startClassOrder;
        int startMinutes = startHour * 60 + startMinute;
        int endMinutes = endHour * 60 + endMinute;
        boolean startClassOrderSolid = false;
        boolean endClassOrderSolid = false;
        List<ClassTimeEntity> timeList = classTimeManager.getAll();
        for (ClassTimeEntity classTimeEntity : timeList) {
            int entityStartMinutes = classTimeEntity.getStartHour() * 60 + classTimeEntity.getStartMinute();
            int entityEndMinutes = classTimeEntity.getEndHour() * 60 + classTimeEntity.getEndMinute();
            if (!startClassOrderSolid) {
                if (startMinutes <= entityStartMinutes && endMinutes <= entityStartMinutes) {
                    startClassOrder = endClassOrder = 0;
                    startClassOrderSolid = true;
                    endClassOrderSolid = true;
                }
                else if (startMinutes <= entityEndMinutes && endMinutes <= entityEndMinutes) {
                    startClassOrder = endClassOrder = endClassOrder + 1;
                    startClassOrderSolid = true;
                    endClassOrderSolid = true;
                }
                else if (startMinutes <= entityEndMinutes && endMinutes > entityEndMinutes) {
                    startClassOrder = endClassOrder = endClassOrder + 1;
                    startClassOrderSolid = true;
                }
                else {
                    startClassOrder = endClassOrder = endClassOrder + 1;
                }
            }
            else {  // startClassOrderSolid == true
                if (endMinutes <= entityStartMinutes) {
                    endClassOrderSolid = true;
                }
                else if (endMinutes <= entityEndMinutes) {
                    endClassOrder++;
                    endClassOrderSolid = true;
                }
                else {
                    endClassOrder++;
                }
            }
//            showToast("startClassOrder:" + startClassOrder);
//            showToast("endClassOrder:" + endClassOrder);
            if (startClassOrderSolid && endClassOrderSolid) {
                break;
            }
        }
        if (startClassOrderSolid == false && endClassOrderSolid == false) {
            startClassOrder = endClassOrder = 0;
        }
        return startClassOrder * 100 + endClassOrder;
    }
}
