package com.caiyu.studymanager.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.activity.ClassDetailActivity;
import com.caiyu.studymanager.activity.ClassSetActivity;
import com.caiyu.studymanager.activity.NoteDetailActivity;
import com.caiyu.studymanager.bean.ClassOffsetBean;
import com.caiyu.studymanager.common.Resolver;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.fragment.ClassFragment;

import java.util.List;

/**
 * Created by 渝 on 2016/1/31.
 */
public class ClassTableAdapter extends BaseAdapter {

    private static final int ROW_COUNT = 5;
    private static final int COLUMN_COUNT = 5;

    private Fragment fragmentContext;
    private List<ClassTableEntity> classList;
    private int currentClassShowPosition;
    private ClassOffsetBean classOffsetBean;
    private int itemWidth;
    private int itemHeight;

    public ClassTableAdapter(Fragment fragment, List<ClassTableEntity> list, ClassOffsetBean classOffsetBean,
                             int totalWidth, int totalHeight) {
        this.fragmentContext = fragment;
        this.classList = list;
        this.classOffsetBean = classOffsetBean;
        if (classOffsetBean.classId != 0)
            this.currentClassShowPosition = (classOffsetBean.classId - 1) % ROW_COUNT * COLUMN_COUNT
                                            + (classOffsetBean.classId - 1) / ROW_COUNT;
        else
            this.currentClassShowPosition = -1;
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
            convertView = LayoutInflater.from(fragmentContext.getActivity()).inflate(R.layout.item_class_table, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemWidth, itemHeight);
            convertView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.classNameTv = (TextView) convertView.findViewById(R.id.classNameTv);
            holder.classRoomTv = (TextView) convertView.findViewById(R.id.classRoomTv);
            holder.teacherTv = (TextView) convertView.findViewById(R.id.teacherTv);
            convertView.setTag(holder);
            if (position == currentClassShowPosition) {
                if (classOffsetBean.isInClass == true)
                    convertView.setBackgroundResource(R.drawable.selector_red_light_pressed);
                else
                    convertView.setBackgroundResource(R.drawable.selector_green_light_pressed);
                holder.teacherTv.setText(Resolver.resolveTimeOffset(classOffsetBean));
            }
            else if (position % 2 == 0)
                convertView.setBackgroundResource(R.drawable.selector_class_bg_1);
            else
                convertView.setBackgroundResource(R.drawable.selector_class_bg_2);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClassTableEntity data = (ClassTableEntity) getItem(position);
        if (isEffectiveClass(data)) {
            holder.classNameTv.setText(data.getClassName());
            holder.classRoomTv.setText(data.getClassRoom());
            if (position != currentClassShowPosition)
                holder.teacherTv.setText(data.getTeacher());
        }
        else {
            holder.classNameTv.setText("");
            holder.classRoomTv.setText("");
            if (position != currentClassShowPosition)
                holder.teacherTv.setText("");
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
    }

    private void showDialog(final ClassTableEntity tableEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentContext.getActivity());
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
        Intent intent = new Intent(fragmentContext.getActivity(), ClassSetActivity.class);
        intent.putExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, id);
        fragmentContext.getActivity().startActivityForResult(intent, ClassFragment.REQ_TABLE);
    }

    private void editNote(long subjectId) {
        Intent intent = new Intent(fragmentContext.getActivity(), NoteDetailActivity.class);
        intent.putExtra(ExtraKeys.SUBJECT_ID, subjectId);
        fragmentContext.getActivity().startActivity(intent);
    }

    private class ClickListener implements View.OnClickListener {
        private ClassTableEntity tableEntity;

        public ClickListener(ClassTableEntity entity) {
            this.tableEntity = entity;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(fragmentContext.getActivity(), ClassDetailActivity.class);
            intent.putExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, tableEntity.getId());
            fragmentContext.startActivityForResult(intent, ClassFragment.REQ_TABLE);
        }
    }
}

