package com.taotao.cloud.rpc.core.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invoker;

import java.util.List;

/**
 * 目录接口
 * 维护可调用服务列表
 */
public interface Directory<T> {

    /**
     * 获取服务接口类
     */
    Class<T> getInterface();

    /**
     * 获取所有可用的 Invoker 列表
     *
     * @return Invoker 列表
     */
    List<Invoker<T>> list();

    /**
     * 获取目录的 URL
     */
    URL getUrl();

    /**
     * 判断是否销毁
     */
    boolean isDestroyed();

    /**
     * 销毁
     */
    void destroy();
}
