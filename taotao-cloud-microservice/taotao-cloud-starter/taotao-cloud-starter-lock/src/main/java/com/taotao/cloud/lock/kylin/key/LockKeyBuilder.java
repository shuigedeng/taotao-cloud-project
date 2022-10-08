package com.taotao.cloud.lock.kylin.key;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 分布式锁Key生成器 接口
 *
 * @author wangjinkui
 */
public interface LockKeyBuilder {

    /**
     * 构建key
     *
     * @param invocation     拦截器链
     * @param definitionKeys 定义
     * @return 解析keys后的拼接起来的字符串
     */
    String buildKey(MethodInvocation invocation, String[] definitionKeys);

    /**
     * 构建 联锁、红锁 key后缀
     *
     * @param invocation 拦截器链
     * @param keySuffix  联锁、红锁 key后缀
     */
    void buildKeySuffix(MethodInvocation invocation, String[] keySuffix);
}
