package com.taotao.cloud.rpc.common.common.remote.netty;

import com.taotao.cloud.rpc.common.common.api.Destroyable;
import com.taotao.cloud.rpc.common.common.api.Initializable;

import java.util.concurrent.Callable;

/**
 * netty 网络客户端
 * @author shuigedeng
 * @since 0.0.8
 * @param <V> 泛型
 */
public interface NettyClient<V> extends Callable<V>, Destroyable, Initializable {
}
