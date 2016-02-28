package com.caiyu.studymanager.manager;

import com.caiyu.dao.ClassTimeDao;
import com.caiyu.entity.ClassTimeEntity;
import com.caiyu.studymanager.common.Verifier;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 渝 on 2016/1/30.
 */
public class ClassTimeManager implements IDaoManager<ClassTimeEntity>{

    private static ClassTimeManager instance;
    private ClassTimeDao classTimeDao;

    private ClassTimeManager() {
        try {
            classTimeDao = DaoLoader.getDaoSession().getClassTimeDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDao() {
        classTimeDao = DaoLoader.getDaoSession().getClassTimeDao();
    }

    public static ClassTimeManager getInstance() {
        if (instance == null)
            instance = new ClassTimeManager();
        return instance;
    }

    @Override
    public void addData(ClassTimeEntity entity) {
        if (classTimeDao != null && entity != null) {
            classTimeDao.insertOrReplace(entity);
        }
    }

    @Override
    public void deleteByKey(long id) {
        if(classTimeDao != null) {
            classTimeDao.deleteByKey(id);
        }
    }

    @Override
    public ClassTimeEntity getDataById(long id) {
        if(classTimeDao != null) {
            return classTimeDao.load(id);
        }
        return null;
    }

    @Override
    public List<ClassTimeEntity> getAll() {
        List<ClassTimeEntity> list;
        if(classTimeDao != null) {
            list = classTimeDao.loadAll();
            if (!Verifier.isEffectiveList(list) || list.size() != 5) {
                fixDao();
                list = classTimeDao.loadAll();
            }
            return list;
        }
        return null;
    }

    @Override
    public boolean hasKey(long id) {
        if(classTimeDao == null) {
            return false;
        }

        QueryBuilder<ClassTimeEntity> qb = classTimeDao.queryBuilder();
        qb.where(ClassTimeDao.Properties.Order.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if(classTimeDao == null) {
            return 0;
        }
        QueryBuilder<ClassTimeEntity> qb = classTimeDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if(classTimeDao != null) {
            classTimeDao.deleteAll();
        }
    }

    public void update(ClassTimeEntity entity) {
        if (classTimeDao != null)
            classTimeDao.update(entity);
    }

    /**
     * 获取所有时间点，包括上课和下课
     * @return
     */
    public List<String> getAllByString() {
        List<ClassTimeEntity> originList = getAll();
        List<String> strList = new ArrayList<>(10);
        if (Verifier.isEffectiveList(originList)) {
            for (ClassTimeEntity entity : originList) {
                strList.add(formatTime(entity.getStartHour(), entity.getStartMinute()));
                strList.add(formatTime(entity.getEndHour(), entity.getEndMinute()));
            }
        }
        return strList;
    }

    private void fixDao() {
        if (getTotalCount() != 5) {
            deleteAll();
            addData(new ClassTimeEntity(1, 8, 10, 9, 50));
            addData(new ClassTimeEntity(2, 10, 20, 12, 0));
            addData(new ClassTimeEntity(3, 13, 30, 15, 10));
            addData(new ClassTimeEntity(4, 15, 40, 17, 10));
            addData(new ClassTimeEntity(5, 18, 0, 19, 40));
        }
    }

    /**
     * 将一对整型的时和分数据，转换为XX:XX的格式
     * @param hour
     * @param minute
     * @return
     */
    public String formatTime(int hour, int minute) {
        StringBuilder sb = new StringBuilder(5);
        if (hour < 10) {
            sb.append('0');
        }
        sb.append(hour);
        sb.append(':');
        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute);
        return sb.toString();
    }

}
