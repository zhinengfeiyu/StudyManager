package com.caiyu.studymanager.manager;

import com.caiyu.dao.TaskDao;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.entity.TaskEntity;
import com.caiyu.studymanager.common.Verifier;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 渝 on 2016/5/27.
 */
public class TaskManager implements IDaoManager<TaskEntity>{
    private static TaskManager instance;
    private TaskDao taskDao;

    private TaskManager() {
        try {
            taskDao = DaoLoader.getDaoSession().getTaskDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDao() {
        taskDao = DaoLoader.getDaoSession().getTaskDao();
    }

    public static TaskManager getInstance() {
        if (instance == null)
            instance = new TaskManager();
        return instance;
    }

    @Override
    public void addData(TaskEntity entity) {
        if (taskDao != null && entity != null) {
            taskDao.insertOrReplace(entity);
        }
    }

    @Override
    public void deleteByKey(long id) {
        if(taskDao != null) {
            taskDao.deleteByKey(id);
        }
    }

    @Override
    public TaskEntity getDataById(long id) {
        if(taskDao != null) {
            return taskDao.load(id);
        }
        return null;
    }

    @Override
    public List<TaskEntity> getAll() {
        List<TaskEntity> list;
        if(taskDao != null) {
            list = taskDao.loadAll();
            return list;
        }
        return null;
    }

    @Override
    public boolean hasKey(long id) {
        if(taskDao == null) {
            return false;
        }
        QueryBuilder<TaskEntity> qb = taskDao.queryBuilder();
        qb.where(TaskDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if(taskDao == null) {
            return 0;
        }

        QueryBuilder<TaskEntity> qb = taskDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if(taskDao != null) {
            taskDao.deleteAll();
        }
    }

    public void update(TaskEntity entity) {
        if (taskDao != null)
            taskDao.update(entity);
    }

}
