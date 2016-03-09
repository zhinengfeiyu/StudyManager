package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.caiyu.entity.ClassTimeEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.manager.ClassTimeManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/2/13.
 */
public class ClassTimeSetActivity extends BaseActivity {

    private ClassTimeManager timeManager = ClassTimeManager.getInstance();

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.btn3)
    Button btn3;
    @Bind(R.id.btn4)
    Button btn4;
    @Bind(R.id.btn5)
    Button btn5;
    @Bind(R.id.btn6)
    Button btn6;
    @Bind(R.id.btn7)
    Button btn7;
    @Bind(R.id.btn8)
    Button btn8;
    @Bind(R.id.btn9)
    Button btn9;
    @Bind(R.id.btn10)
    Button btn10;
    @Bind(R.id.confirmBtn)
    Button confirmBtn;

    private List<Button> btnList;
    private Toast toast;

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_time_set;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_class_time));
        btnList = new ArrayList<>(10);
        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);
        btnList.add(btn4);
        btnList.add(btn5);
        btnList.add(btn6);
        btnList.add(btn7);
        btnList.add(btn8);
        btnList.add(btn9);
        btnList.add(btn10);
        List<String> timeStrList = timeManager.getAllByString();
        for (int i = 0; i < btnList.size(); i++) {
            Button btn = btnList.get(i);
            btn.setText(timeStrList.get(i));
            btn.setOnClickListener(new TimeClickListener());
        }
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm_btn() {
        if (verifyTimeInput() == true) {
            saveTimeInput();
            showToast("设置成功");
            setResult(Activity.RESULT_OK);
            finish();
        }
        else {
            String msg = "时间设置有误";
            if (toast == null)
                toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
            else
                toast.setText(msg);
            toast.show();
        }
    }

    private void saveTimeInput() {
        for (int i = 0; i < 5; i++) {
            String startTimeText = btnList.get(i * 2).getText().toString();
            int startHour = Integer.parseInt(startTimeText.substring(0, 2));
            int startMinute = Integer.parseInt(startTimeText.substring(3, 5));
            String endTimeText = btnList.get(i * 2 + 1).getText().toString();
            int endHour = Integer.parseInt(endTimeText.substring(0, 2));
            int endMinute = Integer.parseInt(endTimeText.substring(3, 5));
            ClassTimeEntity entity = new ClassTimeEntity(i+1, startHour, startMinute, endHour, endMinute);
            timeManager.update(entity);
        }
    }

    private boolean verifyTimeInput() {
        int frontTime = 0;
        for (int i = 0; i < btnList.size(); i++) {
            String timeText = btnList.get(i).getText().toString();
            int hour = Integer.parseInt(timeText.substring(0, 2));
            int minute = Integer.parseInt(timeText.substring(3, 5));
            int curTime = hour * 60 + minute;
            if (frontTime > curTime)
                return false;
            else
                frontTime = curTime;
        }
        return true;
    }

    private class TimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final Button btn = (Button) v;
            String timeText = btn.getText().toString();
            int hour = Integer.parseInt(timeText.substring(0, 2));
            int minute = Integer.parseInt(timeText.substring(3, 5));
            TimePickerDialog dialog = new TimePickerDialog(
                    ClassTimeSetActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            btn.setText(timeManager.formatTime(hourOfDay, minute));
                            confirmBtn.setEnabled(true);
                        }
                    },
                    hour,
                    minute,
                    true
            );
            dialog.show();
        }
    }
}
