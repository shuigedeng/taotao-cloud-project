package com.taotao.cloud.rpc.registry.register.domain.message.body;

import java.io.Serializable;

/**
 * 服务端心跳对象
 * @since 0.2.0
 */
public class ServerHeartbeatBody implements Serializable {

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

    /**
     * 请求的时间
     * @since 0.2.0
     */
    private long time;

    public String ip() {
        return ip;
    }

    public ServerHeartbeatBody ip(String ip) {
        this.ip = ip;
        return this;
    }

    public int port() {
        return port;
    }

    public ServerHeartbeatBody port(int port) {
        this.port = port;
        return this;
    }

    public long time() {
        return time;
    }

    public ServerHeartbeatBody time(long time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "ServerHeartbeatBody{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", time=" + time +
                '}';
    }

}
