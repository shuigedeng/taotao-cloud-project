package com.taotao.cloud.sensitive.sensitive.sensitive.api;

/**
 * 执行上下文接口
 */
public interface ICondition {

    /**
     * 是否执行脱敏
     * @param context 执行上下文
     * @return 结果：是否执行
     */
    boolean valid(IContext context);

}
