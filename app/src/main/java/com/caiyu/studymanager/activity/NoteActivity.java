package com.caiyu.studymanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        setTitle(getString(R.string.title_note));
        long subjectId = getIntent().getLongExtra(ExtraKeys.SUBJECT_ID, -1);
        noteEntity = noteManager.getDataBySubjectId(subjectId);
        contentEt.setText(noteEntity.getContent());
    }

    @Override
    public void onBackPressed() {
        final String input = contentEt.getText().toString();
        if (!input.equals(noteEntity.getContent())) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("笔记内容已修改，是否保存？")
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveNote(input);
                            setResult(RESULT_OK);
                            finish();
                            showToast("笔记保存成功");
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

    @OnClick(R.id.submitBtn)
    void click_submit() {
        String content = contentEt.getText().toString();
        if (!noteEntity.getContent().equals(content)) {
            saveNote(content);
            showToast("笔记保存成功");
            setResult(RESULT_OK);
        }
        finish();
    }

    private void saveNote(String content) {
        noteEntity.setContent(content);
        noteEntity.setLastEditTime(System.currentTimeMillis());
        noteManager.update(noteEntity);
    }
}
