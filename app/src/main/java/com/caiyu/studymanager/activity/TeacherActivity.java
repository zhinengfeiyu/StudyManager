package com.caiyu.studymanager.activity;

import com.caiyu.entity.TeacherEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.TeacherManager;
import com.caiyu.studymanager.widget.SettingView;

import butterknife.Bind;

/**
 * Created by 渝 on 2016/3/7.
 */
public class TeacherActivity extends BaseActivity {

    @Bind(R.id.nameView)
    SettingView nameView;
    @Bind(R.id.sexView)
    SettingView sexView;
    @Bind(R.id.academyView)
    SettingView academyView;
    @Bind(R.id.positionView)
    SettingView positionView;
    @Bind(R.id.phoneView)
    SettingView phoneView;

    private TeacherManager teacherManager = TeacherManager.getInstance();
    private TeacherEntity teacherEntity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_teacher;
    }

    @Override
    public void afterViewCreated() {
        String name = getIntent().getStringExtra(ExtraKeys.TEACHER_NAME);
        nameView.setText(name);
        teacherEntity = teacherManager.getDataByName(name);
        if (teacherEntity != null) {
            sexView.setText(teacherEntity.getSex());
            academyView.setText(teacherEntity.getAcademy());
            positionView.setText(teacherEntity.getPosition());
            phoneView.setText(teacherEntity.getPhone());
        }
    }
}
