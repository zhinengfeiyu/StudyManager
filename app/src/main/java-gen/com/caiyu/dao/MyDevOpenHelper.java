package com.caiyu.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 渝 on 2016/1/30.
 */
public class MyDevOpenHelper extends DaoMaster.OpenHelper {

    public MyDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 2:
                //创建新表，注意createTable()是静态方法
                ClassTimeDao.createTable(db, true);
                db.execSQL("ALTER TABLE 'class_table' ADD 'SUBJECT_ID' INTEGER");
                SubjectDao.createTable(db, true);
                NoteDao.createTable(db, true);
                TeacherDao.createTable(db, true);
                // 加入新字段
                // db.execSQL("ALTER TABLE 'moments' ADD 'audio_path' TEXT;");

                break;
            case 3:
                db.execSQL("ALTER TABLE 'class_table' ADD 'SUBJECT_ID' INTEGER");
                SubjectDao.createTable(db, true);
                NoteDao.createTable(db, true);
                TeacherDao.createTable(db, true);
                break;
            case 4:
                TaskDao.createTable(db, true);
                break;
        }
    }
}
