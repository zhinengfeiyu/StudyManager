package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Resolver;
import com.caiyu.studymanager.constant.ExtraKeys;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/4/29.
 */
public class EmptyClassroomActivity extends BaseActivity {

    @Bind(R.id.weekdayPicker)
    NumberPicker weekdayPicker;
    @Bind(R.id.startTimePicker)
    TimePicker startTimePicker;
    @Bind(R.id.endTimePicker)
    TimePicker endTimePicker;

    @Override
    public int getContentViewId() {
        return R.layout.activity_empty_classroom;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_search_empty_classroom));
        initWeekdayPicker();
        initTimePickers();
    }

    @OnClick(R.id.searchBtn)
    void click_search() {
        if (isTimeInputCorrect()) {
            Intent intent = new Intent(this, EmptyClassroomResultActivity.class);
            intent.putExtra(ExtraKeys.WEEKDAY, weekdayPicker.getValue());
            intent.putExtra(ExtraKeys.START_HOUR, startTimePicker.getCurrentHour());
            intent.putExtra(ExtraKeys.START_MINUTE, startTimePicker.getCurrentMinute());
            intent.putExtra(ExtraKeys.END_HOUR, endTimePicker.getCurrentHour());
            intent.putExtra(ExtraKeys.END_MINUTE, endTimePicker.getCurrentMinute());
            startActivity(intent);
        }
        else {
            showToast("起始时间不能超过结束时间");
        }
    }

    private void initWeekdayPicker() {
        weekdayPicker.setMinValue(Calendar.MONDAY);
        weekdayPicker.setMaxValue(Calendar.FRIDAY);
        weekdayPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return Resolver.resolveWeekday(value);
            }
        });
        weekdayPicker.setValue(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    }

    private void initTimePickers() {
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);
    }

    private boolean isTimeInputCorrect() {
        int startHour = startTimePicker.getCurrentHour();
        int startMinute = startTimePicker.getCurrentMinute();
        int endHour = endTimePicker.getCurrentHour();
        int endMinute = endTimePicker.getCurrentMinute();
        return startHour * 60 + startMinute <= endHour * 60 + endMinute;
    }
}
