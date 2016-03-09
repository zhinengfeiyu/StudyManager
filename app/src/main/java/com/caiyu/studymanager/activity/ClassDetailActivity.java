package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.entity.NoteEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.ClassTableManager;
import com.caiyu.studymanager.manager.NoteManager;
import com.caiyu.studymanager.widget.SettingView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/1/23.
 */
public class ClassDetailActivity extends BaseActivity {

    @Bind(R.id.dayOfWeekView)
    SettingView dayOfWeekView;
    @Bind(R.id.orderOfDayView)
    SettingView orderOfDayView;
    @Bind(R.id.classNameView)
    SettingView classNameView;
    @Bind(R.id.classRoomView)
    SettingView classRoomView;
    @Bind(R.id.teacherView)
    SettingView teacherView;
    @Bind(R.id.startWeekView)
    SettingView startWeekView;
    @Bind(R.id.endWeekView)
    SettingView endWeekView;
    @Bind(R.id.noteView)
    SettingView noteView;
    @Bind(R.id.forumView)
    SettingView forumView;
    @Bind(R.id.deleteBtn)
    Button deleteBtn;

    private ClassTableManager tableManager = ClassTableManager.getInstance();
    private NoteManager noteManager = NoteManager.getInstance();
    private ClassTableEntity classEntity;

    private final int REQ_CLASS_NAME = 1;
    private final int REQ_CLASS_ROOM = 2;
    private final int REQ_WEEK = 3;
    private final int REQ_NOTE = 4;

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_detail;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_class_detail));
        refreshShowInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            refreshShowInfo();
        }
    }

    @OnClick(R.id.classNameView)
    void click_class_name() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(ExtraKeys.SEARCH_TYPE, SearchActivity.TYPE_CLASS);
        intent.putExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, classEntity.getId());
        startActivityForResult(intent, REQ_CLASS_NAME);
    }

    @OnClick(R.id.classRoomView)
    void click_class_room() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(ExtraKeys.SEARCH_TYPE, SearchActivity.TYPE_CLASS_ROOM);
        intent.putExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, classEntity.getId());
        startActivityForResult(intent, REQ_CLASS_ROOM);
    }

    @OnClick(R.id.teacherView)
    void click_teacher() {
        Intent intent = new Intent(this, TeacherActivity.class);
        intent.putExtra(ExtraKeys.TEACHER_NAME, classEntity.getTeacher());
        startActivity(intent);
    }

    @OnClick({R.id.startWeekView, R.id.endWeekView})
    void click_week() {
        Intent intent = new Intent(this, ClassWeekActivity.class);
        intent.putExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, classEntity.getId());
        startActivityForResult(intent, REQ_WEEK);
    }

    @OnClick(R.id.noteView)
    void click_note() {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(ExtraKeys.SUBJECT_ID, classEntity.getSubjectId());
        startActivityForResult(intent, REQ_NOTE);
    }

    @OnClick(R.id.forumView)
    void click_forum() {
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra(ExtraKeys.SUBJECT_ID, classEntity.getSubjectId());
        startActivity(intent);
    }

    @OnClick(R.id.deleteBtn)
    void click_delete() {
        new AlertDialog.Builder(this)
                .setMessage("确认删除该课程？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        classEntity.setClassName("");
                        classEntity.setClassRoom("");
                        classEntity.setTeacher("");
                        classEntity.setStartWeek(0);
                        classEntity.setEndWeek(0);
                        tableManager.update(classEntity);
                        setResult(Activity.RESULT_OK);
                        finish();
                        showToast("该课程已被删除");
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void refreshShowInfo() {
        long id = getIntent().getLongExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, 0);
        classEntity = tableManager.getDataById(id);
        if (classEntity == null) {
            finish();
            return;
        }
        dayOfWeekView.setText(tableManager.getShowWeekday(classEntity.getDayOfWeek()));
        orderOfDayView.setText(tableManager.getShowOrder(classEntity.getOrderOfDay()));
        classNameView.setText(classEntity.getClassName());
        classRoomView.setText(classEntity.getClassRoom());
        teacherView.setText(classEntity.getTeacher());
        startWeekView.setText(classEntity.getStartWeek() + "");
        endWeekView.setText(classEntity.getEndWeek() + "");
        NoteEntity noteEntity = noteManager.getDataBySubjectId(classEntity.getSubjectId());
        noteView.setText(Verifier.isEffectiveStr(noteEntity.getContent())
                        ? noteEntity.getContent() : "暂无笔记");
        if (classEntity.getClassName().equals(""))
            deleteBtn.setEnabled(false);
    }
}
