package com.taotao.cloud.core.heaven.util.lang;

/**
 * 字符工具类
 * @see Character#isDigit(char) 直接使用这个类的常见方法
 */
public final class CharUtil {

    private CharUtil(){}

    /**
     * 是否为空
     * @param character 字符
     * @return 是否
     */
    public static boolean isEmpty(final Character character) {
        return character == null;
    }

    /**
     * 是否不为空
     * @param character 字符
     * @return 是否
     */
    public static boolean isNotEmpty(final Character character) {
        return !isEmpty(character);
    }

    /**
     * 重复多次
     * @param unit 单元信息
     * @param times 次数
     * @return 结果
     */
    public static String repeat(final char unit, final int times) {
        String component = String.valueOf(unit);
        return StringUtil.repeat(component, times);
    }

    /**
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     *
     * 将字符串中的全角字符转为半角
     * @param c 原始字符
     * @return  转换之后的字符
     * @since 0.1.68
     */
    public static char toHalfWidth(final char c) {
        char resultChar = c;
        // 全角空格
        if (resultChar == 12288) {
            resultChar = (char) 32;
        // 其他全角字符
        } else if (resultChar > 65280 && resultChar < 65375) {
            resultChar = (char) (resultChar - 65248);
        }

        return resultChar;
    }

    /**
     *
     * 将半角字符转为全角
     *
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param c 原始字符
     * @return  转换之后的字符
     * @since 0.1.68
     */
    public static char toFullWidth(final char c) {
        char resultChar = c;
        // 半角空格
        if (resultChar == 32) {
            resultChar = (char) 12288;
            // 其他半角字符
        } else if (resultChar >= 33 && resultChar <= 126) {
            resultChar = (char) (resultChar + 65248);
        }

        return resultChar;
    }

    /**
     * 使用UnicodeScript方法判断是否为中文字符
     *
     * @param c 编码
     * @return 是否为中文标点符号
     * @since 0.1.68
     */
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        return sc == Character.UnicodeScript.HAN;
    }

    /**
     * 是否为空格
     * @param c char
     * @return 是否为空格
     * @since 0.1.68
     */
    public static boolean isSpace(final char c) {
        return Character.isSpaceChar(c) || '\u0013' == c;
    }

    /**
     * 是否不为空格
     * @param c char
     * @return 是否为空格
     * @since 0.1.129
     */
    public static boolean isNotSpace(final char c) {
        return !isSpace(c);
    }

    /**
     * 是数字或者英文字符
     * @param c 字符
     * @return 是否
     * @since 0.1.69
     */
    public static boolean isDigitOrLetter(final char c) {
        return Character.isDigit(c)
            || Character.isLowerCase(c)
            || Character.isUpperCase(c);
    }

    /**
     * 是否为组成邮箱的相关字符
     * @param c 字符
     * @return 是否
     * @since 0.1.69
     */
    public static boolean isEmilChar(final char c) {
        return CharUtil.isDigitOrLetter(c)
                || '_' == c || '-' == c
                || c == '.' || c == '@';
    }

    /**
     * 是否为网址字符
     * @param c 字符
     * @return 是否
     *
     * @since 0.0.12
     */
    public static boolean isWebSiteChar(final char c) {
        return CharUtil.isDigitOrLetter(c)
                || '-' == c || '.' == c;
    }

    /**
     * 是否为中文
     * @param ch 中文
     * @return 是否
     * @since 0.1.76
     */
    public static boolean isChinese(final char ch) {
        return ch >= 0x4E00 && ch <= 0x9FA5;
    }

    /**
     * 是否为英文
     * @param ch 英文
     * @return 是否
     * @since 0.1.76
     */
    public static boolean isEnglish(final char ch) {
        return (ch >= 0x0041 && ch <= 0x005A) || (ch >= 0x0061 && ch <= 0x007A);
    }

    /**
     * 是否为数字
     * @param ch 符号
     * @return 是否
     * @since 0.1.76
     */
    public static boolean isDigit(char ch) {
        return ch >= 0x0030 && ch <= 0x0039;
    }

    /**
     * 是否为 ascii 码
     * @param c 字符
     * @return 结果
     * @since 0.1.129
     */
    public static boolean isAscii(char c) {
        if(c > 127) {
            return false;
        }

        return true;
    }

    /**
     * 是否不为 ascii 码
     * @param c 字符
     * @return 结果
     * @since 0.1.129
     */
    public static boolean isNotAscii(char c) {
        return !isAscii(c);
    }

}
