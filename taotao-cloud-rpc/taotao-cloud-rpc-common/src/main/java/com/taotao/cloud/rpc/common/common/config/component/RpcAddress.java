package com.taotao.cloud.rpc.common.common.config.component;

import java.util.Objects;

/**
 * 地址信息
 * @author shuigedeng
 * @since 2024.06
 */
public class RpcAddress {

    /**
     * address 信息
     * @since 2024.06
     */
    private String address;

    /**
     * 端口号
     * @since 2024.06
     */
    private int port;

    /**
     * 权重
     * @since 2024.06
     */
    private int weight;

    public RpcAddress(String address, int port) {
        this.address = address;
        this.port = port;
        this.weight = 0;
    }

    public RpcAddress(String address, int port, int weight) {
        this.address = address;
        this.port = port;
        this.weight = weight;
    }

    public String address() {
        return address;
    }

    public RpcAddress address(String ip) {
        this.address = ip;
        return this;
    }

    public int port() {
        return port;
    }

    public RpcAddress port(int port) {
        this.port = port;
        return this;
    }

    public int weight() {
        return weight;
    }

    public RpcAddress weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcAddress that = (RpcAddress) o;
        return port == that.port && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }

    @Override
    public String toString() {
        return "RpcAddress{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}
