package com.taotao.cloud.ccsr.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * ByteUtils.
 */
public final class ByteUtils {

    private ByteUtils() {
    }

    public static final byte[] EMPTY = new byte[0];

    /**
     * String to byte array.
     *
     * @param input input string
     * @return byte array of string
     */
    public static byte[] toBytes(String input) {
        if (input == null) {
            return EMPTY;
        }
        return input.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Object to byte array.
     *
     * @param obj input obj
     * @return byte array of object
     */
    public static byte[] toBytes(Object obj) {
        if (obj == null) {
            return EMPTY;
        }
        return toBytes(String.valueOf(obj));
    }

    /**
     * Byte array to string.
     *
     * @param bytes byte array
     * @return string
     */
    public static String toString(byte[] bytes) {
        if (bytes == null) {
            return StringUtils.EMPTY;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }

    public static boolean isNotEmpty(byte[] data) {
        return !isEmpty(data);
    }

}
