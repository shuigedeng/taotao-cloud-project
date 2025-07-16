/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.mq.consistency.raft1.common.rpc;

import com.alipay.remoting.exception.RemotingException;
import com.taotao.cloud.mq.consistency.raft1.common.exception.RaftRemotingException;
import java.util.concurrent.TimeUnit;

/**
 * 基于 bolt 的默认实现
 * @since 1.0.0
 */
public class DefaultRpcClient implements RpcClient {

    private com.alipay.remoting.rpc.RpcClient rpcClient;

    @Override
    public <R> R send(RpcRequest request) {
        return send(request, (int) TimeUnit.SECONDS.toMillis(10));
    }

    @Override
    public <R> R send(RpcRequest request, int timeoutMillis) {
        RpcResponse<R> result;
        try {
            result =
                    (RpcResponse<R>) rpcClient.invokeSync(request.getUrl(), request, timeoutMillis);
            return result.getResult();
        } catch (RemotingException e) {
            throw new RaftRemotingException("rpc RaftRemotingException ", e);
        } catch (InterruptedException e) {
            // ignore
        }
        return null;
    }

    @Override
    public void init() {
        rpcClient = new com.alipay.remoting.rpc.RpcClient();
        rpcClient.startup();
    }

    @Override
    public void destroy() {
        rpcClient.shutdown();
    }
}
