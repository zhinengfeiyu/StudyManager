package com.caiyu.studymanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.DiscussBean;
import com.caiyu.studymanager.bean.TopicBean;
import com.caiyu.studymanager.common.Verifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 渝 on 2016/2/27.
 */
public class DiscussAdapter extends BaseAdapter {

    private Context context;

    private List<DiscussBean> discussList;

    private SimpleDateFormat sdf;

    public DiscussAdapter(Context context, List<DiscussBean> discussList) {
        this.context = context;
        this.discussList = discussList;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public List<DiscussBean> getData() {
        return discussList;
    }

    public void setData(List<TopicBean> topicList) {
        this.discussList = discussList;
    }

    @Override
    public int getCount() {
        return discussList.size();
    }

    @Override
    public Object getItem(int position) {
        return discussList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_discuss, null);
        }
        ViewHolder holder;
        if (convertView.getTag() == null) {
            holder = new ViewHolder();
            holder.replyToTv = (TextView) convertView.findViewById(R.id.replyToTv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            holder.authorTv = (TextView) convertView.findViewById(R.id.authorTv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        DiscussBean discussBean = (DiscussBean) getItem(position);
        if (Verifier.isEffectiveStr(discussBean.getReplyTo())) {
            holder.replyToTv.setVisibility(View.VISIBLE);
            holder.replyToTv.setText(String.format("回复%1$s：", discussBean.getReplyTo()));
        }
        else {
            holder.replyToTv.setText("");
            holder.replyToTv.setVisibility(View.GONE);
        }
        holder.contentTv.setText(discussBean.getContent());
        holder.authorTv.setText(discussBean.getAuthor());
        holder.timeTv.setText(sdf.format(new Date(discussBean.getTime())));
        return convertView;
    }

    class ViewHolder {
        TextView replyToTv;
        TextView contentTv;
        TextView authorTv;
        TextView timeTv;
    }
}

