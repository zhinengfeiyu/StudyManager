package com.caiyu.studymanager.activity;

import android.widget.NumberPicker;
import android.widget.TextView;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.ClassTableManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/3/7.
 */
public class ClassWeekActivity extends BaseActivity {

    @Bind(R.id.classNameTv)
    TextView classNameTv;
    @Bind(R.id.weekResultTv)
    TextView weekResultTv;
    @Bind(R.id.startWeekPicker)
    NumberPicker startWeekPicker;
    @Bind(R.id.endWeekPicker)
    NumberPicker endWeekPicker;

    private ClassTableManager classManager = ClassTableManager.getInstance();
    private ClassTableEntity classEntity;

    private final String WEEK_RESULT_FORMAT = "从 第%1$d周 到 第%2$d周";

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_week;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_class_week));
        long classId = getIntent().getLongExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, 1L);
        classEntity = classManager.getDataById(classId);
        classNameTv.setText(classEntity.getClassName());
        int startWeek = classEntity.getStartWeek();
        int endWeek = classEntity.getEndWeek();
        weekResultTv.setText(String.format(WEEK_RESULT_FORMAT, startWeek, endWeek));
        initPicker(startWeekPicker, startWeek);
        initPicker(endWeekPicker, endWeek);
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm() {
        if (startWeekPicker.getValue() == classEntity.getStartWeek()
                && endWeekPicker.getValue() == classEntity.getEndWeek()) {
            finish();
            return;
        }
        if (isInfoCorrect()) {
            classEntity.setStartWeek(startWeekPicker.getValue());
            classEntity.setEndWeek(endWeekPicker.getValue());
            classManager.update(classEntity);
            showToast("课程始末周已修改");
            setResult(RESULT_OK);
            finish();
        }
        else {
            showToast("起始周不能大于结束周");
        }
    }

    private void initPicker(NumberPicker picker, int currentValue) {
        picker.setMinValue(1);
        picker.setMaxValue(25);
        picker.setValue(currentValue);
        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("第%1$d周", value);
            }
        });
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                weekResultTv.setText(String.format(WEEK_RESULT_FORMAT,
                        startWeekPicker.getValue(), endWeekPicker.getValue()));
            }
        });
    }

    private boolean isInfoCorrect() {
        int startWeek = startWeekPicker.getValue();
        int endWeek = endWeekPicker.getValue();
        if (startWeek > endWeek)
            return false;
        return true;
    }
}
