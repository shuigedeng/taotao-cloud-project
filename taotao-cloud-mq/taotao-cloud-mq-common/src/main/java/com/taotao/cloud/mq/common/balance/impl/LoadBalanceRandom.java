//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LoadBalanceRandom<T extends IServer> extends AbstractLoadBalance<T> {
    protected T doSelect(ILoadBalanceContext<T> context) {
        List<T> servers = context.servers();
        Random random = ThreadLocalRandom.current();
        int nextIndex = random.nextInt(servers.size());
        return (T)(servers.get(nextIndex));
    }
}
