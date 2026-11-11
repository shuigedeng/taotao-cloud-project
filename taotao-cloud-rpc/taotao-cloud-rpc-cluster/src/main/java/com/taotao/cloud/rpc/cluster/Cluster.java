package com.taotao.cloud.rpc.cluster;

import com.taotao.cloud.rpc.cluster.Directory;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.spi.SPI;

/**
 * 集群接口
 * 负责将多个 Invoker 组合成一个可高可用的 Invoker
 */
@SPI("failover")
public interface Cluster {

    /**
     * 集群名称
     */
    String getName();

    /**
     * 将目录中的多个 Invoker 合并成一个 Invoker
     *
     * @param directory Invoker 目录
     * @return 合并后的 Invoker
     */
    <T> Invoker<T> join(Directory<T> directory);

    /**
     * 合并 URL 中的集群配置
     *
     * @param url 原始 URL
     * @return 合并后的 URL
     */
    default URL mergeUrl(URL url) {
        return url;
    }
}
