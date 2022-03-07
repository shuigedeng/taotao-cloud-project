/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.util.lang;


import com.taotao.cloud.core.heaven.constant.CharConst;
import com.taotao.cloud.core.heaven.constant.CharsetConst;
import com.taotao.cloud.core.heaven.constant.PunctuationConst;
import com.taotao.cloud.core.heaven.response.exception.CommonRuntimeException;
import com.taotao.cloud.core.heaven.support.condition.ICondition;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import com.taotao.cloud.core.heaven.util.common.ArgUtil;
import com.taotao.cloud.core.heaven.util.guava.Guavas;
import com.taotao.cloud.core.heaven.util.lang.reflect.ClassTypeUtil;
import com.taotao.cloud.core.heaven.util.util.ArrayPrimitiveUtil;
import com.taotao.cloud.core.heaven.util.util.ArrayUtil;
import com.taotao.cloud.core.heaven.util.util.CollectionUtil;
import com.taotao.cloud.core.heaven.util.util.DateUtil;
import com.taotao.cloud.core.heaven.util.util.SystemUtil;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author bbhou
 * @since 0.0.1
 */
public final class StringUtil {

    /**
     * 大写的字母
     *
     * @since 0.1.66
     */
    public static final String LETTERS_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWSXYZ";

    /**
     * 小写的字母
     *
     * @since 0.1.66
     */
    public static final String LETTERS_LOWER = "abcdefghijklmnopqrstuvwsxyz";

    /**
     * 空白信息的表达式
     *
     * @since 0.1.98
     */
    private static final Pattern BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    private StringUtil() {
    }

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 空 json
     *
     * @since 0.1.80
     */
    public static final String EMPTY_JSON = "{}";

    /**
     * 空格
     */
    public static final String BLANK = " ";

    /**
     * 新行
     *
     * System.lineSeparator() 实际的文本效果是2行
     * @since 0.1.129
     */
    public static final String NEW_LINE = "";

    /**
     * 是否不为换行符
     * @param line 内容
     * @return 是否
     * @since 0.1.129
     */
    public static boolean isNotReturnLine(String line){
        return !isReturnLine(line);
    }

    /**
     * 是否为换行符
     * @param line 内容
     * @return 是否
     * @since 0.1.129
     */
    public static boolean isReturnLine(String line) {
        if(StringUtil.isEmpty(line)) {
            return true;
        }

        String trim = line.trim();
        if(StringUtil.isEmpty(trim)) {
            return true;
        }

        if(NEW_LINE.equals(line)) {
            return true;
        }

        return false;
    }

