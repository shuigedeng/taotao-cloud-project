/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.utils.collection;


import com.google.common.collect.Lists;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.support.handler.IHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数组基本类型工具类
 */
public final class ArrayPrimitiveUtil {

    public static final int[] INT_EMPTY = new int[0];

    public static final short[] SHORT_EMPTY = new short[0];

    public static final long[] LONG_EMPTY = new long[0];

    public static final float[] FLOAT_EMPTY = new float[0];

    public static final double[] DOUBLE_EMPTY = new double[0];

    public static final char[] CHAR_EMPTY = new char[0];

    public static final byte[] BYTE_EMPTY = new byte[0];

    public static final boolean[] BOOLEAN_EMPTY = new boolean[0];

    private ArrayPrimitiveUtil() {
    }

    /**
     * int 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(int[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(int[] objects) {
        return !isEmpty(objects);
    }


    /**
     * boolean 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(boolean[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * boolean 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(boolean[] objects) {
        return !isEmpty(objects);
    }

    /**
     * char 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(char[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * char 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(char[] objects) {
        return !isEmpty(objects);
    }

    /**
     * byte 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(byte[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * byte 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(byte[] objects) {
        return !isEmpty(objects);
    }

    /**
     * long 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(long[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * long 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(long[] objects) {
        return !isEmpty(objects);
    }

    /**
     * float 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(float[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(float[] objects) {
        return !isEmpty(objects);
    }

    /**
     * double 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(double[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * double 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(double[] objects) {
        return !isEmpty(objects);
    }

    /**
     * short 数组是否为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isEmpty(short[] objects) {
        return null == objects
                || objects.length <= 0;
    }

    /**
     * short 数组是否不为空
     *
     * @param objects 数组对象
     * @return 是否为空
     */
    public static boolean isNotEmpty(short[] objects) {
        return !isEmpty(objects);
    }

    /**
     * boolean 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final boolean[] values, IHandler<? super Boolean, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(boolean value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * char 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final char[] values, IHandler<? super Character, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(char value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * byte 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final byte[] values, IHandler<? super Byte, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(byte value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * short 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final short[] values, IHandler<? super Short, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(short value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * int 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final int[] values, IHandler<? super Integer, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(int value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * float 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final float[] values, IHandler<? super Float, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(float value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * double 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final double[] values, IHandler<? super Double, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(double value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * long 可遍历的元素对象的某个元素，转换为列表
     *
     * @param values      遍历对象
     * @param keyFunction 转换方式
     * @param <K>         k 泛型
     * @return 结果列表
     */
    public static <K> List<K> toList(final long[] values, IHandler<? super Long, K> keyFunction) {
        if (ArrayPrimitiveUtil.isEmpty(values)) {
            return Collections.emptyList();
        }

        List<K> list = new ArrayList<>(values.length);
        for(long value : values) {
            final K key = keyFunction.handle(value);
            list.add(key);
        }
        return list;
    }

