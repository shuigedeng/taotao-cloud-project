package com.github.houbb.rpc.common.remote.netty;

import com.github.houbb.rpc.common.api.AsyncRunnable;
import com.github.houbb.rpc.common.api.Destroyable;
import com.github.houbb.rpc.common.api.Initializable;

/**
 * netty 网络服务端
 * @author shuigedeng
 * @since 0.0.8
 */
public interface NettyServer extends AsyncRunnable, Runnable, Destroyable, Initializable {
}
