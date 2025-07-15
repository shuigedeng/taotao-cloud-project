
package com.taotao.cloud.mq.common.balance;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.balance.impl.LoadBalanceContext;
import com.taotao.cloud.mq.common.balance.impl.LoadBalances;
import java.util.ArrayList;
import java.util.List;

public final class LoadBalanceBs<T extends IServer> {
    private ILoadBalance<T> loadBalance = LoadBalances.random();
    private String hashKey = "";
    private List<T> servers = new ArrayList();

    private LoadBalanceBs() {
    }

    public static <T extends IServer> LoadBalanceBs<T> newInstance() {
        return new LoadBalanceBs<T>();
    }

    public LoadBalanceBs<T> loadBalance(ILoadBalance<T> loadBalance) {
        ArgUtils.notNull(loadBalance, "loadBalance");
        this.loadBalance = loadBalance;
        return this;
    }

    public LoadBalanceBs<T> hashKey(String hashKey) {
        this.hashKey = hashKey;
        return this;
    }

    public LoadBalanceBs<T> servers(List<T> servers) {
        ArgUtils.notEmpty(servers, "servers");
        this.servers = servers;
        return this;
    }

    public T select() {
        ArgUtils.notEmpty(this.servers, "servers");
        ArgUtils.notNull(this.loadBalance, "loadBalance");
        ILoadBalanceContext<T> context = LoadBalanceContext.<T>newInstance().hashKey(this.hashKey).servers(this.servers);
        return (T)this.loadBalance.select(context);
    }
}
