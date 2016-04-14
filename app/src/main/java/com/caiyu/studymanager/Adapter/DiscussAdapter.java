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
        sdf = new SimpleDateFormat("yyyy年MM月dd日");
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
            holder.authorTv = (TextView) convertView.findViewById(R.id.authorTv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        DiscussBean discussBean = (DiscussBean) getItem(position);
        holder.authorTv.setText(discussBean.getAuthor());
        String replyStr = "";
        if (Verifier.isEffectiveStr(discussBean.getReplyTo())) {
            replyStr = String.format("回复 %1$s：", discussBean.getReplyTo());
        }
        holder.contentTv.setText(replyStr + discussBean.getContent());
        holder.timeTv.setText(sdf.format(new Date(discussBean.getTime())));
        return convertView;
    }

    class ViewHolder {
        TextView authorTv;
        TextView contentTv;
        TextView timeTv;
    }
}

