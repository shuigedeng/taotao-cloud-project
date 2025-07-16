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

package com.taotao.cloud.job.server.extension.singletonpool;

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.server.common.config.TtcJobServerConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * server的stub是动态创建，需要单例池（因为没有配置worker的信息，worker的信息是注册的时候动态获得的）
 * 假如都是一开始就知道，
 */
@Component
public class GrpcStubSingletonPool {
    @Autowired static TtcJobServerConfig config;

    // 通过 ConcurrentHashMap（线程安全） 实现单例注册表
    private static final Map<String, Object> stubSingletons =
            new ConcurrentHashMap<String, Object>(64);
    private static final Map<String, ManagedChannel> channelSingletons =
            new ConcurrentHashMap<String, ManagedChannel>(64);

    @SuppressWarnings("unchecked")
    // 获取单例对象，若不存在则创建并添加
    public static <T> T getStubSingleton(
            String serverAddress, Class<?> grpcClientClass, Class<T> stubClass, String type) {
        ManagedChannel channel = getChannelSingleton(serverAddress, type);
        //        new ServerDiscoverGrpc.ServerDiscoverFutureStub(channel);

        Method[] methods = grpcClientClass.getDeclaredMethods();
        Method targetMethod = null;
        for (Method method : methods) {
            if (method.getReturnType().equals(stubClass)) {
                targetMethod = method;
            }
        }
        // 通过 computeIfAbsent 来确保线程安全地创建和存储单例对象

        Method finalTargetMethod = targetMethod;
        return (T)
                stubSingletons.computeIfAbsent(
                        serverAddress + stubClass.getTypeName() + type,
                        key -> {
                            try {
                                // 通过反射创建指定的 Stub 实例
                                return finalTargetMethod.invoke(grpcClientClass, channel);

                            } catch (Exception e) {
                                throw new RuntimeException(
                                        "Failed to create gRPC stub for server: " + serverAddress,
                                        e);
                            }
                        });
    }

    /**
     * 每个server对应一个channel
     * @param serverAddress
     * @param type
     * @return
     */
    private static ManagedChannel getChannelSingleton(String serverAddress, String type) {
        // 避免出现不同的服务拿到同样的channel，故区分端口
        int port = 0;
        switch (type) {
            case RemoteConstant.SERVER:
                port = TtcJobServerConfig.staticServerPort;
                break;
            case RemoteConstant.WORKER:
                port = TtcJobServerConfig.staticWorkerPort;
                break;
            case RemoteConstant.NAMESERVER:
                port = Integer.parseInt(TtcJobServerConfig.staticNameServerAddress.split(":")[1]);
        }
        //        int port = type.equals(RemoteConstant.SERVER) ?
        // RemoteConstant.DEFAULT_SERVER_GRPC_PORT : RemoteConstant.DEFAULT_WORKER_GRPC_PORT;
        // 通过 computeIfAbsent 来确保线程安全地创建和存储单例对象
        int finalPort = port;
        return channelSingletons.computeIfAbsent(
                serverAddress + type,
                key -> {
                    try {
                        ManagedChannel channel =
                                ManagedChannelBuilder.forAddress(serverAddress, finalPort)
                                        .usePlaintext()
                                        .build();
                        // 通过反射创建指定的 Stub 实例
                        return channel;
                    } catch (Exception e) {
                        throw new RuntimeException(
                                "Failed to create gRPC channel for server: " + serverAddress, e);
                    }
                });
    }
}
