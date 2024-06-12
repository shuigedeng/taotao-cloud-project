package com.taotao.cloud.rpc.common.common.remote.netty;

import com.taotao.cloud.rpc.common.common.api.AsyncRunnable;
import com.taotao.cloud.rpc.common.common.api.Destroyable;
import com.taotao.cloud.rpc.common.common.api.Initializable;

/**
 * netty 网络服务端
 * @author shuigedeng
 * @since 2024.06
 */
public interface NettyServer extends AsyncRunnable, Runnable, Destroyable, Initializable {
}
