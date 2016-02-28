package com.caiyu.studymanager.manager;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.dao.ClassTableDao;
import com.caiyu.entity.ClassTableEntity;
import com.caiyu.entity.SubjectEntity;
import com.caiyu.studymanager.activity.MyApplication;
import com.caiyu.studymanager.bean.ClassBean;
import com.caiyu.studymanager.bean.TopicBean;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 渝 on 2016/1/30.
 */
public class ClassTableManager implements IDaoManager<ClassTableEntity>{

    private static ClassTableManager instance;
    private ClassTableDao classTableDao;

    private ClassTableManager() {
        try {
            classTableDao = DaoLoader.getDaoSession().getClassTableDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDao() {
        classTableDao = DaoLoader.getDaoSession().getClassTableDao();
    }

    public static ClassTableManager getInstance() {
        if (instance == null)
            instance = new ClassTableManager();
        return instance;
    }

    @Override
    public void addData(ClassTableEntity entity) {
        if (classTableDao != null && entity != null) {
            classTableDao.insertOrReplaceInTx(entity);
        }
    }

    @Override
    public void deleteByKey(long id) {
        if(classTableDao != null) {
            classTableDao.deleteByKey(id);
        }
    }

    @Override
    public ClassTableEntity getDataById(long id) {
        if(classTableDao != null) {
            return classTableDao.load(id);
        }
        return null;
    }

    @Override
    public List<ClassTableEntity> getAll() {
        List<ClassTableEntity> list;
        if(classTableDao != null) {
            list = classTableDao.loadAll();
            return list;
        }
        return null;
    }

    @Override
    public boolean hasKey(long id) {
        if(classTableDao == null) {
            return false;
        }

        QueryBuilder<ClassTableEntity> qb = classTableDao.queryBuilder();
        qb.where(ClassTableDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if(classTableDao == null) {
            return 0;
        }

        QueryBuilder<ClassTableEntity> qb = classTableDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if(classTableDao != null) {
            classTableDao.deleteAll();
        }
    }

    public void update(ClassTableEntity entity) {
        if (classTableDao != null) {
            String subjectName = entity.getClassName();
            SubjectManager sm = SubjectManager.getInstance();
            long subjectId = sm.getIdByName(subjectName);
            if (subjectId == -1) {
                sm.addData(new SubjectEntity(null, subjectName));
                subjectId = sm.getIdByName(subjectName);
            }
            entity.setSubjectId(subjectId);
            classTableDao.update(entity);
        }
    }

    public void fixDao() {
        if (getTotalCount() != 25) {
            deleteAll();
            long id = 0;
            for (int dayOfWeek = 1; dayOfWeek <= 5; dayOfWeek++) {
                for (int orderOfDay = 1; orderOfDay <= 5; orderOfDay++) {
                    id++;
                    ClassTableEntity entity = new ClassTableEntity(id);
                    entity.setDayOfWeek(dayOfWeek);
                    entity.setOrderOfDay(orderOfDay);
                    entity.setClassName("");
                    entity.setClassRoom("");
                    entity.setTeacher("");
                    entity.setStartWeek(0);
                    entity.setEndWeek(0);
                    entity.setSubjectId(-1L);
                    addData(entity);
                }
            }
        }
    }


    public String getShowWeekday(int weekday) {
        String showStr;
        switch (weekday) {
            case 1:
                showStr = "星期一";
                break;
            case 2:
                showStr = "星期二";
                break;
            case 3:
                showStr = "星期三";
                break;
            case 4:
                showStr = "星期四";
                break;
            case 5:
                showStr = "星期五";
                break;
            default:
                showStr = "";
        }
        return showStr;
    }

    public String getShowOrder(int order) {
        String showStr;
        switch (order) {
            case 1:
                showStr = "上午第一二节";
                break;
            case 2:
                showStr = "上午第三四节";
                break;
            case 3:
                showStr = "下午第一二节";
                break;
            case 4:
                showStr = "下午第三四节";
                break;
            case 5:
                showStr = "晚上第一二节";
                break;
            default:
                showStr = "";
        }
        return showStr;
    }


}
