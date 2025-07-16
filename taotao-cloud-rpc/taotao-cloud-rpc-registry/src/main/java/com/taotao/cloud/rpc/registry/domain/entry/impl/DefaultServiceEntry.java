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

package com.taotao.cloud.rpc.registry.domain.entry.impl;

import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;

/**
 * <p> 默认服务明细 </p>
 * @since 2024.06
 */
class DefaultServiceEntry implements ServiceEntry {

    private static final long serialVersionUID = 189302740666003309L;

    /**
     * 服务标识
     * @since 2024.06
     */
    private String serviceId;

    /**
     * 服务描述
     * @since 2024.06
     */
    private String description;

    /**
     * 机器 ip 信息
     *
     * <pre>
     *     InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
     *     String clientIP = insocket.getAddress().getHostAddress();
     * </pre>
     *
     * @since 2024.06
     */
    private String ip;

    /**
     * 端口信息
     * @since 2024.06
     */
    private int port;

    /**
     * 权重信息
     * @since 2024.06
     */
    private int weight;

    @Override
    public String serviceId() {
        return serviceId;
    }

    public DefaultServiceEntry serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public String description() {
        return description;
    }

    public DefaultServiceEntry description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String ip() {
        return ip;
    }

    public DefaultServiceEntry ip(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public int port() {
        return port;
    }

    public DefaultServiceEntry port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public int weight() {
        return weight;
    }

    public DefaultServiceEntry weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultServiceEntry{"
                + "serviceId='"
                + serviceId
                + '\''
                + ", description='"
                + description
                + '\''
                + ", ip='"
                + ip
                + '\''
                + ", port="
                + port
                + ", weight="
                + weight
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DefaultServiceEntry that = (DefaultServiceEntry) o;

        if (port != that.port) {
            return false;
        }
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) {
            return false;
        }
        return ip != null ? ip.equals(that.ip) : that.ip == null;
    }

    @Override
    public int hashCode() {
        int result = serviceId != null ? serviceId.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }
}
