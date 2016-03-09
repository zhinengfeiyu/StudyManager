package com.caiyu.studymanager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caiyu.studymanager.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 渝 on 2016/2/26.
 */
public abstract class BaseFragment extends Fragment {

    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getContentViewId(), container, false);
            ButterKnife.bind(this, rootView);
            initTitle();
            afterViewCreated();
        }
        return rootView;
    }

    protected abstract int getContentViewId();

    protected void afterViewCreated() {}

    protected void initTitle() {
        TextView backTv = (TextView) rootView.findViewById(R.id.commonBackTv);
        if (backTv == null) return;
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    protected void setTitle(CharSequence title) {
        TextView titleTv = (TextView) rootView.findViewById(R.id.commonTitleTv);
        if (titleTv == null) return;
        titleTv.setText(title);
    }

    protected void setTitleRightText(CharSequence rightText) {
        TextView rightTv = (TextView) rootView.findViewById(R.id.commonRightTv);
        if (rightTv == null) return;
        rightTv.setText(rightText);
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick();
            }
        });
    }

    protected void onRightClick() {

    }

    protected void showToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
