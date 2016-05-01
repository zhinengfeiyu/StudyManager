package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.ExtraKeys;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/4/29.
 */
public class TaskSetActivity extends BaseActivity {

    @Bind(R.id.timePicker)
    TimePicker timePicker;
    @Bind(R.id.remindInfoEt)
    EditText remindInfoEt;
    @Bind(R.id.recordVoiceBtn)
    Button recordVoiceBtn;

    @Override
    public int getContentViewId() {
        return R.layout.activity_task_set;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_task_set));
        timePicker.setIs24HourView(true);
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm() {
        showToast("任务添加成功");
        Intent intent = new Intent();
        intent.putExtra(ExtraKeys.HOUR, timePicker.getCurrentHour());
        intent.putExtra(ExtraKeys.MINUTE, timePicker.getCurrentMinute());
        intent.putExtra(ExtraKeys.REMIND_CONTENT, remindInfoEt.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
