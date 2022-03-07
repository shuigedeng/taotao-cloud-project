package com.taotao.cloud.core.sensitive.sensitive.api;

/**
 * 脱敏策略
 */
public interface IStrategy {

    /**
     * 脱敏
     * @param original 原始内容
     * @param context 执行上下文
     * @return 脱敏后的字符串
     */
    Object des(final Object original, final IContext context);

}
