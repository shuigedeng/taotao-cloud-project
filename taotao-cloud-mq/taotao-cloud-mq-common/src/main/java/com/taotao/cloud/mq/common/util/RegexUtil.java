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

package com.taotao.cloud.mq.common.util;

import com.taotao.boot.common.utils.collection.CollectionUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * @author shuigedeng
 * @since 0.1.17
 */
public final class RegexUtil {

    private RegexUtil() {}

    /**
     * 特殊字符
     */
    private static final String[] SPECIAL_CHARS = {
        "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"
    };

    /**
     * 标点符号正则
     *
     * P 其中的小写 p 是 property 的意思，表示 Unicode 属性，用于 Unicode 正表达式的前缀。
     *
     * 等价于：
     *
     * <pre>
     * Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
     * </pre>
     * 大写 P 表示 Unicode 字符集七个字符属性之一：标点字符。
     * 其他六个是
     * L：字母；
     * M：标记符号（一般不会单独出现）；
     * Z：分隔符（比如空格、换行等）；
     * S：符号（比如数学符号、货币符号等）；
     * N：数字（比如阿拉伯数字、罗马数字等）；
     * C：其他字符
     *
     * 相关信息：
     * http://www.unicode.org/reports/tr18/
     * http://www.unicode.org/Public/UNIDATA/UnicodeData.txt
     *
     * @since 0.1.68
     */
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("\\p{P}");

    /**
     * 字母-正则模式
     * @since 0.1.68
     */
    private static final Pattern LETTER_PATTERN = Pattern.compile("\\p{L}");

    /**
     * 标记性-正则模式
     * @since 0.1.68
     */
    private static final Pattern MARKABLE_PATTERN = Pattern.compile("\\p{M}");

    /**
     * 分隔符-正则模式
     *
     * 空格、换行等
     * @since 0.1.68
     */
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("\\p{Z}");

    /**
     * 符号-正则模式
     *
     * 数学符号、货币符号
     * @since 0.1.68
     */
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("\\p{S}");

    /**
     * 数字-正则模式
     *
     * 阿拉伯数字、罗马数字等
     * @since 0.1.68
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\p{N}");

    /**
     * 其他字符-正则模式
     * @since 0.1.68
     */
    private static final Pattern OTHER_CHARS_PATTERN = Pattern.compile("\\p{C}");

    /**
     * 邮箱正则表达式
     *
     * https://blog.csdn.net/Architect_CSDN/article/details/89478042
     * https://www.cnblogs.com/lst619247/p/9289719.html
     *
     * 只有英文的邮箱。
     * @since 0.1.68
     */
    private static final Pattern EMAIL_ENGLISH_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    /**
     * 允许中文前缀的邮箱正则表达式
     *
     * https://www.cnblogs.com/lst619247/p/9289719.html
     * @since 0.1.69
     */
    private static final Pattern EMAIL_CHINESE_PATTERN =
            Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    /**
     * 电话号码正则表达式
     * @since 0.1.68
     */
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\\\d{8}$");

    /**
     * URL 正则表达式
     *
     * （1）验证http,https,ftp开头
     * （2）验证一个":"，验证多个"/"
     * （3）验证网址为 xxx.xxx
     * （4）验证有0个或1个问号
     * （5）验证参数必须为xxx=xxx格式，且xxx=空格式通过
     * （6）验证参数与符号&连续个数为0个或1个
     *
     * https://www.cnblogs.com/woaiadu/p/7084250.html
     * @since 0.1.68
     */
    private static final Pattern URL_PATTERN =
            Pattern.compile(
                    "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+(\\\\?{0,1}(([A-Za-z0-9-~]+\\\\={0,1})([A-Za-z0-9-~]*)\\\\&{0,1})*)$");

    /**
     * 网址正则
     * @since 0.1.72
     */
    private static final Pattern WEB_SITE_PATTERN =
            Pattern.compile("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$");

