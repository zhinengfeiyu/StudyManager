package com.caiyu.studymanager.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.provider.Settings;
import android.widget.Toast;

import com.caiyu.studymanager.bean.ClassOffsetBean;
import com.caiyu.studymanager.common.Resolver;
import com.caiyu.studymanager.constant.PrefKeys;

/**
 * Created by 渝 on 2016/5/22.
 */
public class AutoSilenceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction()==null) {
            changeMode(context);
            Toast.makeText(context, "接收到广播：null", Toast.LENGTH_LONG).show();
        }

        else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (context.getSharedPreferences(PrefKeys.TABLE_SETTING, 0).
                                    getBoolean(PrefKeys.AUTO_SILENCE_SET, false) == true) {
                changeMode(context);
                Toast.makeText(context, "开机启动自动静音", Toast.LENGTH_LONG).show();
            }
        }

        else if (intent.getAction().equals("auto_silence_mode_change")) {   //通过切换打开或者关闭
            if (context.getSharedPreferences(PrefKeys.TABLE_SETTING, 0).
                                    getBoolean(PrefKeys.AUTO_SILENCE_SET, false) == false) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0
                        , new Intent(context, AutoSilenceReceiver.class), 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(context, "自动静音取消", Toast.LENGTH_LONG).show();
            }
            else {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0
                        , new Intent(context, AutoSilenceReceiver.class), 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                changeMode(context);
                Toast.makeText(context, "自动静音重置", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void changeMode(Context context) {
        ClassOffsetBean classOffsetBean = Resolver.getCurrentOrNextClass();
        if (classOffsetBean.isInClass == true) {
            switchToSilentMode(context);
        }
        else {
            switchToNoisyMode(context);
        }
        long passedMilliseconds = (((classOffsetBean.dayOffset * 24 + classOffsetBean.hoursOffset) * 60
                                    + classOffsetBean.minutesOffset) * 60 * 1000) + System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0
                                , new Intent(context, AutoSilenceReceiver.class), 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, passedMilliseconds, pendingIntent);
    }

    private void switchToNoisyMode(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    context.getSharedPreferences(PrefKeys.TABLE_SETTING, 0).getInt(PrefKeys.LAST_VOLUMN,
                            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2), 0);
            Toast.makeText(context, "自动切换到正常音量模式", Toast.LENGTH_LONG).show();
        }
    }

    private void switchToSilentMode(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
            SharedPreferences.Editor editor = context.getSharedPreferences(PrefKeys.TABLE_SETTING, 0).edit();
            editor.putInt(PrefKeys.LAST_VOLUMN, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            editor.commit();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            Toast.makeText(context, "自动切换到振动模式", Toast.LENGTH_LONG).show();
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }
}