    /**
     * 返回 c 对应的下标
     * @param chars 原始字符
     * @param c 目标
     * @return 结果
     */
    public static int indexOf(final char[] chars, final char c) {
        if(ArrayPrimitiveUtil.isEmpty(chars)) {
            return -1;
        }

        for(int i = 0; i < chars.length; i++) {
            char cs = chars[i];
            if(cs == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 是否包含
     * @param chars 原始字符
     * @param c 目标
     * @return 结果
     */
    public static boolean contains(final char[] chars, final char c) {
        if(ArrayPrimitiveUtil.isEmpty(chars)) {
            return false;
        }

        for (char cs : chars) {
            if (cs == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回 c 对应的最后下标
     * @param chars 原始字符
     * @param c 目标
     * @return 结果
     */
    public static int lastIndexOf(final char[] chars, final char c) {
        if(ArrayPrimitiveUtil.isEmpty(chars)) {
            return -1;
        }

        int lastIndex = -1;
        for(int i = 0; i < chars.length; i++) {
            char cs = chars[i];
            if(cs == c) {
                lastIndex = i;
            }
        }
        return lastIndex;
    }

    /**
     * 返回 c 对应的所有下标
     * @param chars 原始字符
     * @param c 目标
     * @return 结果
     */
    public static List<Integer> allIndexOf(final char[] chars, final char c) {
        if(ArrayPrimitiveUtil.isEmpty(chars)) {
            return Collections.emptyList();
        }

        List<Integer> indexList = Lists.newArrayList();
        for(int i = 0; i < chars.length; i++) {
            char cs = chars[i];
            if(cs == c) {
                indexList.add(i);
            }
        }
        return indexList;
    }

    /**
     * 获取 char 数组指定的
     * （1）如果一直没有找到指定符号，则一直进行到底。
     * （2）如果有 " 则忽略内容。
     * @param chars 字符数组
     * @param startIndex 开始下标
     * @param symbol 特殊标志
     * @return 结果字符串
     */
    public static String getStringBeforeSymbol(final char[] chars, final int startIndex,
                                             final char symbol) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean doubleQuotesStart = false;
        char preChar = CommonConstant.BLANK;

        for(int i = startIndex; i < chars.length; i++) {
            char currentChar = chars[i];

            preChar = getPreChar(preChar, currentChar);
            // 上一个字符不是转义，且当前为 "。则进行状态的切换
            if (CommonConstant.BACK_SLASH != preChar
                    && CommonConstant.DOUBLE_QUOTES == currentChar) {
                doubleQuotesStart = !doubleQuotesStart;
            }

            // 不在双引号中，且为特殊符号。则直接返回
            if(!doubleQuotesStart && symbol == currentChar) {
                return stringBuilder.toString();
            }
            stringBuilder.append(currentChar);
        }

        return stringBuilder.toString();
    }

    /**
     * 获取上一个字符
     *
     * 保证转义字符的两次抵消。
     *
     * @param preChar     上一个字符
     * @param currentChar 当前字符
     * @return 结果
     */
    private static char getPreChar(final char preChar, final char currentChar) {
        // 判断前一个字符是什么
        if (CommonConstant.BACK_SLASH == preChar
                && CommonConstant.BACK_SLASH == currentChar) {
            return CommonConstant.BLANK;
        }
        return currentChar;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> int[] toIntArray(final List<E> list, final IHandler<E, Integer> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return INT_EMPTY;
        }

        final int size = list.size();
        int[] ints = new int[size];
        for(int i = 0; i < size; i++) {
            ints[i] = handler.handle(list.get(i));
        }
        return ints;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> boolean[] toBooleanArray(final List<E> list, final IHandler<E, Boolean> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return BOOLEAN_EMPTY;
        }

        final int size = list.size();
        boolean[] arrays = new boolean[size];
        for(int i = 0; i < size; i++) {
            arrays[i] = handler.handle(list.get(i));
        }
        return arrays;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> char[] toCharArray(final List<E> list, final IHandler<E, Character> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return CHAR_EMPTY;
        }

        final int size = list.size();
        char[] arrays = new char[size];
        for(int i = 0; i < size; i++) {
            arrays[i] = handler.handle(list.get(i));
        }
        return arrays;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> byte[] toByteArray(final List<E> list, final IHandler<E, Byte> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return BYTE_EMPTY;
        }

        final int size = list.size();
        byte[] arrays = new byte[size];
        for(int i = 0; i < size; i++) {
            arrays[i] = handler.handle(list.get(i));
        }
        return arrays;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> short[] toShortArray(final List<E> list, final IHandler<E, Short> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return SHORT_EMPTY;
        }

        final int size = list.size();
        short[] arrays = new short[size];
        for(int i = 0; i < size; i++) {
            arrays[i] = handler.handle(list.get(i));
        }
        return arrays;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> long[] toLongArray(final List<E> list, final IHandler<E, Long> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return LONG_EMPTY;
        }

        final int size = list.size();
        long[] arrays = new long[size];
        for(int i = 0; i < size; i++) {
            arrays[i] = handler.handle(list.get(i));
        }
        return arrays;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> float[] toFloatArray(final List<E> list, final IHandler<E, Float> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return FLOAT_EMPTY;
        }

        final int size = list.size();
        float[] arrays = new float[size];
        for(int i = 0; i < size; i++) {
            arrays[i] = handler.handle(list.get(i));
        }
        return arrays;
    }

    /**
     * 转换为数组
     * @param list 列表
     * @param handler 转换实现
     * @param <E> 泛型
     * @return 结果
     */
    public static <E> double[] toDoubleArray(final List<E> list, final IHandler<E, Double> handler) {
        if(CollectionUtil.isEmpty(list)) {
            return DOUBLE_EMPTY;
        }

        final int size = list.size();
        double[] arrays = new double[size];
        for(int i = 0; i < size; i++) {
            arrays[i] = handler.handle(list.get(i));
        }
        return arrays;
    }

    /**
     * 创建 int 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static int[] newArray(int ... arrays) {
        return arrays;
    }

    /**
     * 创建 boolean 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static boolean[] newArray(boolean ... arrays) {
        return arrays;
    }

    /**
     * 创建 char 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static char[] newArray(char ... arrays) {
        return arrays;
    }

    /**
     * 创建 short 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static short[] newArray(short ... arrays) {
        return arrays;
    }

    /**
     * 创建 long 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static long[] newArray(long ... arrays) {
        return arrays;
    }

    /**
     * 创建 byte 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static byte[] newArray(byte ... arrays) {
        return arrays;
    }

    /**
     * 创建 float 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static float[] newArray(float ... arrays) {
        return arrays;
    }

    /**
     * 创建 double 数组
     * @param arrays 数组元素
     * @return 数组
     */
    public static double[] newArray(double ... arrays) {
        return arrays;
    }

}
