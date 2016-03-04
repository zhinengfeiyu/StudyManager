package com.caiyu.studymanager.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.caiyu.studymanager.activity.NoteActivity;
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
    private int currentClassShowPosition;
    private boolean isInClass;
    private int itemWidth;
    private int itemHeight;

    public ClassTableAdapter(Activity activity, List<ClassTableEntity> list, int curId, boolean isInClass,
                             int totalWidth, int totalHeight) {
        this.activity = activity;
        this.classList = list;
        this.currentClassShowPosition = (curId - 1) % ROW_COUNT * COLUMN_COUNT + (curId - 1) / ROW_COUNT;
        this.isInClass = isInClass;
        this.itemWidth = totalWidth / COLUMN_COUNT;
        this.itemHeight = totalHeight / ROW_COUNT;
    }

    public List<ClassTableEntity> getData() {
        return classList;
    }

    public void setData(List<ClassTableEntity> list) {
        this.classList = list;
    }

    public void setCurrentClass(int curId, boolean isInClass) {
        this.currentClassShowPosition = curId % ROW_COUNT * COLUMN_COUNT + curId / ROW_COUNT;
        this.isInClass = isInClass;
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
            if (position == currentClassShowPosition) {
                if (isInClass)
                    convertView.setBackgroundColor(activity.getResources().getColor(R.color.green_light));
                else
                    convertView.setBackgroundColor(activity.getResources().getColor(R.color.red_light));
            }
            else if (position % 2 == 0)
                convertView.setBackgroundColor(activity.getResources().getColor(R.color.table_color_1));
            else
                convertView.setBackgroundColor(activity.getResources().getColor(R.color.table_color_2));
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
        convertView.setOnClickListener(new ClickListener(data));
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

    private void showDialog(final ClassTableEntity tableEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("请选择操作");
        CharSequence[] items;
        if (tableEntity.getSubjectId() != -1L)
            items = new CharSequence[]{"编辑课表项", "查看笔记"};
        else
            items = new CharSequence[]{"编辑课表项"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        editClassDetail(tableEntity.getId());
                        break;
                    case 1:
                        editNote(tableEntity.getSubjectId());
                        break;
                }
            }
        });
        builder.show();
    }

    private void editClassDetail(long id) {
        Intent intent = new Intent(activity, ClassSetActivity.class);
        intent.putExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, id);
        activity.startActivityForResult(intent, ClassTableActivity.REQ_TABLE);
    }

    private void editNote(long subjectId) {
        Intent intent = new Intent(activity, NoteActivity.class);
        intent.putExtra(ExtraKeys.SUBJECT_ID, subjectId);
        activity.startActivity(intent);
    }

    private class ClickListener implements View.OnClickListener {
        private ClassTableEntity tableEntity;

        public ClickListener(ClassTableEntity entity) {
            this.tableEntity = entity;
        }

        @Override
        public void onClick(View v) {
            showDialog(tableEntity);
        }
    }
}

