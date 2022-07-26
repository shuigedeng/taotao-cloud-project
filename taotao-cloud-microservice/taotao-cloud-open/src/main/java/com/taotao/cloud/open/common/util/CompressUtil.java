package com.taotao.cloud.open.common.util;

import cn.hutool.core.util.ZipUtil;

import java.nio.charset.StandardCharsets;

/**
 * 压缩工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:32
 */
public class CompressUtil {

    /**
     * 压缩文本
     *
     * @param text 文本
     * @return 压缩后的字节数组
     */
    public static byte[] compressText(String text) {
        byte[] bodyBytes = text.getBytes(StandardCharsets.UTF_8);
        return ZipUtil.gzip(bodyBytes);
    }

    /**
     * 压缩
     *
     * @param bytes 字节数组
     * @return 压缩后的字节数组
     */
    public static byte[] compress(byte[] bytes) {
        return ZipUtil.gzip(bytes);
    }

    /**
     * 解压到文本
     *
     * @param compressedBytes 压缩的字节数组
     * @return 解压后的文本
     */
    public static String decompressToText(byte[] compressedBytes) {
        byte[] decompressedBytes = ZipUtil.unGzip(compressedBytes);
        return new String(decompressedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 解压
     *
     * @param compressedBytes 压缩的字节数组
     * @return 解压后的字节数组
     */
    public static byte[] decompress(byte[] compressedBytes) {
        return ZipUtil.unGzip(compressedBytes);
    }
}
