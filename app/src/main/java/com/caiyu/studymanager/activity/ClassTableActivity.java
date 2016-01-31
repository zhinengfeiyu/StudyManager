package com.caiyu.studymanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.GridView;

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

    @Bind(R.id.tableGv)
    GridView tableGv;

    private ClassTableManager tableManager = ClassTableManager.getInstance();

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
        refreshShowTable();
    }

    private void refreshShowTable() {
        List<ClassTableEntity> classList = tableManager.getAll();
        if (tableGv.getAdapter() == null) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tableGv.getLayoutParams();
            int tableWidth = screenWidth - params.leftMargin - params.rightMargin;
            int tableHeight = screenHeight - params.topMargin - params.bottomMargin;
            tableGv.setAdapter(new ClassTableAdapter(this, classList, tableWidth, tableHeight));
        }
        else {
            ClassTableAdapter adapter = (ClassTableAdapter) tableGv.getAdapter();
            adapter.setData(classList);
            adapter.notifyDataSetChanged();
        }
    }

}
