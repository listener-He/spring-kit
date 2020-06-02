package org.hehh.cloud.common.utils.date;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @create: 2020-03-21 01:35
 * @description: 日期工具类
 **/
public class DateKit {

    private final static String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    /**
     * 获取月所有天
     * @param date 指定日期
     * @return
     */
    public static List<Date> getMonthDay(Date date) {

        if (null == date) {
            return null;
        }

        date = DateUtil.beginOfMonth(new Date());
        long l = DateUtil.betweenDay(date, DateUtil.endOfMonth(date), true);

        List<Date> dates = new ArrayList<>(Long.valueOf(l).intValue() + 1);
        dates.add(date);

        for (long i = 0; i < l; i++) {
            dates.add(DateUtil.offsetDay(date, Long.valueOf(i).intValue() + 1));
        }
        return dates;
    }


    /**
     *  获取指定日期后的所有时间
     * @param date 指定日期
     * @param offset 指定天
     * @return
     */
    public static List<Date> offsetDays(Date date,int offset){
        if(date == null || offset < 1){
            return null;
        }
        List<Date> dateList = new ArrayList<>();
        dateList.add(date);
        for (int i = 1; i <= offset; i++) {
            dateList.add(DateUtil.offsetDay(date,i));
        }
        return dateList.stream().sorted().collect(Collectors.toList());
    }


    /**
     *  获取指定日期后的所有时间
     * @param date 指定日期
     * @param offset 指定天
     * @return
     */
    public static List<String> offsetDaysDate(Date date,int offset){
        List<Date> dates = offsetDays(date, offset);
        if(dates == null){
            return null;
        }
        return dates.stream().map(v->DateUtil.formatDate(v)).collect(Collectors.toList());
    }



    /**
     * 格式化周 -天
     *
     * @param week
     * @return
     */
    public static String formatWeekDay(int week) {
        switch (week) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
        }
        return null;
    }


    /**
     *  格式化月
     * @param month
     * @return
     */
    public static String formatMonth(int month) {
        if (month > 12 || month < 1) {
            return null;
        }
        return months[month - 1];
    }


    /**
     *  格式化年
     * @param year
     * @return
     */
    public static String formatYear(int year){
        String format = Integer.valueOf(year).toString();

        if(year<0){
            format = format.substring(1);
        }

        final StringBuilder builder = StrUtil.builder(format.length());
        if(year <0){
            builder.append("公元前");
        }
        for (char c : format.toCharArray()) {
            builder.append(Convert.numberToChinese(Integer.parseInt(c+""), false));
        }
        format = builder.append("年").toString().replace('零', '〇');
        return format;
    }


    /**
     *  相差时间格式化
     * @param start
     * @param end
     * @return
     */
    public static String formatBetween(Date start,Date end){
        /**
         *  得到相差秒
         */
        long between = DateUtil.between(start, end, DateUnit.SECOND);
        if(between < 60){
            /**小于一分钟*/
            String s = DateUtil.formatBetween(start, end, BetweenFormater.Level.SECOND);
            return s.substring(s.lastIndexOf("分")+1);
        }else if(between < (60 * 60)){
            /**小于一小时*/
            String s = DateUtil.formatBetween(start, end, BetweenFormater.Level.MINUTE);
            return s.substring(s.lastIndexOf("时")+1,s.lastIndexOf("分")+1)+"钟";
        }else if(between < (60 * 60 * 24)){
            /**小于一天*/
            String s = DateUtil.formatBetween(start, end, BetweenFormater.Level.HOUR);
            return s.substring(s.lastIndexOf("天")+1,s.lastIndexOf("时")+1);
        }else if(between < (60 * 60 * 24 * 30)){
            /**小于一月*/
            String s = DateUtil.formatBetween(start, end, BetweenFormater.Level.HOUR);
            return  s.substring(0,s.lastIndexOf("天")+1);
            //return s.substring(0,s.lastIndexOf("时")+1);
        }else if(between < (60 * 60 * 24 * 365)){
            /**不满一年*/
            return DateUtil.betweenMonth(start,end,true)+"个月";
        }else if(between > (60 * 60 * 24 * 365)){
            /**超过一年*/
            return DateUtil.formatDateTime(start);
        }else{
            return "刚刚";
        }

    }
}
