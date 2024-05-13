package com.taotao.cloud.rpc.registry.register.domain.message.body;

import java.io.Serializable;

/**
 * 注册中心新增通知对象
 * @since 0.1.8
 */
public class RegisterCenterAddNotifyBody implements Serializable {

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

    public RegisterCenterAddNotifyBody ip(String ip) {
        this.ip = ip;
        return this;
    }

    public int port() {
        return port;
    }

    public RegisterCenterAddNotifyBody port(int port) {
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
