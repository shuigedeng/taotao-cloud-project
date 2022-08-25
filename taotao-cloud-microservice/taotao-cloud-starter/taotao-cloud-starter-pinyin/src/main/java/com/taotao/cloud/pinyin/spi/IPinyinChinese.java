package com.taotao.cloud.pinyin.spi;

/**
 * 拼音中文接口
 */
public interface IPinyinChinese {

    /**
     * 是否为中文
     *
     * TODO: 这里有一个疑问，是直接 tone 映射比较快，还是每次判断之后再映射快？
     *
     * 如果都是中文的话，显然是直接映射比较高效。
     *
     * 这个还是需要的，如果不添加，假设分词以下信息：
     * 中1a3国
     * 因为拼音需要空格连接，就会变成：
     * 【zhong 1 a 3 guo】
     * 可能预期是：
     * 【zhong 1a3 guo】
     *
     * 暂时不管这个格式问题。
     * @param original 原始字符串
     * @return 是否
     */
    boolean isChinese(final String original);

    /**
     * 是否为中文
     *
     * @param original 原始字符串
     * @return 是否
     */
    boolean isChinese(final char original);

    /**
     * 转换为简体
     * （1）为了兼顾性能，避免两次分词。直接使用分词后的结果进行处理。
     * @param segment 分词后的字符串
     * @return 简体中文
     */
    String toSimple(final String segment);

    /**
     * 转换为简体
     * @param original 原始的字符串
     * @return 简体中文
     */
    String toSimple(final char original);

}
