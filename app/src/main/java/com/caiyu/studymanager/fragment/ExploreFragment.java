package com.caiyu.studymanager.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.activity.ClassDetailActivity;
import com.caiyu.studymanager.activity.ClassSetActivity;
import com.caiyu.studymanager.activity.ForumActivity;
import com.caiyu.studymanager.activity.NoteDetailActivity;
import com.caiyu.studymanager.activity.NoteListActivity;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 渝 on 2016/3/9.
 */
public class ExploreFragment extends BaseFragment {

    @Bind(R.id.gridView)
    GridView gridView;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_explore;
    }

    @Override
    public void afterViewCreated() {
        setTitle(getString(R.string.title_find));
        initGrid();
    }

    private void initGrid() {
        gridView.setAdapter(new GridAdapter(getActivity()));
    }

    private static class GridAdapter extends BaseAdapter  {

        private Context mContext;
        private List<Map<String, Object>> data;

        public GridAdapter(Context context) {
            mContext = context;
            data = new ArrayList<>();

            Map<String, Object> map = new HashMap<>();
            map.put("img", R.mipmap.note);
            map.put("txt", "查看笔记");
            map.put("listener", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoteListActivity.class);
                    mContext.startActivity(intent);
                }
            });
            data.add(map);

            map = new HashMap<>();
            map.put("img", R.mipmap.classroom);
            map.put("txt", "空余教室");
            map.put("listener", null);
            data.add(map);

            map = new HashMap<>();
            map.put("img", R.mipmap.steal_class);
            map.put("txt", "蹭课");
            map.put("listener", null);
            data.add(map);

            map = new HashMap<>();
            map.put("img", R.mipmap.auto_silence);
            map.put("txt", "自动静音");
            map.put("listener", null);
            data.add(map);

            map = new HashMap<>();
            map.put("img", R.mipmap.people);
            map.put("txt", "交流区");
            map.put("listener", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ForumActivity.class));
                }
            });
            data.add(map);

            map = new HashMap<>();
            map.put("img", R.mipmap.study);
            map.put("txt", "学霸模式");
            map.put("listener", null);
            data.add(map);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_explore, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                holder.textView = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            Map<String, Object> map = getItem(position);
            holder.imageView.setImageResource((int) map.get("img"));
            holder.textView.setText((String) map.get("txt"));
            convertView.setOnClickListener((View.OnClickListener) map.get("listener"));
            return convertView;
        }

        private static class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}
