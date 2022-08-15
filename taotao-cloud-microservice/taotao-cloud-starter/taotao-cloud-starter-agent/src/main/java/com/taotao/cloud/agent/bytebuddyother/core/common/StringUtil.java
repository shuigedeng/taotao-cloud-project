package com.taotao.cloud.agent.bytebuddyother.core.common;

/**
 * Created by pphh on 2022/8/4.
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return str == null || isEmpty(str.trim());
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

}
