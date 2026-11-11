package com.taotao.cloud.rpc.common.api;

import com.taotao.cloud.rpc.common.invoke.Invoker;

/**
 * 服务暴露器
 * <p>
 * 由 {@link Protocol#export(Invoker)} 返回，代表一个已导出的服务。
 *
 * @param <T> 服务接口类型
 */
public interface Exporter<T> {

    /**
     * 获取被暴露的 Invoker
     */
    Invoker<T> getInvoker();

    /**
     * 取消暴露
     */
    void unexport();
}
