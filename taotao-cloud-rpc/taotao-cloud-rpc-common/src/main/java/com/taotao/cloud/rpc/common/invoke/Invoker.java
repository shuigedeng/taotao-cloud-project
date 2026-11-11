package com.taotao.cloud.rpc.common.invoke;

import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;

/**
 * 调用者接口
 * 参考 Dubbo 的 Invoker 设计
 * 代表一个可调用实体的抽象
 *
 * @param <T> 服务接口类型
 */
public interface Invoker<T> {

    /**
     * 获取服务接口类
     */
    Class<T> getInterface();

    /**
     * 执行调用
     *
     * @param invocation 调用信息
     * @return 调用结果
     * @throws Throwable 调用异常
     */
    Result invoke(Invocation invocation) throws Throwable;

    /**
     * 获取 URL
     */
    URL getUrl();

    /**
     * 判断是否可用
     */
    boolean isAvailable();

    /**
     * 销毁
     */
    void destroy();
}
