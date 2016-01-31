package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    @Bind(R.id.deleteBtn)
    Button deleteBtn;

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
        dayOfWeekEt.setText(tableManager.getShowWeekday(classEntity.getDayOfWeek()));
        orderOfDayEt.setText(tableManager.getShowOrder(classEntity.getOrderOfDay()));
        classNameEt.setText(classEntity.getClassName());
        classRoomEt.setText(classEntity.getClassRoom());
        teacherEt.setText(classEntity.getTeacher());
        startWeekEt.setText(classEntity.getStartWeek() + "");
        endWeekEt.setText(classEntity.getEndWeek() + "");
        if (classEntity.getClassName().equals(""))
            deleteBtn.setEnabled(false);
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

}
