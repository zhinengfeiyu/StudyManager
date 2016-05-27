package com.caiyu.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.entity.ClassTimeEntity;
import com.caiyu.entity.NoteEntity;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.entity.TaskEntity;
import com.caiyu.entity.TeacherEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig classTableDaoConfig;
    private final DaoConfig classTimeDaoConfig;
    private final DaoConfig subjectDaoConfig;
    private final DaoConfig noteDaoConfig;
    private final DaoConfig teacherDaoConfig;
    private final DaoConfig taskDaoConfig;

    private final ClassTableDao classTableDao;
    private final ClassTimeDao classTimeDao;
    private final SubjectDao subjectDao;
    private final NoteDao noteDao;
    private final TeacherDao teacherDao;
    private final TaskDao taskDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        classTableDaoConfig = daoConfigMap.get(ClassTableDao.class).clone();
        classTimeDaoConfig = daoConfigMap.get(ClassTimeDao.class).clone();
        subjectDaoConfig = daoConfigMap.get(SubjectDao.class).clone();
        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        teacherDaoConfig = daoConfigMap.get(TeacherDao.class).clone();
        taskDaoConfig = daoConfigMap.get(TaskDao.class).clone();
        classTableDaoConfig.initIdentityScope(type);
        classTimeDaoConfig.initIdentityScope(type);
        subjectDaoConfig.initIdentityScope(type);
        noteDaoConfig.initIdentityScope(type);
        teacherDaoConfig.initIdentityScope(type);
        taskDaoConfig.initIdentityScope(type);

        classTableDao = new ClassTableDao(classTableDaoConfig, this);
        classTimeDao = new ClassTimeDao(classTimeDaoConfig, this);
        subjectDao = new SubjectDao(subjectDaoConfig, this);
        noteDao = new NoteDao(noteDaoConfig, this);
        teacherDao = new TeacherDao(teacherDaoConfig, this);
        taskDao = new TaskDao(taskDaoConfig, this);

        registerDao(ClassTableEntity.class, classTableDao);
        registerDao(ClassTimeEntity.class, classTimeDao);
        registerDao(SubjectEntity.class, subjectDao);
        registerDao(NoteEntity.class, noteDao);
        registerDao(TeacherEntity.class, teacherDao);
        registerDao(TaskEntity.class, taskDao);
    }

    public void clear() {
        classTableDaoConfig.getIdentityScope().clear();
        classTimeDaoConfig.getIdentityScope().clear();
        subjectDaoConfig.getIdentityScope().clear();
        noteDaoConfig.getIdentityScope().clear();
        teacherDaoConfig.getIdentityScope().clear();
        taskDaoConfig.getIdentityScope().clear();
    }

    public ClassTableDao getClassTableDao() {
        return classTableDao;
    }

    public ClassTimeDao getClassTimeDao() {
        return classTimeDao;
    }

    public SubjectDao getSubjectDao() {
        return subjectDao;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public TeacherDao getTeacherDao() {
        return teacherDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }
}
