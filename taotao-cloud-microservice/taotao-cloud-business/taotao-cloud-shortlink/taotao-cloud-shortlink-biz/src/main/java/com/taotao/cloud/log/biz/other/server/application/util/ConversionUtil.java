package com.taotao.cloud.log.biz.other.server.application.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

public class ConversionUtil {
    /*
     * 初始化 62 进制数据，索引位置代表字符的数值，比如 A代表10，z代表61等
     */
    private static String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static int scale = 62;

    /**
     * 将数字转为62进制
     * @param num    Long 型数字
     * @param length 转换后的字符串长度，不足则左侧补0
     * @return 62进制字符串
     */
    public static String encode(long num, int length) {
        StringBuffer sf = new StringBuffer();
        int remainder = 0;
        while (num > scale - 1) {
            remainder = Long.valueOf(num % scale).intValue();
            sf.append(chars.charAt(remainder));

            num = num / scale;
        }

        sf.append(chars.charAt(Long.valueOf(num).intValue()));
        String value = sf.reverse().toString();
        return StringUtils.leftPad(value, length, '0');
    }

    /**
     * 62进制字符串转为数字
     *
     * @param str 编码后的62进制字符串
     * @return 解码后的 10 进制字符串
     */
    public static long decode(String str) {
        str = str.replace("^0*", "");
        long num = 0;
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            index = chars.indexOf(str.charAt(i));
            num += (long) (index * (Math.pow(scale, str.length() - i - 1)));
        }

        return num;
    }

}
