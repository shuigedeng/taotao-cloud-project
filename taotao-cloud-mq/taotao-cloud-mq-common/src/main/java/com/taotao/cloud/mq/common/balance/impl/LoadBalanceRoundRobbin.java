//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class LoadBalanceRoundRobbin<T extends IServer> extends AbstractLoadBalance<T> {
    private final AtomicLong indexHolder = new AtomicLong();

    protected T doSelect(ILoadBalanceContext<T> context) {
        List<T> servers = context.servers();
        long index = this.indexHolder.getAndIncrement();
        int actual = (int)(index % (long)servers.size());
        return (T)(servers.get(actual));
    }
}
