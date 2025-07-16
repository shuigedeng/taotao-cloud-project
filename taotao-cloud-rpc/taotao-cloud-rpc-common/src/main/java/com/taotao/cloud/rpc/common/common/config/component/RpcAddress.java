/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        return "RpcAddress{"
                + "address='"
                + address
                + '\''
                + ", port="
                + port
                + ", weight="
                + weight
                + '}';
    }
}
