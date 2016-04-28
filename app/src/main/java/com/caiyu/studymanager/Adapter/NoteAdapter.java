package com.caiyu.studymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caiyu.entity.NoteEntity;
import com.caiyu.studymanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 渝 on 2016/3/14.
 */
public class NoteAdapter extends BaseAdapter {

    private Context mContext;
    private List<NoteEntity> data;

    private SimpleDateFormat sdf;

    public NoteAdapter(Context context, List<NoteEntity> data) {
        this.mContext = context;
        this.data = data;
        sdf = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_note, null);
        }
        ViewHolder holder;
        if (convertView.getTag() == null) {
            holder = new ViewHolder();
            holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        NoteEntity noteEntity = (NoteEntity) getItem(position);
        holder.timeTv.setText(sdf.format(new Date(noteEntity.getLastEditTime())));
        holder.contentTv.setText(noteEntity.getContent());
        return convertView;
    }

    class ViewHolder {
        TextView timeTv;
        TextView contentTv;
    }
}
