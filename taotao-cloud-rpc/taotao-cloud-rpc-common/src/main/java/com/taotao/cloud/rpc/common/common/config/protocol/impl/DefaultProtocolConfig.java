package com.taotao.cloud.rpc.common.common.config.protocol.impl;

import com.taotao.cloud.rpc.common.common.config.protocol.ProtocolConfig;

/**
 * 协议配置信息
 * @author shuigedeng
 * @since 2024.06
 */
public class DefaultProtocolConfig implements ProtocolConfig {

    /**
     * 名称
     * RPC
     * HTTP
     * HTTPS
     * @since 2024.06
     */
    private String name;

    /**
     * 协议端口号
     */
    private int port;

    @Override
    public String name() {
        return name;
    }

    public DefaultProtocolConfig name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int port() {
        return port;
    }

    public DefaultProtocolConfig port(int port) {
        this.port = port;
        return this;
    }
}
