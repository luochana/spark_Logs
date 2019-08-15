package com.luochan.spark_web.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DateUtils {
    public static Date getCurrenttime()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());
        try {
            Date getNewsTime=df.parse(date);
            return getNewsTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getCurrentTimeStr()
    {
        // getInRecTimestamp(1);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(getCurrenttime());
    }
}
