package com.taotao.cloud.rpc.cluster.loadbalance;

import com.taotao.cloud.rpc.cluster.loadbalance.LoadBalance;
import com.taotao.cloud.rpc.cluster.loadbalance.RandomLoadBalance;
import com.taotao.cloud.rpc.cluster.loadbalance.LeastActiveLoadBalance;
import com.taotao.cloud.rpc.cluster.loadbalance.RoundRobinLoadBalance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载均衡工厂
 */
public class LoadBalanceFactory {

    // 负载均衡实例缓存
    private static final Map<String, LoadBalance> LOAD_BALANCES = new ConcurrentHashMap<>();

    static {
        // 注册默认的负载均衡策略
        registerLoadBalance(new RandomLoadBalance());
        registerLoadBalance(new RoundRobinLoadBalance());
        registerLoadBalance(new LeastActiveLoadBalance());
    }

    /**
     * 获取负载均衡实例
     *
     * @param name 负载均衡名称
     * @return 负载均衡实例
     */
    public static LoadBalance getLoadBalance(String name) {
        LoadBalance loadBalance = LOAD_BALANCES.get(name.toLowerCase());
        if (loadBalance == null) {
            // 默认使用随机负载均衡
            loadBalance = LOAD_BALANCES.get(RandomLoadBalance.NAME);
        }
        return loadBalance;
    }

    /**
     * 注册负载均衡
     *
     * @param loadBalance 负载均衡实例
     */
    public static void registerLoadBalance(LoadBalance loadBalance) {
        LOAD_BALANCES.put(loadBalance.getName().toLowerCase(), loadBalance);
    }

    /**
     * 获取默认负载均衡
     */
    public static LoadBalance getDefaultLoadBalance() {
        return getLoadBalance(RandomLoadBalance.NAME);
    }
}
