package com.caiyu.studymanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.TopicBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 渝 on 2016/2/27.
 */
public class TopicAdapter extends BaseAdapter {

    private Context context;

    private List<TopicBean> topicList;

    private SimpleDateFormat sdf;

    public TopicAdapter(Context context, List<TopicBean> topicList) {
        this.context = context;
        this.topicList = topicList;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public List<TopicBean> getData() {
        return topicList;
    }

    public void setData(List<TopicBean> topicList) {
        this.topicList = topicList;
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_forum_topic, null);
        }
        ViewHolder holder;
        if (convertView.getTag() == null) {
            holder = new ViewHolder();
            holder.subjectTv = (TextView) convertView.findViewById(R.id.subjectTv);
            holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        TopicBean topicBean = (TopicBean) getItem(position);
        holder.subjectTv.setText(topicBean.getSubjectName());
        holder.titleTv.setText(topicBean.getTitle());
        holder.contentTv.setText(topicBean.getContent());
        return convertView;
    }

    class ViewHolder {
        TextView subjectTv;
        TextView titleTv;
        TextView contentTv;
    }
}
