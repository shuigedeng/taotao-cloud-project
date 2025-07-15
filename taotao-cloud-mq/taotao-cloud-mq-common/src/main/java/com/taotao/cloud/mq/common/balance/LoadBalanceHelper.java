//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance;

import com.taotao.cloud.mq.common.balance.impl.LoadBalances;
import com.taotao.cloud.mq.common.hash.api.IHashCode;
import java.util.List;

public final class LoadBalanceHelper {
    private LoadBalanceHelper() {
    }

    public static <T extends IServer> T random(List<T> servers) {
        return (T)LoadBalanceBs.<T>newInstance().servers(servers).loadBalance(LoadBalances.random()).select();
    }

    public static <T extends IServer> T roundRobbin(List<T> servers) {
        return (T)LoadBalanceBs.<T>newInstance().servers(servers).loadBalance(LoadBalances.roundRobbin()).select();
    }

    public static <T extends IServer> T weightRoundRobbin(List<T> servers) {
        return (T)LoadBalanceBs.<T>newInstance().servers(servers).loadBalance(LoadBalances.weightRoundRobbin()).select();
    }

    public static <T extends IServer> T commonHash(List<T> servers, IHashCode hash, String hashKey) {
        return (T)LoadBalanceBs.<T>newInstance().servers(servers).hashKey(hashKey).loadBalance(LoadBalances.commonHash(hash)).select();
    }

    public static <T extends IServer> T consistentHash(List<T> servers, IHashCode hash, String hashKey) {
        return (T)LoadBalanceBs.<T>newInstance().servers(servers).hashKey(hashKey).loadBalance(LoadBalances.consistentHash(hash)).select();
    }
}
