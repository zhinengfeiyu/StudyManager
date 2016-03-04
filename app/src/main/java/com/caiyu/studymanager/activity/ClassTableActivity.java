package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.Adapter.ClassTableAdapter;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.manager.ClassTableManager;
import com.caiyu.studymanager.manager.ClassTimeManager;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

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
    private ClassTimeManager timeManager = ClassTimeManager.getInstance();

    public static int REQ_TIME = 1;
    public static int REQ_TABLE = 2;

    private int tableWidth;
    private int tableHeight;

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_table;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_TABLE)
                refreshShowTable();
            else if (requestCode == REQ_TIME)
                refreshShowTime();
        }
    }

    @Override
    public void afterViewCreated() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        tableWidth = screenWidth;
        int screenHeight = metrics.heightPixels;
        //获取状态栏高度
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        tableHeight = screenHeight - statusBarHeight;
        initWeekdayTitle();
        refreshShowTime();
        refreshShowTable();
    }

    @OnClick(R.id.timeLayout)
    void click_class_time() {
        Intent intent = new Intent(this, ClassTimeSetActivity.class);

        startActivityForResult(intent, REQ_TIME);
    }

    private void initWeekdayTitle() {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
        ViewGroup.LayoutParams childParams = new ViewGroup.LayoutParams(
                (tableWidth - layoutParams.leftMargin) / 5, layoutParams.height);
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
        timeLayout.removeAllViews();
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) timeLayout.getLayoutParams();
        ViewGroup.LayoutParams childParams = new ViewGroup.LayoutParams(
                layoutParams.width, (tableHeight - layoutParams.topMargin) / 5);
        List<String> timeList = timeManager.getAllByString();
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(childParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setText(timeList.get(i * 2) + "\n-\n" + timeList.get(i * 2 + 1));
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
            int classContentWidth = tableWidth - timeLayout.getLayoutParams().width;
            int classContentHeight = tableHeight - titleLayout.getLayoutParams().height;
//            tableGv.setAdapter(new ClassTableAdapter(this, classList,classContentWidth, classContentHeight));
        }
        else {
            ClassTableAdapter adapter = (ClassTableAdapter) tableGv.getAdapter();
            adapter.setData(classList);
            adapter.notifyDataSetChanged();
        }
    }

}
