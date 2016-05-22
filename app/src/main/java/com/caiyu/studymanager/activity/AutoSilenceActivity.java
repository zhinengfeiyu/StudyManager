package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.Toast;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.constant.PrefKeys;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/4/16.
 */
public class AutoSilenceActivity extends BaseActivity {

    @Bind(R.id.checkbox)
    CheckBox checkBox;

    @Override
    public int getContentViewId() {
        return R.layout.activity_auto_silence;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_auto_silence));
        checkBox.setChecked(getSharedPreferences(PrefKeys.TABLE_SETTING, 0).
                                getBoolean(PrefKeys.AUTO_SILENCE_SET, false));
    }

    @OnClick(R.id.checkbox)
    void click_check() {
        SharedPreferences.Editor editor = getSharedPreferences(PrefKeys.TABLE_SETTING, 0).edit();
        editor.putBoolean(PrefKeys.AUTO_SILENCE_SET, checkBox.isChecked());
        editor.commit();
        Toast.makeText(getApplicationContext(), "自动静音设置已被修改", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent("auto_silence_mode_change"));
    }

}
