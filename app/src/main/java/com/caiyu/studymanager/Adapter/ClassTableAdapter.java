package com.caiyu.studymanager.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.activity.ClassSetActivity;
import com.caiyu.studymanager.activity.ClassTableActivity;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;

import java.util.List;

/**
 * Created by 渝 on 2016/1/31.
 */
public class ClassTableAdapter extends BaseAdapter {

    private static final int ROW_COUNT = 5;
    private static final int COLUMN_COUNT = 5;

    private Activity activity;
    private List<ClassTableEntity> classList;
    private int itemWidth;
    private int itemHeight;

    public ClassTableAdapter(Activity activity, List<ClassTableEntity> list, int totalWidth, int totalHeight) {
        this.activity = activity;
        this.classList = list;
        this.itemWidth = totalWidth / COLUMN_COUNT;
        this.itemHeight = totalHeight / ROW_COUNT;
    }

    public List<ClassTableEntity> getData() {
        return classList;
    }

    public void setData(List<ClassTableEntity> list) {
        this.classList = list;
    }

    @Override
    public int getCount() {
        if (classList != null)
            return classList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        int dataPosition = position % ROW_COUNT * COLUMN_COUNT + position / ROW_COUNT;
        return classList.get(dataPosition);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_class_table, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemWidth, itemHeight);
            convertView.setLayoutParams(params);
            if (position % 2 == 0)
                convertView.setBackgroundColor(activity.getResources().getColor(R.color.red_light));
            else
                convertView.setBackgroundColor(activity.getResources().getColor(R.color.green_light));
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
        ClassTableEntity data = (ClassTableEntity) getItem(position);
        if (isEffectiveClass(data)) {
            holder.classNameTv.setText(data.getClassName());
            holder.classRoomTv.setText(data.getClassRoom());
            holder.teacherTv.setText(data.getTeacher());
            String weeksFormatStr = "第%1$d-%2$d周";
            holder.weeksTv.setText(String.format(weeksFormatStr, data.getStartWeek(), data.getEndWeek()));
        }
        else {
            holder.classNameTv.setText("");
            holder.classRoomTv.setText("");
            holder.teacherTv.setText("");
            holder.weeksTv.setText("");
        }
        convertView.setOnClickListener(new ClickListener(data.getId()));
        return convertView;
    }

    private boolean isEffectiveClass(ClassTableEntity entity) {
        if (!Verifier.isEffectiveStr(entity.getClassName()))
            return false;
        return true;
    }

    private static class ViewHolder {
        TextView classNameTv;
        TextView classRoomTv;
        TextView teacherTv;
        TextView weeksTv;
    }

    private class ClickListener implements View.OnClickListener {
        private long id;

        public ClickListener(long id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, ClassSetActivity.class);
            intent.putExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, id);
            activity.startActivityForResult(intent, ClassTableActivity.REQ_TABLE);
        }
    }
}

