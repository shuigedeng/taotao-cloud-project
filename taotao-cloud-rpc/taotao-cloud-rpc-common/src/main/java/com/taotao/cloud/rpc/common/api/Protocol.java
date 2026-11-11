package com.taotao.cloud.rpc.common.api;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.spi.SPI;

/**
 * 协议接口
 * <p>
 * 一个协议对应一种远程通信方式（如 ttc-rpc、Triple、HTTP）。
 * <p>
 * 资源文件：META-INF/ttc-rpc/com.taotao.cloud.rpc.api.Protocol
 *
 * @param <T> 服务接口类型
 */
@SPI("ttc-rpc")
public interface Protocol {

    /**
     * 默认端口
     */
    int getDefaultPort();

    /**
     * 服务端导出服务
     *
     * @param invoker 服务实现 Invoker
     * @return 暴露器
     */
    <T> Exporter<T> export(Invoker<T> invoker);

    /**
     * 客户端引用服务
     *
     * @param type      服务接口
     * @param url       远程 URL
     * @param consumerUrl 消费方 URL
     * @return 调用 Invoker
     */
    <T> Invoker<T> refer(Class<T> type, URL url, URL consumerUrl);

    /**
     * 销毁协议（释放所有资源）
     */
    void destroy();
}
