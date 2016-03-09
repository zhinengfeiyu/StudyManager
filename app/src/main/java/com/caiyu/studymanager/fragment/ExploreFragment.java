package com.caiyu.studymanager.fragment;

import com.caiyu.studymanager.R;

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
}
