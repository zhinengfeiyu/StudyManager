package com.caiyu.studymanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.caiyu.studymanager.R;

/**
 * Created by 渝 on 2016/4/28.
 */
public class EmptyView extends FrameLayout {

    private ImageView imageView;

    private TextView textView;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.view_empty, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.text);
        imageView.setImageResource(R.mipmap.sad2);
        textView.setText("暂无内容");
    }

    public void setImageResource(int resId) {
        imageView.setImageResource(resId);
    }

    public void setText(CharSequence str) {
        textView.setText(str);
    }

}
