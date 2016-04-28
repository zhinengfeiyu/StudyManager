package com.caiyu.studymanager.manager;

import java.util.List;

/**
 * Created by 渝 on 2016/1/30.
 */
public interface IDaoManager<T> {

    void addData(T t);

    void deleteByKey(long id);

    void deleteAll();

    T getDataById(long id);

    List<T> getAll();

    long getTotalCount();

    boolean hasKey(long id);

}
