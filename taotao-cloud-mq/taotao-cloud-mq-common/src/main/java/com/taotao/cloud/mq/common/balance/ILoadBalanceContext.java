
package com.taotao.cloud.mq.common.balance;

import java.util.List;

public interface ILoadBalanceContext<T extends IServer> {
    String hashKey();

    List<T> servers();
}
