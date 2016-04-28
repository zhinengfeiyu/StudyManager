package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.widget.ListView;

import com.caiyu.entity.NoteEntity;
import com.caiyu.studymanager.adapter.NoteAdapter;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.NoteManager;
import com.caiyu.studymanager.manager.SubjectManager;

import java.util.List;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * Created by 渝 on 2016/3/11.
 */
public class NoteListActivity extends BaseActivity {

    @Bind(R.id.noteLv)
    ListView listView;

    private SubjectManager subjectManager = SubjectManager.getInstance();
    private NoteManager noteManager = NoteManager.getInstance();

    private boolean noteChanged = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_note_list;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_note));
        setTitleRightIcon(R.mipmap.pencil);
        refreshShowNote(getIntent().getLongExtra(ExtraKeys.SUBJECT_ID, -1L));
    }

    /**
     * 新建笔记：
     * 如果当前列表显示的是单一课程笔记，则指定新笔记的课程名，
     * 如果当前列表显示的是所有笔记，则新笔记默认不指定课程名
     * 新页面与查看笔记详情共用
     */
    @Override
    public void onRightClick() {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra(ExtraKeys.NOTE_ID, -1L);    //笔记id指定为-1，表示新建笔记
        intent.putExtra(ExtraKeys.SUBJECT_ID,
                getIntent().getLongExtra(ExtraKeys.SUBJECT_ID,
                        -1L));     //如果前一个页面传了课程id，则继续传下去，否则传-1，表示未指定课程
       startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            noteChanged = true;
            refreshShowNote(getIntent().getLongExtra(ExtraKeys.SUBJECT_ID, -1L));
        }
    }

    @Override
    public void onBackPressed() {
        if (noteChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }

    /**
     * 查看笔记详情，与新建笔记进入同一个页面
     * @param position
     */
    @OnItemClick(R.id.noteLv)
    void click_note_item(int position) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra(ExtraKeys.NOTE_ID,
                ((NoteEntity) ((NoteAdapter) listView.getAdapter()).getItem(position))
                        .getId());  //只传笔记ID
        startActivityForResult(intent, 0);
    }

    /**
     * 刷新笔记列表
     */
    private void refreshShowNote(long subjectId) {
        List<NoteEntity> data;
        if (subjectId == -1L)
            data = noteManager.getAll();
        else
            data = noteManager.getDataBySubjectId(subjectId);
        if (Verifier.isEffectiveList(data)) {
            listView.setAdapter(new NoteAdapter(this, data));
        }
        else {
            showToast("暂无笔记");
        }
    }
}
