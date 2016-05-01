package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.TopicBean;
import com.caiyu.studymanager.constant.ExtraKeys;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/4/29.
 */
public class TaskListActivity extends BaseActivity {

    @Bind(R.id.taskListView)
    ListView taskListView;

    private List<RemindInfoDO> remindList;

    @Override
    public int getContentViewId() {
        return R.layout.activity_task_list;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_study_pattern));
        remindList = new ArrayList<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            RemindInfoDO remindInfoDO = new RemindInfoDO();
            remindInfoDO.hour = intent.getIntExtra(ExtraKeys.HOUR, 0);
            remindInfoDO.minute = intent.getIntExtra(ExtraKeys.MINUTE, 0);
            remindInfoDO.remindContent = intent.getStringExtra(ExtraKeys.REMIND_CONTENT);
            remindList.add(remindInfoDO);
            refreshShowList();
        }
    }

    @OnClick(R.id.addTaskBtn)
    void click_add_task() {
        Intent intent = new Intent(this, TaskSetActivity.class);
        startActivityForResult(intent, 0);
    }

    private void refreshShowList() {
        taskListView.setAdapter(new RemindAdapter());
    }

    class RemindAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return remindList.size();
        }

        @Override
        public Object getItem(int position) {
            return remindList.get(position);
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
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            RemindInfoDO remindInfoDO = (RemindInfoDO) getItem(position);
            holder.timeTv.setText(remindInfoDO.hour+"："+remindInfoDO.minute);
            holder.taskTv.setText(remindInfoDO.remindContent);
            return convertView;
        }

        class ViewHolder {
            TextView timeTv;
            TextView taskTv;
        }
    }

    class RemindInfoDO {
        int hour;
        int minute;
        String remindContent;
    }
}
