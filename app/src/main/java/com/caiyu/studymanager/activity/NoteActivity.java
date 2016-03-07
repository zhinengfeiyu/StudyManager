package com.caiyu.studymanager.activity;

import android.widget.Button;
import android.widget.EditText;

import com.caiyu.entity.NoteEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.NoteManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/1/23.
 */
public class NoteActivity extends BaseActivity {

    @Bind(R.id.noteContentEt)
    EditText contentEt;
    @Bind(R.id.submitBtn)
    Button submitBtn;

    NoteManager noteManager = NoteManager.getInstance();

    NoteEntity noteEntity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_note;
    }

    @Override
    public void afterViewCreated() {
        long subjectId = getIntent().getLongExtra(ExtraKeys.SUBJECT_ID, -1);
        noteEntity = noteManager.getDataBySubjectId(subjectId);
        contentEt.setText(noteEntity.getContent());
    }

    @OnClick(R.id.submitBtn)
    void click_submit() {
        String content = contentEt.getText().toString();
        if (!noteEntity.getContent().equals(content)) {
            noteEntity.setContent(content);
            noteEntity.setLastEditTime(System.currentTimeMillis());
            noteManager.update(noteEntity);
            showToast("笔记保存成功");
            setResult(RESULT_OK);
        }
        finish();
    }
}
