package com.taotao.cloud.sensitive.sensitive.word.api;

/**
 * 单词格式化
 * （1）忽略大小写
 * （2）忽略全角半角
 * （3）忽略停顿词
 * （4）忽略数字转换。
 *
 */
public interface ICharFormat {

    /**
     * 针对 char 格式化
     * @param original 原始 char
     * @param context 上下文
     * @return 格式化后的 char
     */
    char format(final char original,
                final IWordContext context);

}
