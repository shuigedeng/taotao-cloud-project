package com.taotao.cloud.pinyin.api;

import java.util.List;

/**
 * 拼音核心用户 api
 */
public interface IPinyin {

    /**
     * 获取指定字符串的拼音
     * @param string 原始字符串
     * @param context 上下文
     * @return 转换后的结果
     */
    String toPinyin(final String string, final IPinyinContext context);

    /**
     * 返回所有拼音列表
     * （1）返回当前汉字的所有拼音列表
     * （2）如果不是汉字，返回其本身
     *
     * @param chinese 中文汉字
     * @param context 上下文
     * @return 返回所有拼音列表
     */
    List<String> toPinyinList(final char chinese, final IPinyinContext context);

    /**
     * 是否为同音字
     * @param chineseOne 中文一
     * @param chineseTwo 中文二
     * @param context 上下文
     * @return 是否相同
     */
    boolean hasSamePinyin(final char chineseOne, final char chineseTwo, final IPinyinContext context);

    /**
     * 获取拼音的音调编号
     * 12345
     * @param chinese 中文拼音
     * @param context 上下文
     * @return 结果
     */
    @Deprecated
    List<Integer> toneNumList(final String chinese, final IPinyinContext context);

    /**
     * 获取拼音的音调编号
     * 12345
     * @param chinese 中文字符
     * @param context 上下文
     * @return 结果
     */
    @Deprecated
    List<Integer> toneNumList(final char chinese, final IPinyinContext context);

    /**
     * 获取声母字符
     * @param chinese 中文汉字
     * @param context 上下文
     * @return 结果
     */
    @Deprecated
    List<String> shengMuList(final String chinese, final IPinyinContext context);

    /**
     * 获取韵母字符
     * @param chinese 中文字符
     * @param context 上下文
     * @return 结果
     */
    @Deprecated
    List<String> yunMuList(final String chinese, final IPinyinContext context);

    /**
     * 相同的拼音列表
     * @param pinyin 拼音
     * @param sameToneNum 相同的声调
     * @param context 上下文
     * @return 结果
     */
    List<String> samePinyinList(String pinyin, final boolean sameToneNum, final IPinyinContext context);


}
