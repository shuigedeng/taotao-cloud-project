//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.boot.common.support.filter.IFilter;
import com.taotao.boot.common.support.handler.IHandler;
import com.taotao.boot.common.utils.collection.CollectionUtils;
import com.taotao.boot.common.utils.number.MathUtils;
import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class LoadBalanceWeightRoundRobbin<T extends IServer> extends AbstractLoadBalance<T> {
    private final AtomicLong indexHolder = new AtomicLong();

    protected T doSelect(ILoadBalanceContext<T> context) {
        List<T> servers = context.servers();
        List<T> actualList = this.buildActualList(servers);
        long index = this.indexHolder.getAndIncrement();
        int actual = (int)(index % (long)actualList.size());
        return (T)(actualList.get(actual));
    }

    private List<T> buildActualList(List<T> serverList) {
        List<T> actualList = new ArrayList();
        List<T> notZeroServers = CollectionUtils.filterList(serverList, new IFilter<T>() {
            public boolean filter(IServer iServer) {
                return iServer.weight() <= 0;
            }
        });
        List<Integer> weightList = CollectionUtils.toList(notZeroServers, new IHandler<T, Integer>() {
            public Integer handle(T iServer) {
                return iServer.weight();
            }
        });
        int maxDivisor = MathUtils.ngcd(weightList);

        for(T server : notZeroServers) {
            int weight = server.weight();
            int times = weight / maxDivisor;

            for(int i = 0; i < times; ++i) {
                actualList.add(server);
            }
        }

        return actualList;
    }
}
