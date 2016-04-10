package com.caiyu.studymanager.fragment;

import android.content.Intent;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.activity.NoteListActivity;

import butterknife.OnClick;

/**
 * Created by 渝 on 2016/3/9.
 */
public class ExploreFragment extends BaseFragment {

    @Override
    public int getContentViewId() {
        return R.layout.fragment_explore;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_find));
    }

    @OnClick(R.id.noteBtn)
    void click_note() {
        Intent intent = new Intent(getActivity(), NoteListActivity.class);
        startActivity(intent);
    }
}
