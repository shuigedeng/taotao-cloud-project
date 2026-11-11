package com.taotao.cloud.rpc.registry;

import com.taotao.cloud.rpc.registry.NotifyListener;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.spi.SPI;

import java.util.List;

/**
 * 注册中心服务接口
 * 提供服务注册、发现、订阅功能
 */
@SPI("in-memory")
public interface RegistryService {

    /**
     * 注册服务
     *
     * @param url 服务 URL
     */
    void register(URL url);

    /**
     * 注销服务
     *
     * @param url 服务 URL
     */
    void unregister(URL url);

    /**
     * 订阅服务
     *
     * @param url      服务 URL（条件）
     * @param listener 监听器
     */
    void subscribe(URL url, NotifyListener listener);

    /**
     * 取消订阅
     *
     * @param url      服务 URL
     * @param listener 监听器
     */
    void unsubscribe(URL url, NotifyListener listener);

    /**
     * 查询服务列表
     *
     * @param url 服务 URL（条件）
     * @return 服务 URL 列表
     */
    List<URL> lookup(URL url);

    /**
     * 关闭注册中心
     */
    void destroy();
}
