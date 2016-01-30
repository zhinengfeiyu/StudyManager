package com.caiyu.studymanager.manager;

import java.util.List;

/**
 * Created by 渝 on 2016/1/30.
 */
public interface IDaoManager<T> {

    public void addData(T t);

    public void deleteByKey(long id);

    public void deleteAll();

    public T getDataById(long id);

    public List<T> getAll();

    public long getTotalCount();

    public boolean hasKey(long id);

}
