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

package com.taotao.cloud.ccsr.client.remote.grpc;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.grpc.auto.*;
import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.client.lifecycle.Closeable;
import com.taotao.cloud.ccsr.client.listener.ConfigListenerManager;
import com.taotao.cloud.ccsr.client.loadbalancer.ServiceDiscovery;
import com.taotao.cloud.ccsr.common.enums.RaftGroup;
import com.taotao.cloud.ccsr.common.exception.CcsrClientException;
import com.taotao.cloud.ccsr.common.log.Log;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shuigedeng
 * @date 2025-03-26 11:50
 */
public abstract class GrpcConnection implements Closeable {

    protected ManagedChannel channel;

    protected MetadataServiceGrpc.MetadataServiceBlockingStub blockingStub;

    protected MetadataServiceGrpc.MetadataServiceFutureStub futureStub;

    protected MetadataServiceGrpc.MetadataServiceStub asyncStub;

    private String namespace;

    private List<ServerAddress> serverAddresses;

    private ScheduledExecutorService healthCheckScheduler;

    private final ServiceDiscovery discovery = new ServiceDiscovery();

    private void startHealthCheckScheduler() {
        if (healthCheckScheduler == null || healthCheckScheduler.isShutdown()) {
            healthCheckScheduler = Executors.newSingleThreadScheduledExecutor();
            healthCheckScheduler.scheduleAtFixedRate(this::healthCheck, 0, 30, TimeUnit.SECONDS);
        }
    }

    public GrpcConnection() {}

    public GrpcConnection(String namespace, List<ServerAddress> serverAddresses) {
        init(namespace, serverAddresses);
    }

    public void init(String namespace, List<ServerAddress> serverAddresses) {
        this.namespace = namespace;
        this.serverAddresses = new CopyOnWriteArrayList<>(serverAddresses); // 线程安全

        this.rebuildChannel();
        this.listening();
        this.startHealthCheckScheduler();
    }

    private void rebuildChannel() {
        // 过滤活跃地址并构建目标字符串
        // String target = buildTargetFromAddresses();
        //        String target = "127.0.0.1:8000,127.0.0.1:8100";
        // TODO 这里依赖gRPC默认的负载均衡策略 round_robin, 如果需要可以自定义
        //        this.channel = ManagedChannelBuilder.forTarget(target)

        discovery.update(serverAddresses);
        ServerAddress server = discovery.selector();

        Log.print("尝试连接... host:%s, port:%s", server.getHost(), server.getPort());

        this.channel =
                ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                        .defaultLoadBalancingPolicy("round_robin")
                        .usePlaintext() // 简化开发环境设置，无 SSL
                        .build();

        this.blockingStub = MetadataServiceGrpc.newBlockingStub(channel);
        this.futureStub = MetadataServiceGrpc.newFutureStub(channel);
        this.asyncStub = MetadataServiceGrpc.newStub(channel);
    }

    /**
     * @return eg: static:///127.0.0.1:8000,127.0.0.1:8001
     */
    private String buildTargetFromAddresses() {
        StringBuilder addresses = new StringBuilder("static:///");
        for (ServerAddress addr : serverAddresses) {
            if (addr.isActive()) {
                if (addresses.length() > "static:///".length()) {
                    addresses.append(",");
                }
                addresses.append(addr.getHost()).append(":").append(addr.getPort());
            }
        }
        return addresses.toString();
    }

    @Override
    public void shutdown() throws CcsrClientException {

        // 关闭健康检查调度器
        if (healthCheckScheduler != null) {
            try {
                healthCheckScheduler.shutdown();
                if (!healthCheckScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    healthCheckScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new CcsrClientException(e);
        }
    }

    private void reconnect() {
        Log.print("尝试重新连接...");
        this.channel.shutdown();
        this.rebuildChannel();
        this.listening();
    }

    public void listening() {
        MetadataSubscribeRequest request =
                MetadataSubscribeRequest.newBuilder()
                        .setNamespace(namespace)
                        .setRaftGroup(RaftGroup.CONFIG_CENTER_GROUP.getName())
                        .build();

        asyncStub.subscribe(
                request,
                new StreamObserver<>() {
                    @Override
                    public void onNext(MetadataSubscribeResponse response) {
                        Log.print(
                                "配置订阅监听: namespace=%s, eventType=%s, metadata=%s",
                                response.getNamespace(),
                                response.getOpType(),
                                response.getMetadata());
                        Metadata metadata = response.getMetadata();
                        EventType eventType = EventType.valueOf(response.getOpType());
                        ConfigListenerManager.fireEvent(metadata, eventType);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.print("配置订阅监听出错: " + t.getMessage());
                        reconnect();
                    }

                    @Override
                    public void onCompleted() {
                        Log.print("服务端链接关闭...");
                        // TODO 标记为不健康服务
                    }
                });
    }

    private void healthCheck() {
        try {
            for (ServerAddress addr : serverAddresses) {
                boolean reachable = checkConnectivity(addr);
                addr.setActive(reachable);

                // 记录状态变化
                if (reachable != addr.isActive()) {
                    Log.print(
                            "地址状态变化: "
                                    + addr.getHost()
                                    + ":"
                                    + addr.getPort()
                                    + " -> "
                                    + (reachable ? "激活" : "失效"));
                }
            }
        } catch (Exception e) {
            Log.error("健康检查异常: " + e.getMessage());
        }
    }

    private boolean checkConnectivity(ServerAddress addr) {
        // 实现方案1：简单TCP端口检查（快速但无法验证gRPC服务状态）
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(addr.getHost(), addr.getPort()), 1000);
            return true;
        } catch (IOException e) {
            discovery.markServerDown(addr.getHost(), addr.getPort());
            return false;
        }

        // 实现方案2：TODO gRPC健康检查（需要服务端实现健康检查协议）
    }
}
