package com.caiyu.studymanager.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.caiyu.studymanager.R;
import com.caiyu.studymanager.fragment.ClassFragment;
import com.caiyu.studymanager.fragment.ExploreFragment;
import com.caiyu.studymanager.fragment.MeFragment;

/**
 * Created by 渝 on 2016/2/26.
 */
public class TabActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    //布局填充器
    private LayoutInflater mLayoutInflater;

    //Fragment数组界面
    private Class<?> mFragmentArray[] = {
            ClassFragment.class, ExploreFragment.class, MeFragment.class};

    //存放图片数组
    private int mImageArray[] = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    //选项卡文字
    private String mTextArray[] = {"课表", "发现", "我"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        initView();
    }

    private void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content);
        // 得到fragment的个数
        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
        }
    }

    /**
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.item_tab, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.image);
//        imageView.setImageResource(mImageArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(mTextArray[index]);
        return view;
    }
}
