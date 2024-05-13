package com.taotao.cloud.rpc.registry.register.domain.message.body;

import java.io.Serializable;

/**
 * 注册中心移除通知对象
 * @since 0.1.8
 */
public class RegisterCenterRemoveNotifyBody implements Serializable {

    /**
     * 机器 ip 信息
     * @since 0.1.8
     */
    private String ip;

    /**
     * 端口信息
     * @since 0.1.8
     */
    private int port;

    public String ip() {
        return ip;
    }

    public RegisterCenterRemoveNotifyBody ip(String ip) {
        this.ip = ip;
        return this;
    }

    public int port() {
        return port;
    }

    public RegisterCenterRemoveNotifyBody port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return "RegisterCenterAddNotifyBody{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

}
