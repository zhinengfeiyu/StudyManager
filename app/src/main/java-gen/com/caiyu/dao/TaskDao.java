package com.caiyu.dao;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.caiyu.entity.TaskEntity;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by 渝 on 2016/5/27.
 */
public class TaskDao extends AbstractDao<TaskEntity, Long> {

    public static final String TABLENAME = "task";

    /**
     * Properties of entity TaskEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Hour = new Property(1, Integer.class, "hour", false, "HOUR");
        public final static Property Minute = new Property(2, Integer.class, "minute", false, "MINUTE");
        public final static Property Task_info = new Property(3, String.class, "task_info", false, "TASK_INFO");
        public final static Property Voice_path = new Property(4, String.class, "voice_path", false, "VOICE_PATH");
    };


    public TaskDao(DaoConfig config) {
        super(config);
    }

    public TaskDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'task' (" + //
                "'ID' INTEGER PRIMARY KEY," + // 0: id
                "'HOUR' INTEGER," + // 1: hour
                "'MINUTE' INTEGER," + // 2: minute
                "'TASK_INFO' TEXT," + // 3: task_info
                "'VOICE_PATH' TEXT);"); // 4: voice_path
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'task'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TaskEntity entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        Integer hour = entity.getHour();
        if (hour != null) {
            stmt.bindLong(2, hour);
        }

        Integer minute = entity.getMinute();
        if (minute != null) {
            stmt.bindLong(3, minute);
        }

        String taskInfo = entity.getTaskInfo();
        if (taskInfo != null) {
            stmt.bindString(4, taskInfo);
        }

        String voicePath = entity.getVoicePath();
        if (voicePath != null) {
            stmt.bindString(5, voicePath);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public TaskEntity readEntity(Cursor cursor, int offset) {
        TaskEntity entity = new TaskEntity(
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // hour
                cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // minute
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // taskInfo
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // voicePath
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TaskEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHour(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setMinute(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setTaskInfo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setVoicePath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TaskEntity entity, long rowId) {
        return entity.getId();
    }

    /** @inheritdoc */
    @Override
    public Long getKey(TaskEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}
