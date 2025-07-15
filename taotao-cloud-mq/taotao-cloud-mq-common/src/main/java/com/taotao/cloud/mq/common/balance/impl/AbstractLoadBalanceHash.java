//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance.impl;


import com.taotao.cloud.mq.common.balance.IServer;
import com.taotao.cloud.mq.common.hash.api.IHashCode;

public abstract class AbstractLoadBalanceHash<T extends IServer> extends AbstractLoadBalance<T> {
    protected final IHashCode hashCode;

    public AbstractLoadBalanceHash(IHashCode hashCode) {
        this.hashCode = hashCode;
    }
}
