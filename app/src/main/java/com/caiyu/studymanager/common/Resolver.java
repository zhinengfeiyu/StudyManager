package com.caiyu.studymanager.common;

import java.util.Calendar;

/**
 * Created by 渝 on 2016/4/16.
 */
public class Resolver {

    public static String resolveClassOrder(int classOrder) {
        if (classOrder < 1 || classOrder > 25)
            return null;
        int weekday = (classOrder - 1) / 5;
        int dayOrder = classOrder % 5;
        StringBuilder sb = new StringBuilder(9);
        switch (weekday) {
            case 0:
                sb.append("星期一");
                break;
            case 1:
                sb.append("星期二");
                break;
            case 2:
                sb.append("星期三");
                break;
            case 3:
                sb.append("星期四");
                break;
            case 4:
                sb.append("星期五");
                break;
        }
        switch (dayOrder) {
            case 1:
                sb.append("上午第一二节");
                break;
            case 2:
                sb.append("上午第三四节");
                break;
            case 3:
                sb.append("下午第五六节");
                break;
            case 4:
                sb.append("下午第七八节");
                break;
            case 5:
                sb.append("晚上第九十节");
                break;
        }
        return sb.toString();
    }

    public static String resolveClassWeek(int startWeek, int endWeek) {
        String result = "第%1$d周-第%2$d周";
        return String.format(result, startWeek, endWeek);
    }

    public static String resolveWeekday(int weekday) {
        String result = "";
        switch (weekday) {
            case Calendar.MONDAY:
                result = "星期一";
                break;
            case Calendar.TUESDAY:
                result = "星期二";
                break;
            case Calendar.WEDNESDAY:
                result = "星期三";
                break;
            case Calendar.THURSDAY:
                result = "星期四";
                break;
            case Calendar.FRIDAY:
                result = "星期五";
                break;
            case Calendar.SATURDAY:
                result = "星期六";
                break;
            case Calendar.SUNDAY:
                result = "星期日";
                break;
        }
        return result;
    }
}
