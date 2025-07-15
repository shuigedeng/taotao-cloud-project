//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;


import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import com.taotao.cloud.mq.common.hash.ConsistentHashingBs;
import com.taotao.cloud.mq.common.hash.IConsistentHashing;
import com.taotao.cloud.mq.common.hash.api.IHashCode;

public class LoadBalanceConsistentHash<T extends IServer> extends AbstractLoadBalanceHash<T> {
    public LoadBalanceConsistentHash(IHashCode hashCode) {
        super(hashCode);
    }

    protected T doSelect(ILoadBalanceContext<T> context) {
        IConsistentHashing<T> consistentHashing = ConsistentHashingBs.<T>newInstance().hashCode(this.hashCode).nodes(context.servers()).build();
        String hashKey = context.hashKey();
        return (T)(consistentHashing.get(hashKey));
    }
}
