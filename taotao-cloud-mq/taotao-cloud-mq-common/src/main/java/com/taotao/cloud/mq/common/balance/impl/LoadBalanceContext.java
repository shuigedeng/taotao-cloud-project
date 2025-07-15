//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import java.util.List;

public class LoadBalanceContext<T extends IServer> implements ILoadBalanceContext<T> {
    private String hashKey;
    private List<T> servers;

    public static <T extends IServer> LoadBalanceContext<T> newInstance() {
        return new LoadBalanceContext<T>();
    }

    public String hashKey() {
        return this.hashKey;
    }

    public LoadBalanceContext<T> hashKey(String hashKey) {
        this.hashKey = hashKey;
        return this;
    }

    public List<T> servers() {
        return this.servers;
    }

    public LoadBalanceContext<T> servers(List<T> servers) {
        this.servers = servers;
        return this;
    }

    public String toString() {
        return "LoadBalanceContext{hashKey='" + this.hashKey + '\'' + ", servers=" + this.servers + '}';
    }
}
