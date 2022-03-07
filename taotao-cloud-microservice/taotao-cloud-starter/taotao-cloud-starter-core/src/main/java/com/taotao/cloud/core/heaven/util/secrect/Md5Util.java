package com.taotao.cloud.core.heaven.util.secrect;


import com.taotao.cloud.core.heaven.util.codec.Base64;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 加密算法
 */
public final class Md5Util {

    private Md5Util(){}

    /**
     * 获取字符串的 md5 值
     * @param string 字符串
     * @return md5
     */
    public static String md5(final String string) {
        try {
            if(StringUtil.isEmpty(string)) {
                return string;
            }

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] output = messageDigest.digest(string.getBytes());

            //将得到的字节数组变成字符串返回
            return Base64.encodeToString(output);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
