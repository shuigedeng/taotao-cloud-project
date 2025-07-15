//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.balance;


public interface ILoadBalance<T extends IServer> {
    T select(ILoadBalanceContext<T> var1);
}
