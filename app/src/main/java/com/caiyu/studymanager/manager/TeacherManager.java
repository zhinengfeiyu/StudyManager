package com.caiyu.studymanager.manager;

import com.caiyu.dao.TeacherDao;
import com.caiyu.entity.TeacherEntity;
import com.caiyu.studymanager.common.Verifier;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 渝 on 2016/3/7.
 */
public class TeacherManager implements IDaoManager<TeacherEntity>{

    private static TeacherManager instance;
    private TeacherDao teacherDao;

    private TeacherManager() {
        try {
            teacherDao = DaoLoader.getDaoSession().getTeacherDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDao() {
        teacherDao = DaoLoader.getDaoSession().getTeacherDao();
    }

    public static TeacherManager getInstance() {
        if (instance == null)
            instance = new TeacherManager();
        return instance;
    }

    @Override
    public void addData(TeacherEntity entity) {
        if (teacherDao != null && entity != null) {
            teacherDao.insertOrReplace(entity);
        }
    }

    @Override
    public void deleteByKey(long id) {
        if(teacherDao != null) {
            teacherDao.deleteByKey(id);
        }
    }

    @Override
    public TeacherEntity getDataById(long id) {
        if(teacherDao != null) {
            return teacherDao.load(id);
        }
        return null;
    }

    @Override
    public List<TeacherEntity> getAll() {
        List<TeacherEntity> list;
        if(teacherDao != null) {
            list = teacherDao.loadAll();
            return list;
        }
        return null;
    }

    @Override
    public boolean hasKey(long id) {
        if(teacherDao == null) {
            return false;
        }
        QueryBuilder<TeacherEntity> qb = teacherDao.queryBuilder();
        qb.where(TeacherDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if(teacherDao == null) {
            return 0;
        }

        QueryBuilder<TeacherEntity> qb = teacherDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if(teacherDao != null) {
            teacherDao.deleteAll();
        }
    }

    public TeacherEntity getDataByName(String teacherName) {
        QueryBuilder<TeacherEntity> qb = teacherDao.queryBuilder();
        qb.where(TeacherDao.Properties.Name.eq(teacherName));
        List<TeacherEntity> list = qb.list();
        if (Verifier.isEffectiveList(list))
            return list.get(0);
        return null;
    }

    public long getIdByName(String teacherName) {
        QueryBuilder<TeacherEntity> qb = teacherDao.queryBuilder();
        qb.where(TeacherDao.Properties.Name.eq(teacherName));
        List<TeacherEntity> list = qb.list();
        if (Verifier.isEffectiveList(list))
            return list.get(0).getId();
        else
            return -1L;
    }

    public void update(TeacherEntity entity) {
        if (teacherDao != null)
            teacherDao.update(entity);
    }

}

