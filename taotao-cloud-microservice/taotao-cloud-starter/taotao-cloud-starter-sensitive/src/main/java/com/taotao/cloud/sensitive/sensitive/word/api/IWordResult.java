package com.taotao.cloud.sensitive.sensitive.word.api;

/**
 * 敏感词的结果
 */
public interface IWordResult {

    /**
     * 敏感词
     * @return 敏感词
     */
    String word();

    /**
     * 开始下标
     * @return 开始下标
     */
    int startIndex();

    /**
     * 结束下标
     * @return 结束下标
     */
    int endIndex();

}
