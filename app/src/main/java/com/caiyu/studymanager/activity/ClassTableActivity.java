package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.ClassDetailDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 渝 on 2016/1/23.
 */
public class ClassTableActivity extends Activity {

    private GridView tableGv;

    private List<ClassDetailDO> classList;

    private int unitWidth;
    private int unitHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_table);
        tableGv = (GridView) findViewById(R.id.tableGv);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        unitWidth = (screenWidth - tableGv.getPaddingLeft() - tableGv.getPaddingRight()) / 5;
        unitHeight = (screenHeight - tableGv.getPaddingTop() - tableGv.getPaddingBottom()) / 5;
        getDefaultTableData();
        initViews();
    }

    private void initViews() {
        tableGv.setAdapter(new ClassTableAdapter());
    }

    private class ClassTableAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (classList != null)
                return classList.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            return classList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ClassTableActivity.this).inflate(R.layout.item_class_table, null);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(unitWidth, unitHeight);
                convertView.setLayoutParams(params);
                if (position % 2 == 0)
                    convertView.setBackgroundColor(getResources().getColor(R.color.red_light));
                else
                    convertView.setBackgroundColor(getResources().getColor(R.color.green_light));
                holder = new ViewHolder();
                holder.classNameTv = (TextView) convertView.findViewById(R.id.classNameTv);
                holder.classRoomTv = (TextView) convertView.findViewById(R.id.classRoomTv);
                holder.teacherTv = (TextView) convertView.findViewById(R.id.teacherTv);
                holder.weeksTv = (TextView) convertView.findViewById(R.id.weeksTv);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            ClassDetailDO data = (ClassDetailDO) getItem(position);
            holder.classNameTv.setText(data.getClassName());
            holder.classRoomTv.setText(data.getClassRoom());
            holder.teacherTv.setText(data.getTeacher());
            String weeksFormatStr = "第%1$d-%2$d周";
            holder.weeksTv.setText(String.format(weeksFormatStr, data.getStartWeek(), data.getEndWeek()));
            return convertView;
        }

        class ViewHolder {
            TextView classNameTv;
            TextView classRoomTv;
            TextView teacherTv;
            TextView weeksTv;
        }
    }

    private void getDefaultTableData() {
        if (classList == null) {
            classList = new ArrayList<>(45);
        }
        else {
            classList.clear();
        }
        ClassDetailDO record;

        record = new ClassDetailDO();
        record.setClassName("数据结构与算法");
        record.setClassRoom("同和405");
        record.setTeacher("刘斌");
        record.setStartWeek(1);
        record.setEndWeek(16);
        classList.add(record);

        record = new ClassDetailDO();
        record.setClassName("计算机组成原理");
        record.setClassRoom("厚学302");
        record.setTeacher("糜元根");
        record.setStartWeek(3);
        record.setEndWeek(14);
        classList.add(record);

        record = new ClassDetailDO();
        record.setClassName("计算机网络");
        record.setClassRoom("仁智208");
        record.setTeacher("张芃");
        record.setStartWeek(1);
        record.setEndWeek(12);
        classList.add(record);

        record = new ClassDetailDO();
        record.setClassName("软件工程导论");
        record.setClassRoom("厚学601");
        record.setTeacher("段江");
        record.setStartWeek(4);
        record.setEndWeek(10);
        classList.add(record);

        record = new ClassDetailDO();
        record.setClassName("计算机专业英语");
        record.setClassRoom("仁智408");
        record.setTeacher("曹磊");
        record.setStartWeek(2);
        record.setEndWeek(17);
        classList.add(record);

        record = new ClassDetailDO();
        record.setClassName("离散数学");
        record.setClassRoom("同和306");
        record.setTeacher("曹磊");
        record.setStartWeek(3);
        record.setEndWeek(11);
        classList.add(record);

    }

}
