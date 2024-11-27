package com.taotao.cloud.job.server.jobserver.common.grpc;

import com.taotao.cloud.common.constant.RemoteConstant;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.server.extension.singletonpool.GrpcStubSingletonPool;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
@Component
public class PingServerRpcClient implements RpcServiceCaller{

    @Override
    public Object call(Object params) {
        ServerDiscoverCausa.Ping ping = (ServerDiscoverCausa.Ping)params;
        ServerDiscoverGrpc.ServerDiscoverFutureStub serverDiscoverFutureStub = GrpcStubSingletonPool.getStubSingleton(ping.getTargetServer(), ServerDiscoverGrpc.class, ServerDiscoverGrpc.ServerDiscoverFutureStub.class, RemoteConstant.SERVER);
        try {
            // 发送异步请求并等待响应，超时设置为5秒
            CommonCausa.Response response = serverDiscoverFutureStub.pingServer(ping).get(5, TimeUnit.SECONDS);
            return response;
        } catch (
                ExecutionException e) {
            // 处理RPC调用异常
            System.err.println("RPC failed: " + e.getCause().getMessage());
        } catch (
                TimeoutException e) {
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