    /**
     * emoji 表情正则表达式
     * https://github.com/zly394/EmojiRegex
     * https://github.com/vdurmont/emoji-java
     * @since 0.1.68
     */
    private static final Pattern EMOJI_PATTERN =
            Pattern.compile(
                    "(?:[\\uD83C\\uDF00-\\uD83D\\uDDFF]|[\\uD83E\\uDD00-\\uD83E\\uDDFF]|[\\uD83D\\uDE00-\\uD83D\\uDE4F]|[\\uD83D\\uDE80-\\uD83D\\uDEFF]|[\\u2600-\\u26FF]\\uFE0F?|[\\u2700-\\u27BF]\\uFE0F?|\\u24C2\\uFE0F?|[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}|[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?|[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3|[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?|[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?|[\\u2934\\u2935]\\uFE0F?|[\\u3030\\u303D]\\uFE0F?|[\\u3297\\u3299]\\uFE0F?|[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?|[\\u203C\\u2049]\\uFE0F?|[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?|[\\u00A9\\u00AE]\\uFE0F?|[\\u2122\\u2139]\\uFE0F?|\\uD83C\\uDC04\\uFE0F?|\\uD83C\\uDCCF\\uFE0F?|[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?)");

    /**
     * IP 对应的正则
     * @since 0.1.125
     */
    private static final Pattern IP_PATTERN = Pattern.compile("^\\d{1,3}(.\\d{1,3}){3}$");

    /**
     * 是否为 ip
     * @param ip ip 地址
     * @return 结果
     * @since 0.1.125
     */
    public static boolean isIp(final String ip) {
        if (StringUtils.isEmptyTrim(ip)) {
            return false;
        }

        return IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 对特殊字符转移
     * @param keyword 特殊字符
     * @return 结果
     */
    public static String escapeWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            for (String key : SPECIAL_CHARS) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    /**
     * 是否为表情符号
     * @param string  字符串
     * @return 是否
     * @since 0.1.68
     */
    public static boolean isEmoji(final String string) {
        return EMOJI_PATTERN.matcher(string).find();
    }

    /**
     * 是否为标点符号
     * 中文符号：参考：https://blog.csdn.net/ztf312/article/details/54310542
     * @param string 字符
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isPunctuation(String string) {
        return isPatternMatch(string, PUNCTUATION_PATTERN);
    }

    /**
     * 是否为可标记的符号
     * @param string 字符
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isMarkable(String string) {
        return isPatternMatch(string, MARKABLE_PATTERN);
    }

    /**
     * 是否为字符
     * @param string 字符
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isSymbol(String string) {
        return isPatternMatch(string, SYMBOL_PATTERN);
    }

    /**
     * 是否为可标记的符号
     * @param string 字符
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isOtherChars(String string) {
        return isPatternMatch(string, OTHER_CHARS_PATTERN);
    }

    /**
     * 是否为数字
     * @param string 字符
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isNumber(String string) {
        return isPatternMatch(string, NUMBER_PATTERN);
    }

    /**
     * 是否为邮件
     * @param string 字符
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isEmail(final String string) {
        return isPatternMatch(string, EMAIL_ENGLISH_PATTERN);
    }

    /**
     * 是否为URL
     * @param string 字符
     * @return 结果
     * @since 0.1.68
     */
    public static boolean isUrl(final String string) {
        return isPatternMatch(string, URL_PATTERN);
    }

    /**
     * 是否为网址
     * @param string 结果
     * @return 是否
     * @since 0.1.73
     */
    public static boolean isWebSite(final String string) {
        return isPatternMatch(string, WEB_SITE_PATTERN);
    }

    /**
     * 验证字符串是否匹配正则表达式
     * @param string 字符串
     * @param pattern 正则表达式
     * @return 是否匹配
     * @since 0.1.68
     */
    private static boolean isPatternMatch(final String string, final Pattern pattern) {
        return pattern.matcher(string).find();
    }

    /**
     * 是否匹配
     * @param pattern 正则
     * @param text 文本
     * @return 结果
     * @since 0.1.161
     */
    public static boolean match(final Pattern pattern, final String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    /**
     * 是否匹配
     * @param regex 正则
     * @param text 文本
     * @return 结果
     * @since 0.1.161
     */
    public static boolean match(final String regex, final String text) {
        Pattern pattern = Pattern.compile(regex);
        return match(pattern, text);
    }

    /**
     * 是否拥有一个满足的正则
     * @param textList 文本
     * @param regex 正则
     * @return 是否
     * @since 0.1.161
     */
    public static boolean hasMatch(List<String> textList, String regex) {
        if (CollectionUtils.isEmpty(textList)) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);

        for (String tagName : textList) {
            if (RegexUtil.match(pattern, tagName)) {
                return true;
            }
        }

        return false;
    }
}
