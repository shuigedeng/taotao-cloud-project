package com.taotao.cloud.payment.biz.jeepay.jeepay.util;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * 字符串工具类
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:00
 */
public class StringUtils {

    private static Pattern whitespacePattern = Pattern.compile("\\s");

    public static boolean containsWhitespace(String str) {
        requireNonNull(str);
        return whitespacePattern.matcher(str).find();
    }

    public static String join(String separator, List<String> input) {

        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));

            // if not the last item
            if (i != input.size() - 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String genUrl(String url, String uri) {
        if(!url.endsWith("/")) url += "/";
        return url += uri;
    }

    public static Boolean isEmpty(String str) {
        if(str == null) return true;
        return "".equals(str.trim());
    }

}
