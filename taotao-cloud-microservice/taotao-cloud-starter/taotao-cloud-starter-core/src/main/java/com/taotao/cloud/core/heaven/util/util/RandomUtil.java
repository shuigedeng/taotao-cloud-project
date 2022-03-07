package com.taotao.cloud.core.heaven.util.util;


import com.taotao.cloud.core.heaven.response.exception.CommonRuntimeException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 */
public final class RandomUtil {

    private RandomUtil(){}

    /** 用于随机选的数字 */
    private static final String BASE_NUMBER = "0123456789";

    /** 用于随机选的字符 */
    private static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";

    /** 用于随机选的字符和数字 */
    private static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;

    /**
     * 获取随机数生成器对象<br>
     * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
     *
     * @return {@link ThreadLocalRandom}
     * @since 0.1.13
     */
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 获取{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)
     *
     * @return {@link SecureRandom}
     * @since 0.1.13
     */
    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取随机数产生器
     *
     * @param isSecure 是否为强随机数生成器 (RNG)
     * @return {@link Random}
     * @since 0.1.13
     * @see #getSecureRandom()
     * @see #getRandom()
     */
    public static Random getRandom(boolean isSecure) {
        return isSecure ? getSecureRandom() : getRandom();
    }

    /**
     * 随机的字符
     * @param length 长度
     * @return 随机的数字
     */
    public static String randomChar(final int length) {
        return randomString(BASE_CHAR, length);
    }
    /**
     * 随机的字符数字
     * @param length 长度
     * @return 随机的数字
     */
    public static String randomCharNumber(final int length) {
        return randomString(BASE_CHAR_NUMBER, length);
    }

    /**
     * 随机的数字
     * @param length 长度
     * @return 随机的数字
     */
    public static String randomNumber(final int length) {
        return randomString(BASE_NUMBER, length);
    }

    /**
     * 获得一个随机的字符串
     *
     * @param baseString 随机字符选取的样本
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomString(String baseString, int length) {
        final StringBuilder sb = new StringBuilder();

        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = ThreadLocalRandom.current().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 随机选择列表中的一个元素
     * @param list 列表
     * @param <T> 泛型
     * @return 结果
     * @since 0.1.97
     */
    public static <T> T random(final List<T> list) {
        if(CollectionUtil.isEmpty(list)) {
            return null;
        }

        final int size = list.size();
        final Random random = ThreadLocalRandom.current();
        int index = random.nextInt(size);

        return list.get(index);
    }

}
