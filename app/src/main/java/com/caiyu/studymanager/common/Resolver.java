package com.caiyu.studymanager.common;

import com.caiyu.entity.ClassTableEntity;
import com.caiyu.entity.ClassTimeEntity;
import com.caiyu.studymanager.bean.ClassOffsetBean;
import com.caiyu.studymanager.manager.ClassTableManager;
import com.caiyu.studymanager.manager.ClassTimeManager;

import java.util.Calendar;
import java.util.List;

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

    public static ClassOffsetBean getCurrentOrNextClass() {
        ClassOffsetBean result = new ClassOffsetBean();
        Calendar calendar = Calendar.getInstance();
        int curWeekday = calendar.get(Calendar.DAY_OF_WEEK);
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        int curTotalMinutes = curHour * 60 + curMinute;
        ClassTimeManager classTimeManager = ClassTimeManager.getInstance();
        ClassTableManager classTableManager = ClassTableManager.getInstance();
        int timeOrder = 0;
        boolean isInClass = false;
        int searchStartClassId;
        if (curWeekday == Calendar.SATURDAY || curWeekday == Calendar.SUNDAY) {
            timeOrder = 1;
            isInClass = false;
            searchStartClassId = 1;
        }
        else {
            List<ClassTimeEntity> timeList = classTimeManager.getAll();
            for (ClassTimeEntity classTimeEntity : timeList) {
                int startMinutes = classTimeEntity.getStartHour() * 60 + classTimeEntity.getStartMinute();
                int endMinutes = classTimeEntity.getEndHour() * 60 + classTimeEntity.getEndMinute();
                if (curTotalMinutes <= startMinutes) {
                    timeOrder++;
                    isInClass = false;
                    break;
                } else if (curTotalMinutes < endMinutes) {
                    timeOrder++;
                    isInClass = true;
                    break;
                } else {
                    timeOrder++;
                }
                if (timeOrder == 5) {
                    timeOrder++;
                    isInClass = false;
                }
            }
            searchStartClassId = (curWeekday - Calendar.MONDAY) * 5 + timeOrder;
        }
        ClassTableEntity searchStartClassEntity = classTableManager.getDataById(searchStartClassId);

        if (Verifier.isEffectiveStr(searchStartClassEntity.getClassName())) {   //搜索到的应该有课的时间确实有课
            result.isInClass = isInClass;
            result.classId = searchStartClassId;
            if (timeOrder != 6) {
                if (isInClass) {
                    ClassTimeEntity timeEntity = classTimeManager.getDataById(timeOrder);
                    int timeEntityMinutes = getTotalMinutes(timeEntity.getEndHour(), timeEntity.getEndMinute());
                    int minutesOffset = timeEntityMinutes - curTotalMinutes;
                    result.dayOffset = 0;
                    result.hoursOffset = getHours(minutesOffset);
                    result.minutesOffset = getMinutes(minutesOffset);
                }
                else {
                    int targetWeekday = Calendar.MONDAY + (searchStartClassId - 1) / 5;
                    int targetTimeOrder = searchStartClassId % 5;
                    if (targetTimeOrder == 0)
                        targetTimeOrder = 5;
                    ClassTimeEntity targetTimeEntity = classTimeManager.getDataById(targetTimeOrder);
                    int minutesOffset = calculateMinutesOffset(curWeekday, curHour, curMinute,
                            targetWeekday, targetTimeEntity.getStartHour(),
                            targetTimeEntity.getStartMinute());
                    result.dayOffset = getDays(minutesOffset);
                    result.hoursOffset = getHours(minutesOffset);
                    result.minutesOffset = getMinutes(minutesOffset);
                }
            }
            else {
                ClassTimeEntity timeEntity = classTimeManager.getDataById(timeOrder);
                int timeEntityMinutes = getTotalMinutes(timeEntity.getStartHour(), timeEntity.getStartMinute());
                int zeroMinutes = 24 * 60;
                int minutesOffset = zeroMinutes - curTotalMinutes + timeEntityMinutes;
                result.dayOffset = 0;
                result.hoursOffset = getHours(minutesOffset);
                result.minutesOffset = getMinutes(minutesOffset);
            }
        }

        else {      //搜索到的应该有课的时间实际没有课，继续往下找
            result.isInClass = false;
            int classId = searchStartClassId;
            do {
                if (classId < 25)
                    classId++;
                else
                    classId = 1;
                if (classId == searchStartClassId) {    //课表为空的情况
                    classId = 0;
                    break;
                }
                ClassTableEntity classEntity = classTableManager.getDataById(classId);
                if (Verifier.isEffectiveStr(classEntity.getClassName())) {  //找到了有课的时间段
                    break;
                }
            } while (true);
            result.classId = classId;
            if (result.classId != 0) {
                int targetWeekday = Calendar.MONDAY + (classId - 1) / 5;
                int targetTimeOrder = classId % 5;
                if (targetTimeOrder == 0)
                    targetTimeOrder = 5;
                ClassTimeEntity targetTimeEntity = classTimeManager.getDataById(targetTimeOrder);
                int minutesOffset = calculateMinutesOffset(curWeekday, curHour, curMinute,
                                                        targetWeekday, targetTimeEntity.getStartHour(),
                                                        targetTimeEntity.getStartMinute());
                result.dayOffset = getDays(minutesOffset);
                result.hoursOffset = getHours(minutesOffset);
                result.minutesOffset = getMinutes(minutesOffset);
            }
        }
        return result;
    }

    public static int getTotalMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    public static int getDays(int minutes) {
        return minutes / (24 * 60);
    }

    public static int getHours(int minutes) {
        return minutes / 60;
    }

    public static int getMinutes(int minutes) {
        return minutes % 60;
    }

    /**
     * 计算当前时间和目标上课时间相差的总分钟数
     * @param curWeekday
     * @param curHour
     * @param curMinute
     * @param targetWeekday
     * @param targetHour
     * @param targetMinute
     * @return
     */
    public static int calculateMinutesOffset(int curWeekday, int curHour, int curMinute,
                                             int targetWeekday, int targetHour, int targetMinute) {
        int curTotalMinutes = getTotalMinutes(curHour, curMinute);
        int targetTotalMinutes = getTotalMinutes(targetHour, targetMinute);
        int weekdayOffset;
        int minutesOffsetInDay;
        int zeroMinutes = 24 * 60;
        if (targetWeekday > curWeekday) {
            weekdayOffset = targetWeekday - curWeekday - 1;
            minutesOffsetInDay = zeroMinutes - curTotalMinutes + targetTotalMinutes;
        }
        else if (targetWeekday < targetWeekday) {
            weekdayOffset = (Calendar.FRIDAY - curWeekday) + (targetWeekday - Calendar.MONDAY) + 2;
            minutesOffsetInDay = zeroMinutes - curTotalMinutes + targetTotalMinutes;
        }
        else {
            if (targetTotalMinutes < curTotalMinutes) {
                weekdayOffset = 6;
                minutesOffsetInDay = zeroMinutes - curTotalMinutes + targetTotalMinutes;
            }
            else {
                weekdayOffset = 0;
                minutesOffsetInDay = targetTotalMinutes - curTotalMinutes;
            }
        }
        return weekdayOffset * 24 * 60 + minutesOffsetInDay;
    }

    public static String resolveTimeOffset(ClassOffsetBean classOffsetBean) {
        String result;
        if (classOffsetBean.isInClass) {
            result = "正在上课";
        }
        else {
            StringBuilder sb = new StringBuilder(15);
            sb.append("还有");
            if (classOffsetBean.dayOffset > 0) {
                sb.append(classOffsetBean.dayOffset);
                sb.append('天');
            }
            if (classOffsetBean.hoursOffset > 0) {
                sb.append(classOffsetBean.hoursOffset);
                sb.append("小时");
            }
            if (classOffsetBean.dayOffset == 0 && classOffsetBean.minutesOffset > 0) {
                sb.append(classOffsetBean.minutesOffset);
                sb.append("分钟");
            }
            sb.append("上课");
            result = sb.toString();
        }
        return result;
    }
}