    /**
     * 是否全部为大写
     *
     * @param string 待检验字符
     * @return 是否为大写
     */
    public static boolean isUpperCase(final String string) {
        if (StringUtil.isEmpty(string)) {
            return false;
        }

        char[] characters = string.toCharArray();
        for (char c : characters) {
            if (!Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全部为小写
     *
     * @param string 待检验字符
     * @return 是否为大写
     */
    public static boolean isLowerCase(final String string) {
        if (StringUtil.isEmpty(string)) {
            return false;
        }

        char[] characters = string.toCharArray();
        for (char c : characters) {
            if (!Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含大写字母
     *
     * @param string 待检验字符
     * @return 是否为大写
     */
    public static boolean containsUppercase(final String string) {
        if (StringUtil.isEmpty(string)) {
            return false;
        }

        char[] characters = string.toCharArray();
        for (char c : characters) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含小写字母
     *
     * @param string 待检验字符
     * @return 是否为大写
     */
    public static boolean containsLowercase(final String string) {
        if (StringUtil.isEmpty(string)) {
            return false;
        }

        char[] characters = string.toCharArray();
        for (char c : characters) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否全部由字母组成
     * 1. 大写字母
     * 2. 小写字母
     *
     * @param string 字符串
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isLetter(final String string) {
        return isCharsCondition(string, new ICondition<Character>() {
            @Override
            public boolean condition(Character character) {
                return Character.isLowerCase(character)
                        || Character.isUpperCase(character);
            }
        });
    }

    /**
     * 是否全部为数字
     *
     * @param string 字符串
     * @return 是否为数字
     * @since 0.1.68
     */
    public static boolean isDigit(final String string) {
        return isCharsCondition(string, new ICondition<Character>() {
            @Override
            public boolean condition(Character character) {
                return Character.isDigit(character);
            }
        });
    }

    /**
     * 是否全部为数字或者字母
     *
     * @param string 字符串
     * @return 是否数字或者字母
     * @since 0.1.68
     */
    public static boolean isDigitOrLetter(final String string) {
        return isCharsCondition(string, new ICondition<Character>() {
            @Override
            public boolean condition(Character character) {
                return CharUtil.isDigitOrLetter(character);
            }
        });
    }

    /**
     * 字符串是否全部满足某一个条件
     *
     * @param string    原始字符串
     * @param condition 条件
     * @return 是否满足
     * @since 0.1.68
     */
    private static boolean isCharsCondition(final String string,
                                            final ICondition<Character> condition) {
        if (StringUtil.isEmpty(string)) {
            return false;
        }

        char[] chars = string.toCharArray();
        for (char c : chars) {
            if (!condition.condition(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为空
     *
     * @param string 字符串
     * @return {@code true} 为空
     */
    public static boolean isEmpty(final String string) {
        return null == string || EMPTY.equals(string);
    }

    /**
     * 是否为空-进行 trim 之后
     *
     * @param string 原始字符串
     * @return 是否
     * @since 0.1.71
     */
    public static boolean isEmptyTrim(final String string) {
        if (isEmpty(string)) {
            return true;
        }

        String trim = trim(string);
        return isEmpty(trim);
    }

    /**
     * 是否不为空-进行 trim 之后
     *
     * @param string 原始字符串
     * @return 是否
     * @since 0.1.102
     */
    public static boolean isNotEmptyTrim(final String string) {
        return !isEmptyTrim(string);
    }

    /**
     * 是否为空的 json
     *
     * @param json json 信息
     * @return 是否
     * @since 0.1.80
     */
    public static boolean isEmptyJson(final String json) {
        if (isEmptyTrim(json)) {
            return true;
        }

        String trim = json.trim();
        return EMPTY_JSON.equals(trim);
    }

    /**
     * 是否为非空
     *
     * @param string 字符串
     * @return {@code true} 为非空
     */
    public static boolean isNotEmpty(final String string) {
        return !isEmpty(string);
    }

    /**
     * 是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 是否不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 根据任意多的空格进行分割字符串。
     * 1. 入参为空,则返回空字符串数组
     *
     * @param string 字符串
     * @return 割字符串数组
     */
    public static String[] splitByAnyBlank(final String string) {
        if (StringUtil.isEmpty(string)) {
            return new String[0];
        }

        final String pattern = "\\s+|\u0013";
        return string.split(pattern);
    }

    /**
     * 过滤掉所有的空格
     * （1）trim
     * （2）移除所有的空格
     *
     * @param string 原始字符串
     * @return 过滤后的内容
     * @since 0.1.68
     */
    public static String trimAnyBlank(final String string) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }

        String trim = string.trim();
        return trim.replaceAll("\\s+|\u0013", "");
    }

    /**
     * 替换掉任意空格
     *
     * @param string      原始字符串
     * @param replacement 待替换的文本
     * @return 结果
     * @since 0.1.98
     */
    public static String replaceAnyBlank(final String string,
                                         final String replacement) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }

        Matcher m = BLANK_PATTERN.matcher(string);
        String result = m.replaceAll(replacement);
        //160 &nbsp;
        result = result.replaceAll("\\u00A0", replacement);
        return result;
    }

    /**
     * 替换掉任意空格为空
     *
     * @param string 原始字符串
     * @return 结果
     * @since 0.1.98
     */
    public static String replaceAnyBlank(final String string) {
        return replaceAnyBlank(string, StringUtil.EMPTY);
    }

    /**
     * 过滤掉所有的标点符号
     * （1）trim
     * （2）移除标点符号
     * （3）移除 symbol
     *
     * @param string 原始字符串
     * @return 过滤后的内容
     * @since 0.1.68
     */
    public static String trimAnyPunctionAndSymbol(final String string) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }

        String trim = string.trim();
        return trim.replaceAll("\\p{P}|\\p{S}", "");
    }

    /**
     * 获取的驼峰写法。
     * 1.这是 mybatis-gen 源码
     *
     * @param inputString             输入字符串
     * @param firstCharacterUppercase 首字母是否大写。
     * @return 驼峰写法
     */
    public static String getCamelCaseString(String inputString, boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;

                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 首字母小写字符串
     */
    public static String firstToLowerCase(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写结果
     */
    public static String firstToUpperCase(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 默认为 “”
     * 1. 如果为 null TO ""
     * 2. 返回本身
     *
     * @param string 字符串
     * @return 非 null 的字符串
     */
    public static String defaultEmpty(final String string) {
        if (isEmpty(string)) {
            return EMPTY;
        }
        return string;
    }

    /**
     * 将数组进行逗号连接
     *
     * @param array object array
     * @return join string
     * @since 0.1.46
     */
    public static String join(Object... array) {
        return join(array, PunctuationConst.COMMA);
    }

    /**
     * 将数组进行连接
     *
     * @param array     object array
     * @param separator 分隔符
     * @return join string
     * @see #join(Object[], String, int, int) 核心实现
     * @since 0.1.14
     */
    public static String join(Object[] array, String separator) {
        final int endIndex = ArrayUtil.getEndIndex(-1, array);
        return join(array, separator, 0, endIndex);
    }

    /**
     * 拼接
     * @param splitter 拼接符
     * @param objects 结果
     * @return 结果
     * @since 0.1.153
     */
    public static String join(String splitter, Object... objects) {
        return join(objects, splitter);
    }

    /**
     * 将数组进行连接
     * from:    apache lang3
     *
     * @param array      object array
     * @param separator  分隔符
     * @param startIndex 开始下标
     * @param endIndex   结束下标
     * @return join string
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }

        if (separator == null) {
            separator = "";
        }

        int noOfItems = endIndex - startIndex;
        if (noOfItems < 0) {
            return "";
        } else {
            StringBuilder buf = new StringBuilder(noOfItems * 16);

            for (int i = startIndex; i <= endIndex; ++i) {
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

    /**
     * 字符串拼接
     * (1) v0.1.14 将其范围扩展到对象列表
     * 注意：如果有 null 属性，会导致直接报错。此处不再处理。
     *
     * @param collection 集合列表
     * @param splitter   分隔符
     * @param startIndex 开始下标
     * @param endIndex   结束下标
     * @param <E>        泛型
     * @return 结果
     * @since 0.1.14
     */
    public static <E> String join(final Collection<E> collection, final String splitter,
                                  final int startIndex, final int endIndex) {
        if (CollectionUtil.isEmpty(collection)) {
            return StringUtil.EMPTY;
        }

        final String actualSplitter = StringUtil.nullToDefault(splitter, StringUtil.EMPTY);
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<E> iterator = collection.iterator();
        // 循环直到 startIndex
        for (int i = 0; i < startIndex; i++) {
            iterator.next();
        }
        stringBuilder.append(iterator.next().toString());
        for (int i = startIndex; i < endIndex; i++) {
            stringBuilder.append(actualSplitter).append(iterator.next().toString());
        }
        return stringBuilder.toString();
    }

    /**
     * 字符串拼接
     * (1) v0.1.14 将其范围扩展到对象列表
     * 注意：如果有 null 属性，会导致直接报错。此处不再处理。
     *
     * @param collection 集合信息
     * @param splitter   分隔符
     * @param <E>        泛型
     * @return 结果
     * @since 0.1.14
     */
    public static <E> String join(final Collection<E> collection, final String splitter) {
        final int endIndex = CollectionUtil.getEndIndex(-1, collection);
        return join(collection, splitter, 0, endIndex);
    }

    /**
     * 字符串按逗号拼接拼接
     *
     * @param collection 集合信息
     * @param <E>        泛型
     * @return 结果
     * @since 0.1.46
     */
    public static <E> String join(final Collection<E> collection) {
        return join(collection, PunctuationConst.COMMA);
    }

    /**
     * 驼峰命名转下划线
     *
     * @param camelStr 驼峰字符串
     * @return 下划线字符串
     */
    public static String camelToUnderline(String camelStr) {
        if (StringUtil.isEmpty(camelStr)) {
            return StringUtil.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        char[] chars = camelStr.toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 下划线转驼峰命名
     *
     * @param underlineStr 下划线字符串
     * @return 驼峰字符串
     */
    public static String underlineToCamel(String underlineStr) {
        if (StringUtil.isEmpty(underlineStr)) {
            return StringUtil.EMPTY;
        }

        int len = underlineStr.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = underlineStr.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(underlineStr.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 重复多少次
     *
     * @param component 组成信息
     * @param times     重复次数
     * @return 重复多次的字符串结果
     */
    public static String repeat(final String component, final int times) {
        if (StringUtil.isEmpty(component)
                || times <= 0) {
            return StringUtil.EMPTY;
        }

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < times; i++) {
            stringBuffer.append(component);
        }

        return stringBuffer.toString();
    }

    /**
     * 构建新的字符串
     *
     * @param original     原始对象
     * @param middle       中间隐藏信息
     * @param prefixLength 前边信息长度
     * @return 构建后的新字符串
     * @since 0.0.8
     */
    public static String buildString(final Object original,
                                     final String middle,
                                     final int prefixLength) {
        if (ObjectUtil.isNull(original)) {
            return null;
        }

        final String string = original.toString();
        final int stringLength = string.length();

        String prefix = "";
        String suffix = "";

        if (stringLength >= prefixLength) {
            prefix = string.substring(0, prefixLength);
        } else {
            prefix = string.substring(0, stringLength);
        }

        int suffixLength = stringLength - prefix.length() - middle.length();
        if (suffixLength > 0) {
            suffix = string.substring(stringLength - suffixLength);
        }

        return prefix + middle + suffix;
    }

    /**
     * 过滤掉空格
     *
     * @param original 原始字符串
     * @return 过滤后的字符串
     * @since 0.1.0
     */
    public static String trim(final String original) {
        if (StringUtil.isEmpty(original)) {
            return original;
        }
        return original.trim();
    }

    /**
     * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
     *
     * <pre>
     * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     * @since 0.1.0
     */
    public static String nullToDefault(CharSequence str, String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }

    /**
     * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串
     *
     * @param str        被填充的字符串
     * @param filledChar 填充的字符
     * @param len        填充长度
     * @param isPre      是否填充在前
     * @return 填充后的字符串
     * @since 0.1.0
     */
    public static String fill(String str, char filledChar, int len, boolean isPre) {
        final int strLen = str.length();
        if (strLen > len) {
            return str;
        }

        String filledStr = StringUtil.repeat(String.valueOf(filledChar), len - strLen);
        return isPre ? filledStr.concat(str) : str.concat(filledStr);
    }

    /**
     * 对象转换为字符串
     * 1. 对数组特殊处理 {@link Arrays#toString(Object[])} 避免打印无意义的信息（v0.1.14）
     *
     * @param object          对象
     * @param defaultWhenNull 对象为空时的默认值
     * @return 结果
     * @since 0.1.5
     */
    public static String objectToString(final Object object,
                                        final String defaultWhenNull) {
        if (ObjectUtil.isNull(object)) {
            return defaultWhenNull;
        }
        Class type = object.getClass();
        if (ClassTypeUtil.isArray(type)) {
            Object[] arrays = (Object[]) object;
            return Arrays.toString(arrays);
        }
        return object.toString();
    }

    /**
     * 对象转换为字符串
     * 1. 默认为空时返回 null
     *
     * @param object 对象
     * @return 结果
     * @since 0.1.5
     */
    public static String objectToString(final Object object) {
        return objectToString(object, null);
    }

    /**
     * 对 single 的信息重复多次
     *
     * @param single 单个字符
     * @param times  重复次数
     * @return 结果
     * @see #repeat(String, int) 重复
     * @since 0.1.9
     */
    @Deprecated
    public static String times(final String single,
                               final int times) {
        if (StringUtil.isEmpty(single)) {
            return single;
        }
        if (times <= 0) {
            return single;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            stringBuilder.append(single);
        }
        return stringBuilder.toString();
    }

    /**
     * 首字母大写
     *
     * @param string 字符串
     * @return 大写的结果
     * @since 0.1.11
     */
    public static String capitalFirst(final String string) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }

        if (string.length() <= 1) {
            return string.toUpperCase();
        }

        char capitalChar = Character.toUpperCase(string.charAt(0));
        return capitalChar + string.substring(1);
    }

    /**
     * 严格拆分
     * 【传统拆分】
     * 1:2:3:31::32:4 结果是：[1, 2, 3, 31, , 32, 4]
     * <p>
     * 【严格拆分】
     * 严格匹配 : 拆分符，如果有多个，则不进行拆分。
     * 结果：[1, 2, 3, 31::32, 4]
     * <p>
     * 实现逻辑：
     * （1）根据 index 获取所有的下标。+length（当前步长）
     * （2）获取当前的所有拆分下标，获取 times+1 的拆分下标
     * （3）从当前下标中移除 times+1 的下标。并且移除连续的信息。
     * 连续：times+1 的下标，后续的 times 步长。如果不连续，则中断。
     * （4）根据过滤后的列表生成最后的结果。
     *
     * @param string    原始字符串
     * @param splitUnit 分隔单元
     * @param times     次数
     * @return 结果
     * @since 0.1.16
     */
    public static List<String> splitStrictly(final String string,
                                             final char splitUnit,
                                             final int times) {
        if (StringUtil.isEmpty(string)) {
            return Collections.emptyList();
        }
        if (times <= 0) {
            return Collections.singletonList(string);
        }

        // 分别获取索引列表
        final String split = CharUtil.repeat(splitUnit, times);
        final String moreSplit = CharUtil.repeat(splitUnit, times + 1);
        final List<Integer> splitIndexList = getIndexList(string, split);
        final List<Integer> moreSplitIndexList = getIndexList(string, moreSplit);

        // 移除重复下标
        final List<Integer> removeIndexList = getSerialFilterList(splitIndexList, moreSplitIndexList, times);

        // 构建结果列表
        Collection<Integer> trimIndexList = CollectionUtil.difference(splitIndexList, removeIndexList);
        return subStringList(string, trimIndexList, times);
    }

    /**
     * 获取满足条件连续的列表
     * （1）当前信息
     * （2）连续的索引信息
     *
     * @param allList    所有的整数
     * @param filterList 待排除的整数
     * @param step       步长
     * @return 结果列表
     */
    private static List<Integer> getSerialFilterList(final List<Integer> allList,
                                                     final List<Integer> filterList,
                                                     final int step) {
        List<Integer> resultList = Guavas.newArrayList();

        resultList.addAll(filterList);
        // 根据 index+times 为步长进行连续判断。不存在则跳过
        for (Integer filter : filterList) {
            // 从匹配的下一个元素开始
            final int startIndex = allList.indexOf(filter) + 1;
            int stepTimes = 1;
            for (int i = startIndex; i < allList.size(); i++) {
                final Integer indexVal = allList.get(i);
                final int nextVal = step * stepTimes + filter;
                if (indexVal.equals(nextVal)) {
                    resultList.add(nextVal);
                } else {
                    // 跳出当前循环
                    break;
                }
                stepTimes++;
            }
        }

        return resultList;
    }

    /**
     * 根据下标截取列表
     * <p>
     * 【最后的截取问题】
     * 最后构建的结果：
     * string=1::2::3:31:::32::4:
     * index=[1,4,15]
     * ignore=2
     * <p>
     * 每次截取：
     * [0,1)
     * [1+2,4)
     * [15+2,]
     *
     * @param string          原始字符串
     * @param indexCollection 下标列表
     * @param ignoreLength    每次忽略跳过的长度。用于跳过 split 字符。
     * @return 结果列表
     * @since 0.1.16
     */
    public static List<String> subStringList(final String string,
                                             final Collection<Integer> indexCollection,
                                             final int ignoreLength) {
        if (StringUtil.isEmpty(string)) {
            return Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(indexCollection)) {
            return Collections.singletonList(string);
        }

        List<String> resultList = Guavas.newArrayList(indexCollection.size());
        int startIndex = 0;
        for (Integer index : indexCollection) {
            // 最后的位置添加空字符串
            if (startIndex > string.length() - 1) {
                resultList.add(StringUtil.EMPTY);
                break;
            }
            String subString = string.substring(startIndex, index);
            resultList.add(subString);
            startIndex = index + ignoreLength;
        }
        // 最后的结果信息
        if (startIndex < string.length()) {
            String subString = string.substring(startIndex);
            resultList.add(subString);
        }

        return resultList;
    }

    /**
     * 获取所有符合条件的下标类表
     * 【下标】
     * 1:2:3:31::32:4:
     * <p>
     * [1, 3, 5, 8, 9, 12, 14]
     * <p>
     * 问题：这个下标没有过滤 split。
     * 如果想过滤分隔符，应该如下：
     * (0,1)
     * (1+split.length, 3)
     * ...
     * 1,2,
     *
     * @param string 原始字符串
     * @param split  分隔字符串
     * @return 下标列表
     * @since 0.1.16
     */
    public static List<Integer> getIndexList(final String string,
                                             final String split) {
        if (StringUtil.isEmpty(string)
                || StringUtil.isEmpty(split)) {
            return Collections.emptyList();
        }

        List<Integer> indexList = Guavas.newArrayList();
        int startIndex = 0;
        while (startIndex < string.length()) {
            startIndex = string.indexOf(split, startIndex);
            if (startIndex < 0) {
                break;
            }
            indexList.add(startIndex);
            startIndex += split.length();
        }
        return indexList;
    }


    /**
     * 获取字符串对应的下标信息
     *
     * @param string             字符串
     * @param symbol             分隔符
     * @param ignoreDoubleQuotes 是否忽略双引号中的信息
     * @return 结果列表
     * @since 0.1.27
     */
    public static List<Integer> getIndexList(final String string,
                                             final char symbol,
                                             final boolean ignoreDoubleQuotes) {
        if (StringUtil.isEmpty(string)) {
            return Collections.emptyList();
        }

        List<Integer> resultList = Guavas.newArrayList();
        char[] chars = string.toCharArray();

        boolean doubleQuotesStart = false;
        char preChar = CharConst.BLANK;
        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];

            preChar = getPreChar(preChar, currentChar);
            // 上一个字符不是转义，且当前为 "。则进行状态的切换
            if (CharConst.BACK_SLASH != preChar
                    && CharConst.DOUBLE_QUOTES == currentChar) {
                doubleQuotesStart = !doubleQuotesStart;
            }

            // 等于且不在双引号中。
            if (currentChar == symbol) {
                // 忽略双引号中的信息 && 不在双引号中。
                if (ignoreDoubleQuotes) {
                    if (!doubleQuotesStart) {
                        resultList.add(i);
                    }
                } else {
                    resultList.add(i);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取上一个字符
     * <p>
     * 保证转义字符的两次抵消。
     *
     * @param preChar     上一个字符
     * @param currentChar 当前字符
     * @return 结果
     * @since 0.1.27
     */
    @Deprecated
    private static char getPreChar(final char preChar, final char currentChar) {
        // 判断前一个字符是什么
        if (CharConst.BACK_SLASH == preChar
                && CharConst.BACK_SLASH == currentChar) {
            return CharConst.BLANK;
        }
        return currentChar;
    }

    /**
     * 根据索引下标直接拆分
     *
     * @param string    原始字符串
     * @param indexList 结果列表
     * @return 结果
     * @since 0.1.27
     */
    public static List<String> splitByIndexes(final String string, final List<Integer> indexList) {
        if (StringUtil.isEmpty(string)) {
            return Collections.emptyList();
        }
        if (CollectionUtil.isEmpty(indexList)) {
            return Collections.singletonList(string);
        }

        List<String> resultList = Guavas.newArrayList(indexList.size() + 1);

        int preIndex = 0;
        for (Integer anIndexList : indexList) {
            int currentIndex = anIndexList;
            if (currentIndex > preIndex) {
                resultList.add(string.substring(preIndex, currentIndex));
            }
            preIndex = currentIndex + 1;
        }
        // 判断最后一个下标
        final int lastIndex = indexList.get(indexList.size() - 1);
        if (lastIndex + 1 < string.length()) {
            resultList.add(string.substring(lastIndex + 1));
        }
        return resultList;
    }

    /**
     * 字符串转字节数组
     *
     * @param string 字符串
     * @return 字节数组
     * @since 0.1.35
     */
    public static byte[] stringToBytes(final String string) {
        if (ObjectUtil.isNull(string)) {
            return null;
        }

        return string.getBytes();
    }

    /**
     * 字节数组转字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     * @since 0.1.35
     */
    public static String bytesToString(final byte[] bytes) {
        if (ArrayPrimitiveUtil.isEmpty(bytes)) {
            return null;
        }

        return new String(bytes);
    }

    /**
     * 拆分为字符串数组
     *
     * @param string   字符串
     * @param splitter 拆分符号
     * @return 字符串数组
     * @since 0.1.46
     */
    public static String[] splitToStringArray(final String string, final String splitter) {
        if (StringUtil.isEmpty(string)) {
            return null;
        }

        return string.split(splitter);
    }

    /**
     * 拆分为字符串数组
     *
     * @param string 字符串
     * @return 字符串数组
     * @since 0.1.46
     */
    public static String[] splitToStringArray(final String string) {
        return splitToStringArray(string, PunctuationConst.COMMA);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final byte[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Byte> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Byte, Byte>() {
            @Override
            public Byte handle(Byte value) {
                return value;
            }
        });
        return join(lists, splitter);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final char[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Character> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Character, Character>() {
            @Override
            public Character handle(Character value) {
                return value;
            }
        });
        return join(lists, splitter);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final short[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Short> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Short, Short>() {
            @Override
            public Short handle(Short value) {
                return value;
            }
        });
        return join(lists, splitter);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final long[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Long> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Long, Long>() {
            @Override
            public Long handle(Long value) {
                return value;
            }
        });
        return join(lists, splitter);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final float[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Float> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Float, Float>() {
            @Override
            public Float handle(Float value) {
                return value;
            }
        });
        return join(lists, splitter);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final double[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Double> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Double, Double>() {
            @Override
            public Double handle(Double value) {
                return value;
            }
        });
        return join(lists, splitter);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final boolean[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Boolean> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Boolean, Boolean>() {
            @Override
            public Boolean handle(Boolean value) {
                return value;
            }
        });
        return join(lists, splitter);
    }

    /**
     * 数组拼接为字符串
     *
     * @param array     数组
     * @param splitters 分隔符
     * @return 拼接结果
     * @since 0.1.49
     */
    public static String join(final int[] array, final String... splitters) {
        if (ArrayPrimitiveUtil.isEmpty(array)) {
            return StringUtil.EMPTY;
        }

        String splitter = getSplitter(splitters);
        List<Integer> integers = ArrayPrimitiveUtil.toList(array, new IHandler<Integer, Integer>() {
            @Override
            public Integer handle(Integer integer) {
                return integer;
            }
        });
        return join(integers, splitter);
    }

    /**
     * 获取指定的分隔符
     *
     * @param splitters 分隔符
     * @return 字符串
     * @since 0.1.49
     */
    private static String getSplitter(final String... splitters) {
        if (ArrayUtil.isEmpty(splitters)) {
            return PunctuationConst.COMMA;
        }

        return splitters[0];
    }

    /**
     * 拆分为列表
     *
     * @param string   字符串
     * @param splitter 分隔符号
     * @return 字符串列表
     * @since 0.1.49
     */
    public static List<String> splitToList(final String string,
                                           final String splitter) {
        ArgUtil.notEmpty(splitter, "splitter");

        if (StringUtil.isEmpty(string)) {
            return Guavas.newArrayList();
        }

        String[] strings = string.split(splitter);
        return ArrayUtil.toList(strings);
    }

    /**
     * 拆分为列表
     *
     * @param string 字符串
     * @return 字符串列表
     * @since 0.1.49
     */
    public static List<String> splitToList(final String string) {
        return splitToList(string, PunctuationConst.COMMA);
    }


    /**
     * 转换为数组字符
     *
     * @param string 字符串
     * @return 结果
     * @since 0.1.66
     */
    public static Character[] toCharacterArray(final String string) {
        final char[] chars = string.toCharArray();
        Character[] newArray = new Character[chars.length];

        for (int i = 0; i < chars.length; i++) {
            newArray[i] = chars[i];
        }

        return newArray;
    }

    /**
     * 转换为列表字符
     *
     * @param string 字符串
     * @return 结果
     * @since 0.1.66
     */
    public static List<Character> toCharacterList(final String string) {
        final char[] chars = string.toCharArray();
        List<Character> newList = new ArrayList<>(chars.length);

        for (char aChar : chars) {
            newList.add(aChar);
        }

        return newList;
    }

    /**
     * 转换为 char 字符串列表
     *
     * @param string 字符串
     * @return 字符串列表
     * @since 0.1.74
     */
    public static List<String> toCharStringList(final String string) {
        if (StringUtil.isEmpty(string)) {
            return Guavas.newArrayList();
        }

        char[] chars = string.toCharArray();
        return ArrayPrimitiveUtil.toList(chars, new IHandler<Character, String>() {
            @Override
            public String handle(Character character) {
                return String.valueOf(character);
            }
        });
    }

    /**
     * 将字符串中的全角字符转为半角
     *
     * @param string 字符串
     * @return 转换之后的字符串
     * @since 0.1.68
     */
    public static String toHalfWidth(final String string) {
        return characterHandler(string, new IHandler<Character, Character>() {
            @Override
            public Character handle(Character character) {
                return CharUtil.toHalfWidth(character);
            }
        });
    }

    /**
     * 将字符串中的半角字符转为全角
     *
     * @param string 字符串
     * @return 转换之后的字符串
     * @since 0.1.68
     */
    public static String toFullWidth(final String string) {
        return characterHandler(string, new IHandler<Character, Character>() {
            @Override
            public Character handle(Character character) {
                return CharUtil.toFullWidth(character);
            }
        });
    }

    /**
     * 字符的处理
     *
     * @param string  字符串
     * @param handler 处理类
     * @return 结果
     * @since 0.1.68
     */
    private static String characterHandler(final String string,
                                           final IHandler<Character, Character> handler) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }

        //1. 转换为列表
        char[] chars = string.toCharArray();
        char[] resultChars = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            resultChars[i] = handler.handle(chars[i]);
        }

        //2. 构建结果
        return new String(resultChars);
    }

    /**
     * 过滤掉非中文字符
     *
     * @param string 字符串
     * @return 结果
     * @since 0.1.79
     */
    public static String trimNotChinese(final String string) {
        if (StringUtil.isEmptyTrim(string)) {
            return StringUtil.EMPTY;
        }

        char[] chars = string.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : chars) {
            if (CharUtil.isChinese(character)) {
                stringBuilder.append(character);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 避免默认实现的问题
     *
     * @param object 对象
     * @return 结果
     * @see String#valueOf(Object) 默认实现会把 null 转换为 "null"
     * @since 0.1.102
     */
    public static String valueOf(final Object object) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }

        return String.valueOf(object);
    }

    /**
     * 左补信息
     *
     * @param original     原始字符串
     * @param targetLength 目标长度
     * @param unit         补的元素
     * @return 结果
     * @since 0.1.104
     */
    public static String leftPadding(final String original,
                                     final int targetLength,
                                     final char unit) {
        ArgUtil.notNull(original, "original");

        //1. fast-return
        final int originalLength = original.length();
        if (originalLength >= targetLength) {
            return original;
        }

        //2. 循环补零
        StringBuilder stringBuilder = new StringBuilder(targetLength);
        for (int i = originalLength; i < targetLength; i++) {
            stringBuilder.append(unit);
        }
        stringBuilder.append(original);

        return stringBuilder.toString();
    }

    /**
     * 左补信息
     * 默认左补零 0
     *
     * @param original     原始字符串
     * @param targetLength 目标长度
     * @return 结果
     * @since 0.1.104
     */
    public static String leftPadding(final String original,
                                     final int targetLength) {
        return leftPadding(original, targetLength, '0');
    }

    /**
     * 获取第一个字符
     * @param text 文本
     * @return 结果
     * @since 0.1.122
     */
    public static Character getFirstChar(final String text) {
        if(StringUtil.isEmpty(text)) {
            return null;
        }

        return text.charAt(0);
    }

    /**
     * 空转换为 null
     * @param text 文本
     * @return 结果
     * @since 0.1.123
     */
    public static String emptyToNull(String text) {
        if(StringUtil.isEmpty(text)) {
            return null;
        }
        return text;
    }

    /**
     * 转换为 boolean 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Boolean toBool(final String text) {
        return "YES".equalsIgnoreCase(text)
                || "TRUE".equalsIgnoreCase(text)
                || "1".equalsIgnoreCase(text);
    }

    /**
     * 转换为 boolean 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Character toChar(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return text.charAt(0);
    }

    /**
     * 转换为 Byte 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Byte toByte(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return Byte.valueOf(text);
    }

    /**
     * 转换为 Short 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Short toShort(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return Short.valueOf(text);
    }

    /**
     * 转换为 Integer 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Integer toInt(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return Integer.valueOf(text);
    }

    /**
     * 转换为 Long 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Long toLong(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return Long.valueOf(text);
    }

    /**
     * 转换为 Float 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Float toFloat(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return Float.valueOf(text);
    }

    /**
     * 转换为 Float 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Double toDouble(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return Double.valueOf(text);
    }

    /**
     * 转换为 BigInteger 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static BigInteger toBigInteger(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return new BigInteger(text);
    }

    /**
     * 转换为 BigDecimal 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static BigDecimal toBigDecimal(final String text) {
        if(isEmpty(text)) {
            return null;
        }

        return new BigDecimal(text);
    }

    /**
     * 转换为 Date 类型
     * @param text 文本
     * @param dateFormat 格式化
     * @return 结果
     * @since 0.1.124
     */
    public static Date toDate(final String text, final String dateFormat) {
        if(isEmpty(text)) {
            return null;
        }

        return DateUtil.getFormatDate(text, dateFormat);
    }

    /**
     * 转换为 BigDecimal 类型
     * @param text 文本
     * @return 结果
     * @since 0.1.124
     */
    public static Date toDate(final String text) {
        return toDate(text, DateUtil.PURE_DATE_FORMAT);
    }

    /**
     * 转换为字符串
     * @param date 日期
     * @param format 格式化
     * @return 结果
     * @since 0.1.124
     */
    public static String toString(Date date, String format) {
        return DateUtil.getDateFormat(date, format);
    }

    /**
     * 转换为字符串
     * @param date 日期
     * @return 结果
     * @since 0.1.124
     */
    public static String toString(Date date) {
        return toString(date, DateUtil.PURE_DATE_FORMAT);
    }

    /**
     * 转换为字符串
     * @param object 对象
     * @return 结果
     * @since 0.1.124
     */
    public static String toString(Object object) {
        if(null == object) {
            return null;
        }

        return object.toString();
    }

    /**
     * 转换为字符串
     * @param bytes 字节
     * @param charset 编码
     * @return 结果
     * @since 0.1.130
     */
    public static String toString(byte[] bytes, String charset) {
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 转换为字符串
     * @param bytes 字节
     * @return 结果
     * @since 0.1.130
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, CharsetConst.UTF8);
    }

    /**
     * 转换为 bytes
     * @param text 文本
     * @param charset 编码
     * @return 结果
     * @since 0.1.130
     */
    public static byte[] getBytes(String text, String charset) {
        try {
            return text.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 转换为 bytes
     * @param text 文本
     * @return 结果
     * @since 0.1.130
     */
    public static byte[] getBytes(String text) {
        return getBytes(text, CharsetConst.UTF8);
    }

    /**
     * 是否全部是英文
     * @param text 文本
     * @return 结果
     * @since 0.1.132
     */
    public static boolean isEnglish(String text) {
        if(StringUtil.isEmpty(text)) {
            return false;
        }

        char[] chars = text.toCharArray();
        for(char c : chars) {
            if(!CharUtil.isEnglish(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否全部是中文
     * @param text 文本
     * @return 结果
     * @since 0.1.132
     */
    public static boolean isChinese(String text) {
        if(StringUtil.isEmpty(text)) {
            return false;
        }

        char[] chars = text.toCharArray();
        for(char c : chars) {
            if(!CharUtil.isChinese(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 包信息调整为路径信息
     * @param packageName 包信息
     * @return 結果
     * @since 0.1.141
     */
    public static String packageToPath(String packageName) {
        if(StringUtil.isEmpty(packageName)) {
            return packageName;
        }

        return packageName.replaceAll("\\.", "/");
    }

    /**
     * 字符串截取
     * @param text 文本
     * @param startIndex 开始位置
     * @param length 长度
     * @return 结果
     * @since 0.1.142
     */
    public static String subString(String text, int startIndex, int length) {
        if(StringUtil.isEmpty(text)) {
            return text;
        }

        // 长度
        if(length <= 0) {
            return null;
        }

        //避免越界
        int endIndex = startIndex + length;
        if(endIndex > text.length()) {
            endIndex = text.length();
        }

        return text.substring(startIndex, endIndex);
    }

    /**
     * 在不同的操作系统中，对换号符的定义是不同的，比如：
     *
     * 1. \n unix,linux系统，好像新的mac也是这样的。
     *
     * 2. \r 有的mac系统
     *
     * 3. \r\n window系统。
     *
     * 自己观察，你会发现规律，其实用一个正则表达式就可以满足： \r?\n
     *
     * @param content 内容
     * @return 结果
     * @since 0.1.143
     */
    public static List<String> contentToLines(String content) {
        if(content == null) {
            return null;
        }

        // 根据换行符分割
        String[] strings = content.split("\\r?\\n");
        return ArrayUtil.toList(strings);
    }

    /**
     * 字符串按照换行符拼接为新的内容
     * @param lines 行
     * @return 结果
     * @since 0.1.143
     */
    public static String linesToContent(List<String> lines) {
        if(CollectionUtil.isEmpty(lines)) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < lines.size()-1; i++) {
            stringBuilder.append(lines.get(i))
                    .append(SystemUtil.getLineSeparator());
        }

        stringBuilder.append(lines.get(lines.size()-1));

        return stringBuilder.toString();
    }

    /**
     * 根据长度进行文本截断
     * @param text 文本
     * @param limitSize 限制长度
     * @return 结果列表
     * @since 0.1.149
     */
    public static List<String> splitByLength(String text, int limitSize) {
        if(StringUtil.isEmpty(text)) {
            return Collections.emptyList();
        }

        final int totalLength = text.length();

        int times = totalLength / limitSize;
        if(totalLength % limitSize != 0) {
            times++;
        }

        List<String> resultList = new ArrayList<>(times);

        for(int i = 0; i < times; i++) {
            int startIndex = i * limitSize;
            int endIndex = (i+1) * limitSize;

            // 越界处理
            if(endIndex > totalLength) {
                endIndex = totalLength;
            }
            String subText = text.substring(startIndex, endIndex);
            resultList.add(subText);
        }

        return resultList;
    }



}
