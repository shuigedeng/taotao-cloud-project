/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.common.utils;

import java.util.Arrays;
import java.util.Collection;

/**
 * 字符串工具类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:50
 */
public final class StringUtils {

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    private StringUtils() {

    }

    /**
     * 判断是否为空白字符串
     *
     * @param cs
     *         字符
     * @return 是否为空白字符串
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否不为空白字符串
     *
     * @param cs
     *         字符
     * @return 是否不为空白字符串
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 判断字符串数组中是否有空白字符串
     *
     * @param css
     *         字符串数组
     * @return 是否有空白字符串
     */
    public static boolean isAnyBlank(final CharSequence... css) {
        return css != null && css.length > 0 && Arrays.stream(css).anyMatch(StringUtils::isBlank);
    }

    /**
     * 去除字符串两端空白，如果为空字符串则返回null
     *
     * @param str
     *         字符串
     * @return 去除两端空白后的字符串
     */
    public static String trimToNull(final String str) {
        if (str == null) {
            return null;
        }
        String ts = str.trim();
        return ts.length() == 0 ? null : ts;
    }

    /**
     * 将对象数组字符串化后通过连字符进行拼接
     *
     * @param collection
     *         待拼接的对象数组
     * @param separator
     *         连字符
     * @return 拼接后的字符串
     */
    public static String join(Collection<?> collection, String separator) {
        if (collection == null || collection.isEmpty()) {
            return EMPTY;
        }

        return join(collection.toArray(), separator);
    }

    /**
     * 将对象数组字符串化后通过连字符进行拼接
     *
     * @param array
     *         待拼接的对象数组
     * @param separator
     *         连字符
     * @return 拼接后的字符串
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }

        return join(array, separator, 0, array.length);
    }

    /**
     * 将对象数组字符串化后通过连字符进行拼接
     *
     * @param array
     *         待拼接的对象数组
     * @param separator
     *         连字符
     * @param startIndex
     *         开始位置
     * @param endIndex
     *         结束位置
     * @return 拼接后的字符串
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }

        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
}
