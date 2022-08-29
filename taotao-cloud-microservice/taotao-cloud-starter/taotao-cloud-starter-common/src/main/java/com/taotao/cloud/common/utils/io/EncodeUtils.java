package com.taotao.cloud.common.utils.io;


import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.lang.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 编码工具类
 *
 */
public class EncodeUtils {

    /**
     * 对内容进行 utf=8 编码
     *
     * @param content 编码
     * @return 结果
     */
    public static String encode(final String content) {
        return encode(content, CommonConstant.UTF8);
    }

    /**
     * 对内容进行 utf=8 编码
     *
     * @param content 编码
     * @param charset 字符编码
     * @return 结果
     */
    public static String encode(final String content, final String charset) {
        try {
            return URLEncoder.encode(content, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 编码为 unicode
     *
     * unicode 码对每一个字符用4位16进制数表示。
     *
     * 具体规则是：将一个字符(char)的高8位与低8位分别取出，转化为16进制数，如果转化的16进制数的长度不足2位，则在其后补0，然后将高、低8位转成的16进制字符串拼接起来并在前面补上 u 即可。
     *
     * @param string 字符串
     * @return 结果
     */
    public static String encodeUnicode(final String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }

        String tmp;
        int length = 6 * string.length();
        StringBuffer sb = new StringBuffer(length);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < string.length(); i++) {
            c = string.charAt(i);
            sb.append("\\u");
            //取出高8位
            j = (c >>> 8);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            //取出低8位
            j = (c & 0xFF);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }

        return (new String(sb));
    }

    /**
     * 解码 unicode
     *
     * @param string 字符串
     * @return 结果
     */
    public static String decodeUnicode(final String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        //如果不是unicode码则原样返回
        if (!string.contains("\\u")) {
            return string;
        }

        int actualLength = string.length() / 6;
        StringBuilder sb = new StringBuilder(actualLength);
        for (int i = 0; i <= string.length() - 6; i += 6) {
            String strTemp = string.substring(i, i + 6);
            String value = strTemp.substring(2);
            int c = 0;
            for (int j = 0; j < value.length(); j++) {
                char tempChar = value.charAt(j);
                int t = 0;
                switch (tempChar) {
                    case 'a':
                        t = 10;
                        break;
                    case 'b':
                        t = 11;
                        break;
                    case 'c':
                        t = 12;
                        break;
                    case 'd':
                        t = 13;
                        break;
                    case 'e':
                        t = 14;
                        break;
                    case 'f':
                        t = 15;
                        break;
                    default:
                        t = tempChar - 48;
                        break;
                }
                c += t * ((int) Math.pow(16, (value.length() - j - 1)));
            }
            sb.append((char) c);
        }
        return sb.toString();
    }

}
