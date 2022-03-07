package com.taotao.cloud.core.sensitive.word.api;

/**
 * 敏感词替换策略
 *
 */
public interface ISensitiveWordReplace {

    /**
     * 替换
     * @param context 上下文
     * @return 结果
     * @since 0.2.0
     */
    String replace(ISensitiveWordReplaceContext context);

}
