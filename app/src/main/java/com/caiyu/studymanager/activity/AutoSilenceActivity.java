package com.caiyu.studymanager.activity;

import android.os.Bundle;
import android.widget.ToggleButton;

import com.caiyu.studymanager.R;

import butterknife.Bind;

/**
 * Created by 渝 on 2016/4/16.
 */
public class AutoSilenceActivity extends BaseActivity {

    @Bind(R.id.toggleBtn)
    ToggleButton toggleBtn;

    @Override
    public int getContentViewId() {
        return R.layout.activity_auto_silence;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_auto_silence));
    }

}
