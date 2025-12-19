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

package com.taotao.cloud.mq.common.balance;

import java.util.Objects;

/**
 * Server
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class Server implements IServer {

    private String url;
    private int weight;

    public static Server newInstance() {
        return new Server();
    }

    public static Server of( String url, int weight ) {
        return newInstance().url(url).weight(weight);
    }

    public static Server of( String url ) {
        return of(url, 1);
    }

    public String url() {
        return this.url;
    }

    public Server url( String url ) {
        this.url = url;
        return this;
    }

    public int weight() {
        return this.weight;
    }

    public Server weight( int weight ) {
        this.weight = weight;
        return this;
    }

    public String toString() {
        return "Server{url='" + this.url + '\'' + ", weight=" + this.weight + '}';
    }

    public boolean equals( Object object ) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            Server server = (Server) object;
            return this.weight == server.weight && Objects.equals(this.url, server.url);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.url, this.weight});
    }
}
