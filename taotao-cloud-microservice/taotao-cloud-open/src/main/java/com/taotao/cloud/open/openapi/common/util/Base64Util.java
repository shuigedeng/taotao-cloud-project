package com.taotao.cloud.open.openapi.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64跑龙套
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:24
 */
public class Base64Util {

    /**
     * 字节数组转Base64编码
     *
     * @param bytes 字节数组
     * @return Base64编码
     */
    public static String bytesToBase64(byte[] bytes) {
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Base64编码转字节数组
     *
     * @param base64Str Base64编码
     * @return 字节数组
     */
    public static byte[] base64ToBytes(String base64Str) {
        byte[] bytes = base64Str.getBytes(StandardCharsets.UTF_8);
        return Base64.getDecoder().decode(bytes);
    }
}
