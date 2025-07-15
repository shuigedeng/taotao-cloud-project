
package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.cloud.mq.common.balance.ILoadBalance;
import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import java.util.List;

public abstract class AbstractLoadBalance<T extends IServer> implements ILoadBalance<T> {
    public T select(ILoadBalanceContext<T> context) {
        List<T> servers = context.servers();
        return (T)(servers.size() <= 1 ? (IServer)servers.get(0) : this.doSelect(context));
    }

    protected abstract T doSelect(ILoadBalanceContext<T> var1);
}
