package com.caiyu.dao;

/**
 * Created by 渝 on 2016/2/20.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.caiyu.entity.SubjectEntity;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table subject.
 */
public class SubjectDao extends AbstractDao<SubjectEntity, Long> {

    public static final String TABLENAME = "subject";

    /**
     * Properties of entity SubjectEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
    };


    public SubjectDao(DaoConfig config) {
        super(config);
    }

    public SubjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'subject' (" + //
                "'ID' INTEGER PRIMARY KEY," + // 0: id
                "'NAME' TEXT);"); // 1: name
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'subject'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SubjectEntity entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public SubjectEntity readEntity(Cursor cursor, int offset) {
        SubjectEntity entity = new SubjectEntity(
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // name
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SubjectEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SubjectEntity entity, long rowId) {
        return entity.getId();
    }

    /** @inheritdoc */
    @Override
    public Long getKey(SubjectEntity entity) {
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
