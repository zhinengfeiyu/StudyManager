package com.caiyu.studymanager.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.caiyu.entity.TeacherEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.manager.TeacherManager;
import com.caiyu.studymanager.widget.SettingView;

import butterknife.Bind;

/**
 * Created by 渝 on 2016/3/7.
 */
public class TeacherActivity extends BaseActivity {

    @Bind(R.id.headImg)
    ImageView headView;
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
        setTitle(getString(R.string.title_teacher));
        String name = getIntent().getStringExtra(ExtraKeys.TEACHER_NAME);
        nameView.setText(name);
        teacherEntity = teacherManager.getDataByName(name);
        if (teacherEntity != null) {
            sexView.setText(teacherEntity.getSex());
            academyView.setText(teacherEntity.getAcademy());
            positionView.setText(teacherEntity.getPosition());
            phoneView.setText(teacherEntity.getPhone());
            requestHeadImage();
        }
    }

    private void requestHeadImage() {
        headView.setImageResource(R.mipmap.head);
        ImageRequest imageRequest = new ImageRequest(
                String.format(Server.IMAGE_URL + "/teacher%1$d.jpg", teacherEntity.getId()),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        headView.setImageBitmap(response);
                    }
                }, 100, 100, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        imageRequest.setTag("showTeacherHeadImage");
        MyApplication.getRequestQueue().add(imageRequest);
    }
}
