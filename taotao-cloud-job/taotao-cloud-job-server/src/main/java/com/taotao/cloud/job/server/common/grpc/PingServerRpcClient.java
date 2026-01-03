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

package com.taotao.cloud.job.server.common.grpc;

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.job.server.extension.singletonpool.GrpcStubSingletonPool;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

/**
 * PingServerRpcClient
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Component
public class PingServerRpcClient implements RpcServiceCaller {

    @Override
    public Object call( Object params ) {
        ServerDiscoverCausa.Ping ping = (ServerDiscoverCausa.Ping) params;
        ServerDiscoverGrpc.ServerDiscoverFutureStub serverDiscoverFutureStub =
                GrpcStubSingletonPool.getStubSingleton(
                        ping.getTargetServer(),
                        ServerDiscoverGrpc.class,
                        ServerDiscoverGrpc.ServerDiscoverFutureStub.class,
                        RemoteConstant.SERVER);
        try {
            // 发送异步请求并等待响应，超时设置为5秒
            CommonCausa.Response response =
                    serverDiscoverFutureStub.pingServer(ping).get(5, TimeUnit.SECONDS);
            return response;
        } catch (ExecutionException e) {
            // 处理RPC调用异常
            System.err.println("RPC failed: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            // 处理超时
            System.err.println("Request timed out");
        } catch (InterruptedException e) {
            // 处理中断异常
            Thread.currentThread().interrupt();
            System.err.println("Request was interrupted");
        }
        return null;
    }
}
