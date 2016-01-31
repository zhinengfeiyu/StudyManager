package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.Adapter.ClassTableAdapter;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.manager.ClassTableManager;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 渝 on 2016/1/23.
 */
public class ClassTableActivity extends BaseActivity {

    @Bind(R.id.weekdayTitleLayout)
    LinearLayout titleLayout;
    @Bind(R.id.timeLayout)
    LinearLayout timeLayout;
    @Bind(R.id.tableGv)
    GridView tableGv;

    private ClassTableManager tableManager = ClassTableManager.getInstance();

    private int screenWidth;
    private int screenHeight;

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_table;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode) {
            case Activity.RESULT_OK:
                refreshShowTable();
                break;
        }
    }

    @Override
    public void afterViewCreated() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        int totalHeight = metrics.heightPixels;
//        Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        screenHeight = totalHeight - contentTop;
        initWeekdayTitle();
        refreshShowTime();
        refreshShowTable();
    }

    private void initWeekdayTitle() {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
        ViewGroup.LayoutParams childParams = new ViewGroup.LayoutParams(
                (screenWidth - layoutParams.leftMargin) / 5, layoutParams.height);
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(childParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setText(tableManager.getShowWeekday(i + 1));
            if (i % 2 == 0)
                textView.setBackgroundColor(getResources().getColor(R.color.green_light));
            else
                textView.setBackgroundColor(getResources().getColor(R.color.red_light));
            titleLayout.addView(textView);
        }
    }

    private void refreshShowTime() {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) timeLayout.getLayoutParams();
        ViewGroup.LayoutParams childParams = new ViewGroup.LayoutParams(
                layoutParams.width, (screenHeight - layoutParams.topMargin) / 5);
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(childParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setText("00:00");
            if (i % 2 == 0)
                textView.setBackgroundColor(getResources().getColor(R.color.green_light));
            else
                textView.setBackgroundColor(getResources().getColor(R.color.red_light));
            timeLayout.addView(textView);
        }
    }

    private void refreshShowTable() {
        List<ClassTableEntity> classList = tableManager.getAll();
        if (tableGv.getAdapter() == null) {
            int tableWidth = screenWidth - timeLayout.getLayoutParams().width;
            int tableHeight = screenHeight - titleLayout.getLayoutParams().height;
            tableGv.setAdapter(new ClassTableAdapter(this, classList, tableWidth, tableHeight));
        }
        else {
            ClassTableAdapter adapter = (ClassTableAdapter) tableGv.getAdapter();
            adapter.setData(classList);
            adapter.notifyDataSetChanged();
        }
    }

}
