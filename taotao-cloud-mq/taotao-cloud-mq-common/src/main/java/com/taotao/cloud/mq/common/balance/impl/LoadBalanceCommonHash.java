//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import com.taotao.cloud.mq.common.hash.api.IHashCode;
import java.util.List;

public class LoadBalanceCommonHash<T extends IServer> extends AbstractLoadBalanceHash<T> {
    public LoadBalanceCommonHash(IHashCode hashCode) {
        super(hashCode);
    }

    protected T doSelect(ILoadBalanceContext<T> context) {
        List<T> servers = context.servers();
        String hashKey = context.hashKey();
        int code = this.hashCode.hash(hashKey);
        int hashCode = Math.abs(code);
        int index = hashCode % servers.size();
        return (T)(servers.get(index));
    }
}
