package com.caiyu.studymanager.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.caiyu.dao.DaoMaster;
import com.caiyu.dao.DaoSession;
import com.caiyu.dao.MyDevOpenHelper;

/**
 * Created by 渝 on 2016/1/30.
 */
public class DaoLoader {
    private static final String DATABASE_NAME = "user-%1$d.db";
    private static DaoSession daoSession;

    // 虚方法，可以无实体内容
    public static void init(Context context, int userId) {
        MyDevOpenHelper helper = new MyDevOpenHelper(context, String.format(DATABASE_NAME, userId), null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
        else {
            daoSession = daoMaster.newSession();
            ClassTableManager.getInstance().resetDao();
            ClassTimeManager.getInstance().resetDao();
            NoteManager.getInstance().resetDao();
            SubjectManager.getInstance().resetDao();
            TeacherManager.getInstance().resetDao();
        }
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
