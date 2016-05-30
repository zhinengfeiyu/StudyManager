package com.caiyu.studymanager.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.caiyu.entity.TaskEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.TopicBean;
import com.caiyu.studymanager.common.Resolver;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.manager.TaskManager;
import com.caiyu.studymanager.receiver.TaskRemindReceiver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 渝 on 2016/4/29.
 */
public class TaskListActivity extends BaseActivity {

    @Bind(R.id.taskListView)
    ListView taskListView;

    private List<TaskEntity> taskList;
    private TaskManager taskManager = TaskManager.getInstance();
    private MediaPlayer mediaPlayer;

    @Override
    public int getContentViewId() {
        return R.layout.activity_task_list;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_study_pattern));
        taskList = taskManager.getAll();
        refreshShowList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setHour(intent.getIntExtra(ExtraKeys.HOUR, 0));
            taskEntity.setMinute(intent.getIntExtra(ExtraKeys.MINUTE, 0));
            taskEntity.setTaskInfo(intent.getStringExtra(ExtraKeys.REMIND_CONTENT));
            taskEntity.setVoicePath(intent.getStringExtra(ExtraKeys.VOICE_PATH));
            addCurrentData(taskEntity);
            refreshDbData();
            refreshShowList();
            addAlarm(taskEntity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @OnClick(R.id.addTaskBtn)
    void click_add_task() {
        Intent intent = new Intent(this, TaskSetActivity.class);
        intent.putExtra(ExtraKeys.NEXT_INDEX, taskList.size());
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.resetBtn)
    void click_reset() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        for (TaskEntity taskEntity : taskList) {
            if (!Verifier.isEffectiveStr(taskEntity.getVoicePath()))
                continue;
            Intent intent = new Intent(this, TaskRemindReceiver.class);
            intent.putExtra(ExtraKeys.VOICE_PATH, taskEntity.getVoicePath());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskList.indexOf(taskEntity),
                                                intent, 0);
            alarmManager.cancel(pendingIntent);

            String voiceFileName = taskEntity.getVoicePath();
            File file = new File(voiceFileName);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
        taskList.clear();
        taskManager.deleteAll();
        refreshShowList();
    }

    @OnItemClick(R.id.taskListView)
    void click_list_item(int position) {
        String voicePath = taskList.get(position).getVoicePath();
        if (Verifier.isEffectiveStr(voicePath)) {
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

    private void addCurrentData(TaskEntity taskEntity) {
        int totalMinutes = taskEntity.getHour() * 60 + taskEntity.getMinute();
        int index = 0;
        for (;;) {
            if (index >= taskList.size())
                break;
            TaskEntity te = taskList.get(index);
            if (te.getHour() * 60 + te.getMinute() < totalMinutes)
                index++;
            else
                break;
        }
        taskList.add(index, taskEntity);
    }

    private void refreshDbData() {
        taskManager.deleteAll();
        for (TaskEntity taskEntity : taskList) {
            taskManager.addData(taskEntity);
        }
    }

    private void refreshShowList() {
        taskListView.setAdapter(new RemindAdapter());
    }

    private void addAlarm(TaskEntity taskEntity) {
        if (!Verifier.isEffectiveStr(taskEntity.getVoicePath()))
            return;
        Calendar calendar = Calendar.getInstance();
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        int curTotalMinutes = curHour * 60 + curMinute;
        int targetMinutes = taskEntity.getHour() * 60 + taskEntity.getMinute();
        if (curTotalMinutes >= targetMinutes)
            return;
        int passedTimeMillies = (targetMinutes - curTotalMinutes) * 60 * 1000;
        Intent intent = new Intent(this, TaskRemindReceiver.class);
        intent.putExtra(ExtraKeys.VOICE_PATH, taskEntity.getVoicePath());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskList.indexOf(taskEntity)
                                                                                        , intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + passedTimeMillies, pendingIntent);

        int passedHours = Resolver.getHours(targetMinutes - curTotalMinutes);
        int passedMinutes = Resolver.getMinutes(targetMinutes - curTotalMinutes);
        StringBuilder sb = new StringBuilder();
        if (passedHours > 0) {
            sb.append(passedHours);
            sb.append("小时");
        }
        if (passedMinutes > 0) {
            sb.append(passedMinutes);
            sb.append("分钟");
        }
        sb.append("后语音提醒");
        String toastStr = sb.toString();
        showToast(toastStr);
    }

    class RemindAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public TaskEntity getItem(int position) {
            return taskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(TaskListActivity.this).inflate(R.layout.item_task, null);
            }
            ViewHolder holder;
            if (convertView.getTag() == null) {
                holder = new ViewHolder();
                holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
                holder.taskTv = (TextView) convertView.findViewById(R.id.taskTv);
                holder.voiceImg = (ImageView) convertView.findViewById(R.id.voiceImg);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            TaskEntity taskEntity = getItem(position);
            holder.timeTv.setText(doubleStr(taskEntity.getHour())+"："+doubleStr(taskEntity.getMinute()));
            holder.taskTv.setText(taskEntity.getTaskInfo());
            if (!Verifier.isEffectiveStr(taskEntity.getVoicePath())) {
                holder.voiceImg.setVisibility(View.GONE);
            }
            return convertView;
        }

        private String doubleStr(int num) {
            if (num < 10) {
                return "0" + num;
            }
            else {
                return "" + num;
            }
        }

        class ViewHolder {
            TextView timeTv;
            TextView taskTv;
            ImageView voiceImg;
        }
    }

}
