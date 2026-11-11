package com.taotao.cloud.rpc.core.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;

import java.util.List;

/**
 * 路由接口
 * 用于过滤 invoker 列表
 */
public interface Router {

    /**
     * 获取路由 URL
     */
    URL getUrl();

    /**
     * 路由过滤
     *
     * @param invokers   原始 invoker 列表
     * @param url        引用 URL
     * @param invocation 调用信息
     * @return 过滤后的 invoker 列表
     */
    <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation);

    /**
     * 获取路由优先级，数字越小优先级越高
     */
    default int getPriority() {
        return 0;
    }
}
