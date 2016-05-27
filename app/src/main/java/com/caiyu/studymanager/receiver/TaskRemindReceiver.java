package com.caiyu.studymanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.caiyu.studymanager.constant.ExtraKeys;

import java.io.IOException;

/**
 * Created by 渝 on 2016/5/27.
 */
public class TaskRemindReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String voicePath = intent.getStringExtra(ExtraKeys.VOICE_PATH);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(voicePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
