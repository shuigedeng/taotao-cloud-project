//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;


import com.taotao.cloud.mq.common.balance.ILoadBalance;
import com.taotao.cloud.mq.common.balance.IServer;
import com.taotao.cloud.mq.common.hash.api.IHashCode;

public final class LoadBalances {
    private LoadBalances() {
    }

    public static <T extends IServer> ILoadBalance<T> random() {
        return new LoadBalanceRandom();
    }

    public static <T extends IServer> ILoadBalance<T> roundRobbin() {
        return new LoadBalanceRoundRobbin();
    }

    public static <T extends IServer> ILoadBalance<T> weightRoundRobbin() {
        return new LoadBalanceWeightRoundRobbin();
    }

    public static <T extends IServer> ILoadBalance<T> commonHash(IHashCode hashCode) {
        return new LoadBalanceCommonHash(hashCode);
    }

    public static <T extends IServer> ILoadBalance<T> consistentHash(IHashCode hashCode) {
        return new LoadBalanceConsistentHash(hashCode);
    }
}
