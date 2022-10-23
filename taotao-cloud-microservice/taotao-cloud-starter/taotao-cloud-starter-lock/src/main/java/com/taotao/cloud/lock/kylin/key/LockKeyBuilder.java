package com.taotao.cloud.lock.kylin.key;

import java.lang.reflect.Method;

/**
 * 分布式锁Key生成器 接口
 *
 * @author wangjinkui
 */
public interface LockKeyBuilder {

    /**
     * @param method         加锁方法
     * @param args           加锁方法参数
     * @param definitionKeys keys
     * @return 解析keys后的拼接起来的字符串
     */
    String buildKey(Method method, Object[] args, String[] definitionKeys);

    /**
     * 构建 联锁、红锁 key后缀
     *
     * @param method    加锁方法
     * @param args      加锁方法参数
     * @param keySuffix 联锁、红锁 key后缀
     */
    void buildKeySuffix(Method method, Object[] args, String[] keySuffix);
}
