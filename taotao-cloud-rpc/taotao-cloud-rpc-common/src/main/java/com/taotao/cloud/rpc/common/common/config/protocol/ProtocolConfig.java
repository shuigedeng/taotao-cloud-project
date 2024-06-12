package com.taotao.cloud.rpc.common.common.config.protocol;

/**
 * 协议配置信息
 * @author shuigedeng
 * @since 2024.06
 */
public interface ProtocolConfig {

    /**
     * 名称
     * RPC
     * HTTP
     * HTTPS
     * @since 2024.06
     * @return 协议名称
     */
    String name();

    /**
     * 协议端口号
     * @return 端口号
     */
    int port();

}
