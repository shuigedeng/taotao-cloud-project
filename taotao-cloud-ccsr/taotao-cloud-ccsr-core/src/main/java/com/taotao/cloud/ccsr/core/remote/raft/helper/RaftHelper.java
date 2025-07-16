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

package com.taotao.cloud.ccsr.core.remote.raft.helper;

import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.alipay.sofa.jraft.rpc.impl.GrpcClient;
import com.alipay.sofa.jraft.rpc.impl.GrpcRaftRpcFactory;
import com.alipay.sofa.jraft.rpc.impl.MarshallerRegistry;
import com.alipay.sofa.jraft.util.RpcFactoryHelper;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.core.executor.NameThreadFactory;
import com.taotao.cloud.ccsr.core.remote.raft.RaftServer;
import com.taotao.cloud.ccsr.core.remote.raft.processor.DeleteRequestRpcProcessor;
import com.taotao.cloud.ccsr.core.remote.raft.processor.ReadRequestRpcProcessor;
import com.taotao.cloud.ccsr.core.remote.raft.processor.WriteRequestRpcProcessor;
import com.taotao.cloud.ccsr.core.serializer.SerializeFactory;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.commons.io.FileUtils;

/**
 * RaftHelper 类提供了初始化 Raft 相关 RPC 服务器的工具方法。
 */
public class RaftHelper {

    public static void initDirectory(
            String basePath, String group, PeerId peerId, NodeOptions options) {

        String peerPath = peerId.getIp() + "_" + peerId.getPort();
        final String logUri =
                Paths.get(
                                basePath,
                                "logs",
                                "taotao-cloud-ccsr-server",
                                "raft",
                                group,
                                peerPath,
                                "logs")
                        .toString();
        final String snapshotUri =
                Paths.get(
                                basePath,
                                "logs",
                                "taotao-cloud-ccsr-server",
                                "raft",
                                group,
                                peerPath,
                                "snapshot")
                        .toString();
        final String metaUri =
                Paths.get(
                                basePath,
                                "logs",
                                "taotao-cloud-ccsr-server",
                                "raft",
                                group,
                                peerPath,
                                "meta")
                        .toString();

        // Initialize the raft file storage path for different services
        try {
            FileUtils.forceMkdir(new File(logUri));
            FileUtils.forceMkdir(new File(snapshotUri));
            FileUtils.forceMkdir(new File(metaUri));
        } catch (Exception e) {
            Log.error("Init Raft-File dir have some error, cause: ", e);
            throw new RuntimeException(e);
        }

        options.setLogUri(logUri);
        options.setRaftMetaUri(metaUri);
        options.setSnapshotUri(snapshotUri);
    }

    public static RpcServer initServer(RaftServer server, PeerId peerId) {
        // 获取 GrpcRaftRpcFactory 实例，用于创建和管理 RPC 相关组件
        GrpcRaftRpcFactory rpcFactory = (GrpcRaftRpcFactory) RpcFactoryHelper.rpcFactory();
        // 注册 Protobuf 序列化器，用于处理不同类型的请求对象
        rpcFactory.registerProtobufSerializer(
                MetadataDeleteRequest.class.getName(), MetadataDeleteRequest.getDefaultInstance());
        rpcFactory.registerProtobufSerializer(
                MetadataReadRequest.class.getName(), MetadataReadRequest.getDefaultInstance());
        rpcFactory.registerProtobufSerializer(
                MetadataWriteRequest.class.getName(), MetadataWriteRequest.getDefaultInstance());
        rpcFactory.registerProtobufSerializer(
                Response.class.getName(), Response.getDefaultInstance());

        // 获取 MarshallerRegistry 实例，用于管理序列化和反序列化的注册信息
        MarshallerRegistry registry = rpcFactory.getMarshallerRegistry();
        // 注册每个请求类型对应的响应实例，用于在 RPC 调用中返回响应
        registry.registerResponseInstance(
                MetadataDeleteRequest.class.getName(), Response.getDefaultInstance());
        registry.registerResponseInstance(
                MetadataReadRequest.class.getName(), Response.getDefaultInstance());
        registry.registerResponseInstance(
                MetadataWriteRequest.class.getName(), Response.getDefaultInstance());

        // 根据节点的端点信息创建 RpcServer 实例
        final RpcServer rpcServer = rpcFactory.createRpcServer(peerId.getEndpoint());
        // 为 RpcServer 添加 Raft 请求处理器，使用固定大小的线程池处理请求
        RaftRpcServerFactory.addRaftRequestProcessors(
                rpcServer, getDefaultExecutor(), getDefaultExecutor());
        // 注册自定义的处理器，用于处理不同类型的请求
        rpcServer.registerProcessor(
                new WriteRequestRpcProcessor(server, SerializeFactory.getDefault()));
        rpcServer.registerProcessor(
                new ReadRequestRpcProcessor(server, SerializeFactory.getDefault()));
        rpcServer.registerProcessor(
                new DeleteRequestRpcProcessor(server, SerializeFactory.getDefault()));

        return rpcServer;
    }

    public static RpcClient initClient() {
        GrpcRaftRpcFactory rpcFactory = new GrpcRaftRpcFactory();
        rpcFactory.registerProtobufSerializer(
                MetadataWriteRequest.class.getName(), MetadataWriteRequest.getDefaultInstance());
        rpcFactory.registerProtobufSerializer(
                MetadataDeleteRequest.class.getName(), MetadataDeleteRequest.getDefaultInstance());
        rpcFactory.registerProtobufSerializer(
                MetadataReadRequest.class.getName(), MetadataReadRequest.getDefaultInstance());
        MarshallerRegistry registry = rpcFactory.getMarshallerRegistry();
        registry.registerResponseInstance(
                MetadataWriteRequest.class.getName(), Response.getDefaultInstance());
        registry.registerResponseInstance(
                MetadataDeleteRequest.class.getName(), Response.getDefaultInstance());
        registry.registerResponseInstance(
                MetadataReadRequest.class.getName(), Response.getDefaultInstance());

        Map<String, Message> parserClasses = new HashMap<>();
        parserClasses.put(
                MetadataWriteRequest.class.getName(), MetadataWriteRequest.getDefaultInstance());
        parserClasses.put(
                MetadataDeleteRequest.class.getName(), MetadataDeleteRequest.getDefaultInstance());
        parserClasses.put(
                MetadataReadRequest.class.getName(), MetadataReadRequest.getDefaultInstance());
        return new GrpcClient(parserClasses, registry);
    }

    public static ExecutorService getDefaultExecutor() {
        // 创建一个固定大小为 8 的线程池，并使用自定义的线程工厂命名线程
        return Executors.newFixedThreadPool(
                8, new NameThreadFactory("com.taotao.cloud.ccsr.remote.raft-default"));
    }

    public static ExecutorService getCliExecutor() {
        // 创建一个固定大小为 8 的线程池，并使用自定义的线程工厂命名线程
        return Executors.newFixedThreadPool(
                8, new NameThreadFactory("com.taotao.cloud.ccsr.remote.raft-cli"));
    }

    public static ScheduledExecutorService getScheduleExecutor() {
        return Executors.newScheduledThreadPool(
                8, new NameThreadFactory("com.taotao.cloud.ccsr.remote.raft-schedule"));
    }
}
