
package com.taotao.cloud.mq.common.balance;

import java.util.Objects;

public class Server implements IServer {
    private String url;
    private int weight;

    public static Server newInstance() {
        return new Server();
    }

    public static Server of(String url, int weight) {
        return newInstance().url(url).weight(weight);
    }

    public static Server of(String url) {
        return of(url, 1);
    }

    public String url() {
        return this.url;
    }

    public Server url(String url) {
        this.url = url;
        return this;
    }

    public int weight() {
        return this.weight;
    }

    public Server weight(int weight) {
        this.weight = weight;
        return this;
    }

    public String toString() {
        return "Server{url='" + this.url + '\'' + ", weight=" + this.weight + '}';
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            Server server = (Server)object;
            return this.weight == server.weight && Objects.equals(this.url, server.url);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.url, this.weight});
    }
}
