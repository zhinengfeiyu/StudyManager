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

    /**
     * 测试字符串是否表示非负整数
     */
    public static boolean isEffectiveUnsignedInt(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9')
                return false;
        }
        return true;
    }
}
