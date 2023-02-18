package com.example.tabipapp.base.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leopold on 2021/3/24
 * Description:
 */
public class TimeUtils {


    public static String createTimeFormat(long milliseconds){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(milliseconds);
        return dateFormat.format(date);
    }


    public static String formatDuration(long durationSecond){
        //歌曲当前播放时长
        int minute = (int)durationSecond / 60;
        int second = (int)durationSecond  % 60;
        String strMinute = "";
        String strSecond = "";
        //如果时间中的分钟小于10
        if (minute < 10) {
            //在分钟的前面加一个0
            strMinute = "0" + minute;
        } else {
            strMinute = minute + "";
        }
        //如果时间中的秒钟小于10
        if (second < 10) {
            //在秒钟前面加一个0
            strSecond = "0" + second;
        } else {
            strSecond = second + "";
        }
        return strMinute+":"+strSecond;
    }


}
