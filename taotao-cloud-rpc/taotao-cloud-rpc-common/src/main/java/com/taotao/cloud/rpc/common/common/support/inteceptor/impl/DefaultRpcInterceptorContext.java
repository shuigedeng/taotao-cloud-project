package com.taotao.cloud.rpc.common.common.support.inteceptor.impl;

import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptorContext;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc 拦截器上下文
 *
 * @author shuigedeng
 * @since 0.1.4
 */
public class DefaultRpcInterceptorContext implements RpcInterceptorContext {

    /**
     * 唯一标识
     * @since 0.1.4
     */
    private String traceId;

    /**
     * 开始时间
     * @since 0.1.4
     */
    private long startTime;

    /**
     * 结束时间
     * @since 0.1.4
     */
    private long endTime;

    /**
     * map 集合
     * @since 0.1.4
     */
    private transient final Map<String, Object> map;

    /**
     * 获取请求参数
     * @since 0.2.2
     */
    private Object[] params;

    /**
     * 请求结果
     * @since 0.2.2
     */
    private Object result;

    private DefaultRpcInterceptorContext() {
        map = new HashMap<>();
    }

    /**
     * 创建一个新的对象实例
     * @return this
     * @since 0.1.4
     */
    public static DefaultRpcInterceptorContext newInstance() {
        return new DefaultRpcInterceptorContext();
    }

    @Override
    public String traceId() {
        return traceId;
    }

    public DefaultRpcInterceptorContext traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    public DefaultRpcInterceptorContext startTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public long endTime() {
        return endTime;
    }

    public DefaultRpcInterceptorContext endTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public RpcInterceptorContext put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        return this.map.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> tClass) {
        Object object = this.get(key);
//        if(ObjectUtil.isNotNull(object)) {
//            return (T)object;
//        }
        return null;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public DefaultRpcInterceptorContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public DefaultRpcInterceptorContext result(Object result) {
        this.result = result;
        return this;
    }
}
