package com.caiyu.studymanager.manager;

import com.caiyu.dao.SubjectDao;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.studymanager.common.Verifier;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 渝 on 2016/1/30.
 */
public class SubjectManager implements IDaoManager<SubjectEntity>{

    private static SubjectManager instance;
    private SubjectDao subjectDao;

    private SubjectManager() {
        try {
            subjectDao = DaoLoader.getDaoSession().getSubjectDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDao() {
        subjectDao = DaoLoader.getDaoSession().getSubjectDao();
    }

    public static SubjectManager getInstance() {
        if (instance == null)
            instance = new SubjectManager();
        return instance;
    }

    @Override
    public void addData(SubjectEntity entity) {
        if (subjectDao != null && entity != null) {
            subjectDao.insertOrReplace(entity);
        }
    }

    @Override
    public void deleteByKey(long id) {
        if(subjectDao != null) {
            subjectDao.deleteByKey(id);
        }
    }

    @Override
    public SubjectEntity getDataById(long id) {
        if(subjectDao != null) {
            return subjectDao.load(id);
        }
        return null;
    }

    @Override
    public List<SubjectEntity> getAll() {
        List<SubjectEntity> list;
        if(subjectDao != null) {
            list = subjectDao.loadAll();
            return list;
        }
        return null;
    }

    @Override
    public boolean hasKey(long id) {
        if(subjectDao == null) {
            return false;
        }
        QueryBuilder<SubjectEntity> qb = subjectDao.queryBuilder();
        qb.where(SubjectDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if(subjectDao == null) {
            return 0;
        }

        QueryBuilder<SubjectEntity> qb = subjectDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if(subjectDao != null) {
            subjectDao.deleteAll();
        }
    }

    /**
     * 通过课程名称获取课程ID
     * @param subjectName
     * @return
     *    对应的课程ID，如果找不到该课程返回-1
     */
    public long getIdByName(String subjectName) {
        QueryBuilder<SubjectEntity> qb = subjectDao.queryBuilder();
        qb.where(SubjectDao.Properties.Name.eq(subjectName));
        List<SubjectEntity> list = qb.list();
        if (Verifier.isEffectiveList(list))
            return list.get(0).getId();
        else
            return -1L;
    }

    public void update(SubjectEntity entity) {
        if (subjectDao != null)
            subjectDao.update(entity);
    }

}

