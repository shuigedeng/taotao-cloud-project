package com.taotao.cloud.rpc.common;

import com.taotao.cloud.rpc.common.URL;

/**
 * 抽象节点
 * <p>
 * 描述服务注册/发现中的"节点"概念（Provider 或 Consumer）。
 * 节点持有 URL，并维护可用性状态。
 */
public interface Node {

    /**
     * 节点 URL
     */
    URL getUrl();

    /**
     * 主机地址
     */
    default String getHost() {
        URL url = getUrl();
        return url == null ? null : url.getHost();
    }

    /**
     * 端口
     */
    default int getPort() {
        URL url = getUrl();
        return url == null ? 0 : url.getPort();
    }

    /**
     * 是否可用
     */
    boolean isAvailable();

    /**
     * 销毁节点
     */
    void destroy();
}
