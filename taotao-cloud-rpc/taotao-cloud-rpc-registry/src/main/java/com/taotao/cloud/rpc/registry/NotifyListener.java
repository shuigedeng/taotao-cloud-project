package com.taotao.cloud.rpc.registry;

import com.taotao.cloud.rpc.common.URL;

import java.util.List;

/**
 * 服务变更监听器
 */
public interface NotifyListener {

    /**
     * 服务变更通知
     *
     * @param urls 变更后的服务 URL 列表
     */
    void notify(List<URL> urls);

    /**
     * 服务添加通知
     *
     * @param url 新增的服务 URL
     */
    default void onServiceAdded(URL url) {
    }

    /**
     * 服务移除通知
     *
     * @param url 移除的服务 URL
     */
    default void onServiceRemoved(URL url) {
    }
}
