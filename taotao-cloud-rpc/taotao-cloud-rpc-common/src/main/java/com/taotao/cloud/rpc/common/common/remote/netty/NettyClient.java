package com.github.houbb.rpc.common.remote.netty;

import com.github.houbb.rpc.common.api.Destroyable;
import com.github.houbb.rpc.common.api.Initializable;

import java.util.concurrent.Callable;

/**
 * netty 网络客户端
 * @author shuigedeng
 * @since 0.0.8
 * @param <V> 泛型
 */
public interface NettyClient<V> extends Callable<V>, Destroyable, Initializable {
}
