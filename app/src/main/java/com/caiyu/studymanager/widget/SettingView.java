package com.caiyu.studymanager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caiyu.studymanager.R;

/**
 * Created by 渝 on 2016/2/28.
 */
public class SettingView extends FrameLayout {

    private int leftTextSize;
    private int rightTextSize;
    private int leftTextColor;
    private int rightTextColor;
    private String leftText;
    private String rightText;
    private TextView leftTv;
    private TextView rightTv;

    public SettingView(Context context) {
        this(context, null);
    }

    public SettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SettingView, defStyle, 0);
        leftTextSize = arr.getDimensionPixelSize(R.styleable.SettingView_leftTextSize, 20);
        rightTextSize = arr.getDimensionPixelSize(R.styleable.SettingView_rightTextSize, 20);
        leftTextColor = arr.getColor(R.styleable.SettingView_leftTextColor,
                            getResources().getColor(R.color.gray_33));
        rightTextColor = arr.getColor(R.styleable.SettingView_rightTextColor,
                getResources().getColor(R.color.gray_99));
        leftText = arr.getString(R.styleable.SettingView_leftText);
        rightText = arr.getString(R.styleable.SettingView_rightText);
        arr.recycle();
        LayoutInflater.from(context).inflate(R.layout.view_setting, this);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        leftTv = (TextView) findViewById(R.id.leftTv);
        rightTv = (TextView) findViewById(R.id.rightTv);
        leftTv.setTextColor(leftTextColor);
        rightTv.setTextColor(rightTextColor);
        leftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
        rightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        leftTv.setText(leftText);
        rightTv.setText(rightText);
    }

    public void setText(String text) {
        rightTv.setText(text);
    }

}
