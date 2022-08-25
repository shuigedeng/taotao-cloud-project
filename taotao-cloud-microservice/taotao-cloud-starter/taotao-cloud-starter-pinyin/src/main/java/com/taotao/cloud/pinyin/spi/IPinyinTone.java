package com.taotao.cloud.pinyin.spi;


import com.taotao.cloud.pinyin.api.IPinyinContext;
import java.util.List;
import java.util.Set;

/**
 * 拼音连接接口
 *
 * （1）可以使用模式来制定对应的映射实现。
 */
public interface IPinyinTone {

    /**
     * 获取映射后的信息
     * @param segment 分词后的片段
     * @param context 上下文
     * @return 结果
     */
    String tone(final String segment, final IPinyinContext context);

    /**
     * 返回所有的列表
     * @param chinese 单个中文
     * @param context 上下文
     * @return 结果
     */
    List<String> toneList(final String chinese, final IPinyinContext context);

    /**
     * 词组列表
     *
     * 作用：快速分词。
     * @return 列表
     */
    Set<String> phraseSet();

    /**
     * 根据中文获取对应的拼音声调
     *
     * 1. 这里暂时没有考虑多音字的问题。
     * 2. 如果考虑内容不是单个字，那么结果应该变为一个列表。
     * @param defaultPinyin 中文拼音-默认格式
     * @return 声调
     */
    int toneNum(final String defaultPinyin);

}
