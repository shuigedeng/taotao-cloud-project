package com.taotao.cloud.pinyin.roses.api;

/**
 * 拼音转化接口
 *
 */
public interface PinYinApi {

    /**
     * 获取姓氏的首字母大写
     * <p>
     * 百家姓涉及到多音字的，都配置在properties中，优先读取properties中的映射
     * <p>
     * 例如：张 -> Z
     * 例如：单 -> S
     *
     * @param lastnameChines 中文姓氏
     * @return 姓氏的首字母大写
     */
    String getLastnameFirstLetterUpper(String lastnameChines);

    /**
     * 将文字转为汉语拼音，取每个字的第一个字母大写
     * <p>
     * 例如：你好 => NH
     *
     * @param chineseString 中文字符串
     * @return 中文字符串每个字的第一个字母大写
     */
    String getChineseStringFirstLetterUpper(String chineseString);

    /**
     * 获取汉字字符串的全拼拼音
     * <p>
     * 例如：中国人 -> zhongguoren
     *
     * @param chineseString 中文字符串
     * @return 拼音形式的字符串
     */
    String parsePinyinString(String chineseString);

    /**
     * 将中文字符串转化为汉语拼音，取每个字的首字母
     * <p>
     * 例如：中国人 -> zgr
     *
     * @param chinesString 中文字符串
     * @return 每个字的拼音首字母组合
     */
    String parseEveryPinyinFirstLetter(String chinesString);

    /**
     * 将中文字符串转移为ASCII码
     *
     * @param chineseString 中文字符串
     * @return ASCII码
     */
    String getChineseAscii(String chineseString);

}
