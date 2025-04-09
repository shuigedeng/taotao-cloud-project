package com.taotao.cloud.ccsr.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String calculateMD5(String content) {
        try {
            // 创建 MD5 摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 计算哈希值
            byte[] digest = md.digest(content.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
