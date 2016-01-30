package com.caiyu.studymanager.common;

import java.util.List;

/**
 * Created by 渝 on 2016/1/30.
 */
public class Verifier {

    public static boolean isEffectiveList(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean isEffectiveStr(String str) {
        return str != null && !str.equals("");
    }
}
