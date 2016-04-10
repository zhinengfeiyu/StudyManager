package com.caiyu.studymanager.manager;

import com.caiyu.dao.NoteDao;
import com.caiyu.entity.NoteEntity;
import com.caiyu.studymanager.common.Verifier;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 渝 on 2016/1/30.
 */
public class NoteManager implements IDaoManager<NoteEntity>{

    private static NoteManager instance;
    private NoteDao noteDao;

    private NoteManager() {
        try {
            noteDao = DaoLoader.getDaoSession().getNoteDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDao() {
        noteDao = DaoLoader.getDaoSession().getNoteDao();
    }

    public static NoteManager getInstance() {
        if (instance == null)
            instance = new NoteManager();
        return instance;
    }

    @Override
    public void addData(NoteEntity entity) {
        if (noteDao != null && entity != null) {
            noteDao.insertOrReplace(entity);
        }
    }

    @Override
    public void deleteByKey(long id) {
        if(noteDao != null) {
            noteDao.deleteByKey(id);
        }
    }

    @Override
    public NoteEntity getDataById(long id) {
        if(noteDao != null) {
            return noteDao.load(id);
        }
        return null;
    }

    @Override
    public List<NoteEntity> getAll() {
        List<NoteEntity> list;
        if(noteDao != null) {
            list = noteDao.loadAll();
            return list;
        }
        return null;
    }

    @Override
    public boolean hasKey(long id) {
        if(noteDao == null) {
            return false;
        }
        QueryBuilder<NoteEntity> qb = noteDao.queryBuilder();
        qb.where(NoteDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if(noteDao == null) {
            return 0;
        }

        QueryBuilder<NoteEntity> qb = noteDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if(noteDao != null) {
            noteDao.deleteAll();
        }
    }

    public List<NoteEntity> getDataBySubjectId(long subjectId) {
        if(noteDao == null) {
            return null;
        }
        QueryBuilder<NoteEntity> qb = noteDao.queryBuilder();
        qb.where(NoteDao.Properties.Subject_id.eq(subjectId));
        List<NoteEntity> list = qb.list();
        if (Verifier.isEffectiveList(list))
            return list;
        else
            return null;
    }

    public List<NoteEntity> getNoteBySubjectId(long subjectId) {
        if(noteDao == null) {
            return null;
        }
        QueryBuilder<NoteEntity> qb = noteDao.queryBuilder();
        qb.where(NoteDao.Properties.Subject_id.eq(subjectId));
        List<NoteEntity> list = qb.list();
        return list;
    }

    public void update(NoteEntity entity) {
        if (noteDao != null)
            noteDao.update(entity);
    }

}

