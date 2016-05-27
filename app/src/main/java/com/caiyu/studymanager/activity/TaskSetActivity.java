package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/4/29.
 */
public class TaskSetActivity extends BaseActivity {

    @Bind(R.id.timePicker)
    TimePicker timePicker;
    @Bind(R.id.remindInfoEt)
    EditText remindInfoEt;
    @Bind(R.id.recordVoiceBtn)
    Button recordVoiceBtn;

    private String voiceFileName;
    private MediaPlayer mediaPlayer;

    @Override
    public int getContentViewId() {
        return R.layout.activity_task_set;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_task_set));
        timePicker.setIs24HourView(true);
        recordVoiceBtn.setOnTouchListener(new TouchRecordListener());
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm() {
        showToast("任务添加成功");
        Intent intent = new Intent();
        intent.putExtra(ExtraKeys.HOUR, timePicker.getCurrentHour());
        intent.putExtra(ExtraKeys.MINUTE, timePicker.getCurrentMinute());
        intent.putExtra(ExtraKeys.REMIND_CONTENT, remindInfoEt.getText().toString());
        intent.putExtra(ExtraKeys.VOICE_PATH, Verifier.isEffectiveStr(voiceFileName) ? voiceFileName : "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private class TouchRecordListener implements View.OnTouchListener {
        private final int BEFORE_RECORD = 1;
        private final int RECORDING = 2;
        private final int AFTER_RECORD = 3;
        private final int PLAYING = 4;
        private int btnStatus = BEFORE_RECORD;
        private MediaRecorder mediaRecorder;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Button recordBtn = (Button) v;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recordBtn.setBackgroundColor(0x66ff0000);
                if (btnStatus == BEFORE_RECORD) {
                    btnStatus = RECORDING;
                    recordBtn.setText("松开结束录音");
                    initRecorder();
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                recordBtn.setBackgroundColor(0x6600ff00);
                if (btnStatus == RECORDING) {
                    btnStatus = AFTER_RECORD;
                    recordBtn.setText("播放录制的提醒音");
                    mediaRecorder.stop();// 停止刻录
                    // recorder.reset(); // 重新启动MediaRecorder.
                    mediaRecorder.release(); // 刻录完成一定要释放资源
                    initPlayer();
                }
                else if (btnStatus == AFTER_RECORD){
                    btnStatus = PLAYING;
                    recordBtn.setText("停止播放录音");
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }
                else if (btnStatus == PLAYING) {
                    btnStatus = AFTER_RECORD;
                    recordBtn.setText("播放录制的提醒音");
                    mediaPlayer.pause();
                }
            }
            return true;
        }

        private void initRecorder() {
            mediaRecorder = new MediaRecorder();// new出MediaRecorder对象
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置MediaRecorder的音频源为麦克风
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            // 设置MediaRecorder录制的音频格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置MediaRecorder录制音频的编码为amr.
            String fileName = "study_manager_task_remind_" + getIntent().getIntExtra(ExtraKeys.NEXT_INDEX, 0);
            voiceFileName = String.format("/sdcard/%1$s.amr", fileName);
            mediaRecorder.setOutputFile(voiceFileName);
            // 设置录制好的音频文件保存路径
            try {
                mediaRecorder.prepare();// 准备录制
                mediaRecorder.start();// 开始录制
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void initPlayer() {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(voiceFileName);
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        recordVoiceBtn.setText("播放录制的提醒音");
                        btnStatus = AFTER_RECORD;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
