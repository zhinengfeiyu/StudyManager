package com.caiyu.studymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.bean.StealDetailBean;
import com.caiyu.studymanager.common.Resolver;

import java.util.List;

/**
 * Created by 渝 on 2016/4/16.
 */
public class StealDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<StealDetailBean> data;

    public StealDetailAdapter(Context context, List<StealDetailBean> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public StealDetailBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_steal_class, null);
        }
        ViewHolder holder;
        if (convertView.getTag() == null) {
            holder = new ViewHolder();
            holder.professionTv = (TextView) convertView.findViewById(R.id.professionTv);
            holder.classOrderTv = (TextView) convertView.findViewById(R.id.classOrderTv);
            holder.classRoomTv = (TextView) convertView.findViewById(R.id.classRoomTv);
            holder.teacherTv = (TextView) convertView.findViewById(R.id.teacherTv);
            holder.weeksTv = (TextView) convertView.findViewById(R.id.weeksTv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        StealDetailBean bean = getItem(position);
        holder.professionTv.setText(bean.profession);
        holder.classOrderTv.setText(Resolver.resolveClassOrder(bean.classOrder));
        holder.classRoomTv.setText(bean.classRoom);
        holder.teacherTv.setText(bean.teacher);
        holder.weeksTv.setText(Resolver.resolveClassWeek(bean.startWeek, bean.endWeek));
        return convertView;
    }

    class ViewHolder {
        TextView professionTv;
        TextView classOrderTv;
        TextView classRoomTv;
        TextView teacherTv;
        TextView weeksTv;
    }
}
