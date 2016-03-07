package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.ClassTableManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/3/7.
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.inputEt)
    EditText inputEt;
    @Bind(R.id.confirmBtn)
    Button confirmBtn;

    private ClassTableManager classManager = ClassTableManager.getInstance();

    public static final int TYPE_CLASS = 1;
    public static final int TYPE_CLASS_ROOM = 2;
    public static final int TYPE_TEACHER = 3;

    private int inputType;
    private long classId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void afterViewCreated() {
        inputType = getIntent().getIntExtra(ExtraKeys.SEARCH_TYPE, 1);
        classId = getIntent().getLongExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, 1L);
        initInputChange();
    }

    @OnClick(R.id.cancelBtn)
    void click_cancel() {
        finish();
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm() {
        String inputStr = inputEt.getText().toString().trim();
        saveResult(inputStr);
        returnResult(inputStr);
    }

    private void initInputChange() {
        inputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = s.toString().trim();
                confirmBtn.setEnabled(Verifier.isEffectiveStr(inputStr));
                listResult(inputStr);
            }
        });
    }

    private void listResult(String inputStr) {

    }

    private void saveResult(String inputStr) {
        if (inputType == TYPE_CLASS) {

        }
        else if (inputType == TYPE_CLASS_ROOM) {
            ClassTableEntity classEntity = classManager.getDataById(classId);
            classEntity.setClassRoom(inputStr);
            classManager.update(classEntity);
        }
        else if (inputType == TYPE_TEACHER) {

        }
    }

    private void returnResult(String str) {
        Intent intent = new Intent();
        intent.putExtra(ExtraKeys.UPDATE_RESULT, str);
        setResult(RESULT_OK, intent);
        finish();
    }
}
