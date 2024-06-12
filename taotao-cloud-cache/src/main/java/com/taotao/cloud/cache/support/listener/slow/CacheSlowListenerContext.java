package com.taotao.cloud.cache.support.listener.slow;

import com.taotao.cloud.cache.api.ICacheSlowListenerContext;

/**
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheSlowListenerContext implements ICacheSlowListenerContext {

    /**
     * 方法名称
     * @since 2024.06
     */
    private String methodName;

    /**
     * 参数信息
     * @since 2024.06
     */
    private Object[] params;

    /**
     * 方法结果
     * @since 2024.06
     */
    private Object result;

    /**
     * 开始时间
     * @since 2024.06
     */
    private long startTimeMills;

    /**
     * 结束时间
     * @since 2024.06
     */
    private long endTimeMills;

    /**
     * 消耗时间
     * @since 2024.06
     */
    private long costTimeMills;

    /**
     * @since 2024.06
     * @return 实例
     */
    public static CacheSlowListenerContext newInstance() {
        return new CacheSlowListenerContext();
    }

    @Override
    public String methodName() {
        return methodName;
    }

    public CacheSlowListenerContext methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheSlowListenerContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public CacheSlowListenerContext result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startTimeMills() {
        return startTimeMills;
    }

    public CacheSlowListenerContext startTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
        return this;
    }

    @Override
    public long endTimeMills() {
        return endTimeMills;
    }

    public CacheSlowListenerContext endTimeMills(long endTimeMills) {
        this.endTimeMills = endTimeMills;
        return this;
    }

    @Override
    public long costTimeMills() {
        return costTimeMills;
    }

    public CacheSlowListenerContext costTimeMills(long costTimeMills) {
        this.costTimeMills = costTimeMills;
        return this;
    }
}
