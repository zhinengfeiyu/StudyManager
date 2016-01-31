package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.ClassTableManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/1/30.
 */
public class ClassSetActivity extends BaseActivity{

    @Bind(R.id.dayOfWeekEt)
    EditText dayOfWeekEt;
    @Bind(R.id.orderOfDayEt)
    EditText orderOfDayEt;
    @Bind(R.id.classNameEt)
    EditText classNameEt;
    @Bind(R.id.classRoomEt)
    EditText classRoomEt;
    @Bind(R.id.teacherEt)
    EditText teacherEt;
    @Bind(R.id.startWeekEt)
    EditText startWeekEt;
    @Bind(R.id.endWeekEt)
    EditText endWeekEt;
    @Bind(R.id.confirmBtn)
    Button confirmBtn;

    private ClassTableManager tableManager = ClassTableManager.getInstance();

    private ClassTableEntity classEntity;

    private Toast toast;

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_set;
    }

    @Override
    public void afterViewCreated() {
        long id = getIntent().getLongExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, 0);
        classEntity = tableManager.getDataById(id);
        if (classEntity == null) {
            finish();
            return;
        }
        dayOfWeekEt.setText(getShowWeekday(classEntity.getDayOfWeek()));
        orderOfDayEt.setText(getShowOrder(classEntity.getOrderOfDay()));
        classNameEt.setText(classEntity.getClassName());
        classRoomEt.setText(classEntity.getClassRoom());
        teacherEt.setText(classEntity.getTeacher());
        startWeekEt.setText(classEntity.getStartWeek() + "");
        endWeekEt.setText(classEntity.getEndWeek() + "");
    }

    @Override
    public void showToast(String msg) {
        if (toast == null)
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        else
            toast.setText(msg);
        toast.show();
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm_update() {
        if (verifyInput() == false) return;
        String className = classNameEt.getText().toString().trim();
        String classRoom = classRoomEt.getText().toString().trim();
        String teacher = teacherEt.getText().toString().trim();
        int startWeek = Integer.parseInt(startWeekEt.getText().toString().trim());
        int endWeek = Integer.parseInt(endWeekEt.getText().toString().trim());
        classEntity.setClassName(className);
        classEntity.setClassRoom(classRoom);
        classEntity.setTeacher(teacher);
        classEntity.setStartWeek(startWeek);
        classEntity.setEndWeek(endWeek);
        tableManager.update(classEntity);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private boolean verifyInput() {
        String className = classNameEt.getText().toString().trim();
        if (!Verifier.isEffectiveStr(className)) {
            showToast("课程名称不能为空");
            return false;
        }
        String startWeekStr = startWeekEt.getText().toString().trim();
        if (!Verifier.isEffectiveStr(startWeekStr)) {
            showToast("起始周不能为空");
            return false;
        }
        if (!Verifier.isEffectiveUnsignedInt(startWeekStr)) {
            showToast("起始周是无效输入，请输入1-30的整数");
            return false;
        }
        int startWeek = Integer.parseInt(startWeekStr);
        if (startWeek < 1 || startWeek > 30) {
            showToast("起始周的数字超限，只能是1-30");
            return false;
        }
        String endWeekStr = endWeekEt.getText().toString().trim();
        if (!Verifier.isEffectiveStr(endWeekStr)) {
            showToast("结束周不能为空");
            return false;
        }
        if (!Verifier.isEffectiveUnsignedInt(endWeekStr)) {
            showToast("结束周是无效输入，请输入1-30的整数");
            return false;
        }
        int endWeek = Integer.parseInt(endWeekStr);
        if (endWeek < 1 || endWeek > 30) {
            showToast("结束周的数字超限，只能是1-30");
            return false;
        }
        if (Integer.parseInt(startWeekStr) > Integer.parseInt(endWeekStr)) {
            showToast("起始周不能大于结束周");
            return false;
        }
        return true;
    }

    private String getShowWeekday(int weekday) {
        String showStr;
        switch (weekday) {
            case 1:
                showStr = "星期一";
                break;
            case 2:
                showStr = "星期二";
                break;
            case 3:
                showStr = "星期三";
                break;
            case 4:
                showStr = "星期四";
                break;
            case 5:
                showStr = "星期五";
                break;
            default:
                showStr = "";
        }
        return showStr;
    }

    private String getShowOrder(int order) {
        String showStr;
        switch (order) {
            case 1:
                showStr = "上午第一二节";
                break;
            case 2:
                showStr = "上午第三四节";
                break;
            case 3:
                showStr = "下午第一二节";
                break;
            case 4:
                showStr = "下午第三四节";
                break;
            case 5:
                showStr = "晚上第一二节";
                break;
            default:
                showStr = "";
        }
        return showStr;
    }
}
