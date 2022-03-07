package com.taotao.cloud.core.heaven.util.lang;

/**
 * 字节工具类]
 * <p>
 * 如果是 jdk8，将有很多方法。
 * <p>
 * 比如：long result = Long.parseUnsignedLong("B62FD56EFD5B375D", 16);
 * <p>
 * 但是 jdk7 是没有的。
 *
 */
public final class ByteUtil {

    private ByteUtil() {
    }

    /**
     * byte数组转int类型的对象
     *
     * @param bytes 字节数组
     * @return int
     */
    public static int byteToInt(byte[] bytes) {
        return (bytes[0] & 0xff) << 24
                | (bytes[1] & 0xff) << 16
                | (bytes[2] & 0xff) << 8
                | (bytes[3] & 0xff);
    }

    /**
     * int转byte数组
     *
     * @param num 整数
     * @return byte 数组
     */
    public byte[] intToByte(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((num >> 24) & 0xff);
        bytes[1] = (byte) ((num >> 16) & 0xff);
        bytes[2] = (byte) ((num >> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;
    }

}
