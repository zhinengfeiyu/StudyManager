package com.caiyu.studymanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.caiyu.entity.NoteEntity;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.NoteManager;
import com.caiyu.studymanager.manager.SubjectManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/1/23.
 */
public class NoteDetailActivity extends BaseActivity {

    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.noteContentEt)
    EditText contentEt;

    NoteManager noteManager = NoteManager.getInstance();
    SubjectManager subjectManager = SubjectManager.getInstance();

    NoteEntity noteEntity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_note_detail;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_note));
        setTitleRightText("保存");
        //intent接收参数要么只有noteId，要么只有subjectId，不可能两个都有
        long noteId = getIntent().getLongExtra(ExtraKeys.NOTE_ID, -1L);
        if (noteId != -1L)
            noteEntity = noteManager.getDataById(noteId);
        long subjectId = getIntent().getLongExtra(ExtraKeys.SUBJECT_ID,
                noteEntity != null ? noteEntity.getSubjectId() : -1L);
        if (subjectManager.getDataById(subjectId) != null)  //存在该课程ID对应的课程
            initSpinner(subjectManager.getDataById(subjectId).getName());
        else
            initSpinner(null);  //下拉列表选择项为"未指定课程"
        initEditText();
    }

    @Override
    public void onBackPressed() {
        final String input = contentEt.getText().toString();
        if (shouldSave()) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("笔记内容已修改，是否保存？")
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveNote(input);
                            setResult(RESULT_OK);
                            showToast("笔记保存成功");
                            finish();
                        }
                    })
                    .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            dialog.show();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRightClick() {
        String content = contentEt.getText().toString();
        if (shouldSave()) {
            saveNote(content);
            showToast("笔记保存成功");
            setResult(RESULT_OK);
        }
        finish();
    }

    private void initSpinner(String curSubject) {
        List<String> subjectNameList = new ArrayList<>();
        subjectNameList.add("未指定课程");
        List<SubjectEntity> subjectEntityList = subjectManager.getAll();
        if (Verifier.isEffectiveList(subjectEntityList)) {
            for (SubjectEntity se : subjectEntityList) {
                subjectNameList.add(se.getName());
            }
        }
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNameList);
        //设置样式
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(spinnerAdapter);
        if (curSubject != null)
            spinner.setSelection(subjectNameList.indexOf(curSubject));
    }

    private void initEditText() {
        if (noteEntity != null) {
            contentEt.setText(noteEntity.getContent());
            contentEt.setSelection(contentEt.getText().length());
        }
        contentEt.addTextChangedListener(new TextWatcher() {
     //       private TextView rightTv = (TextView) findViewById(R.id.commonRightTv);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (shouldSave())
//                    rightTv.setEnabled(true);
//                else
//                    rightTv.setEnabled(false);
            }
        });
    }

    /**
     * 判断笔记是否需要保存
     * 如果是新建的笔记（noteEntity为null），则根据输入是否为空确定
     * 如果是原有的笔记，则根据新旧内容是否一致判断
     * @return
     *      如果需要保存，返回true，否则返回false
     */
    private boolean shouldSave() {
        String content = contentEt.getText().toString();
        if (noteEntity == null) {   //新建的笔记
            return Verifier.isEffectiveStr(content);
        }
        else {  //原有的笔记
            return !noteEntity.getContent().equals(content);
        }
    }

    /**
     * 保存笔记（是否要保存已判断）
     * 如果是原有的笔记，且输入为空，则删除该项笔记，否则执行保存
     * 如果是新建的笔记，则执行保存
     * @param content
     */
    private void saveNote(String content) {
        if (noteEntity != null) {
            if (Verifier.isEffectiveStr(content)) {
                noteEntity.setContent(content);
                noteEntity.setSubjectId(subjectManager.getIdByName((String) spinner.getSelectedItem()));
                noteEntity.setLastEditTime(System.currentTimeMillis());
                noteManager.update(noteEntity);
            } else {
                noteManager.deleteByKey(noteEntity.getId());
            }
        }
        else {
            if (Verifier.isEffectiveStr(content)) {
                noteEntity = new NoteEntity();
                noteEntity.setContent(content);
                noteEntity.setSubjectId(subjectManager.getIdByName((String) spinner.getSelectedItem()));
                noteEntity.setLastEditTime(System.currentTimeMillis());
                noteManager.addData(noteEntity);
            }
        }
    }
}
