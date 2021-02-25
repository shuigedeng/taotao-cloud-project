package com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    //字符串转Util.Date
    public static java.util.Date strToUtil(String str){
        try {
            return SIMPLE_DATE_FORMAT.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    //Util.Date转Sql.Date
    public static java.sql.Date utilToSql(java.util.Date date){
        return new java.sql.Date(date.getTime());
    }

    //Util.Date转String
    public static String utilToStr(java.util.Date date){
        return SIMPLE_DATE_FORMAT.format(date);
    }
}
