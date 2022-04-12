package com.taotao.cloud.common.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户名验证工具类
 */
public class RegularUtil {


    /**
     * 手机号
     */
    public static final Pattern MOBILE = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$");

    /**
     * 邮箱
     */
    public static final Pattern EMAIL = Pattern.compile("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");

    public static boolean mobile(String v){

        Matcher m = MOBILE.matcher(v);
        if(m.matches()){
            return true;
        }
        return false;
    }

    public static boolean email(String v){

        Matcher m = EMAIL.matcher(v);
        if(m.matches()){
            return true;
        }
        return false;
    }
}
