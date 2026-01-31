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

package com.taotao.cloud.ccsr.core.remote.grpc.impl;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.Any;
import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.event.GlobalEventBus;
import com.taotao.cloud.ccsr.api.event.MetadataChangeEvent;
import com.taotao.cloud.ccsr.api.grpc.auto.*;
import com.taotao.cloud.ccsr.api.listener.Listener;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.common.enums.RaftGroup;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.core.remote.grpc.GrpcService;
import com.taotao.cloud.ccsr.core.remote.raft.client.RaftClientFactory;
import com.taotao.cloud.ccsr.core.remote.raft.client.RaftClientWorker;
import com.taotao.cloud.ccsr.core.storage.MetadaStorage;
import com.taotao.cloud.ccsr.core.utils.StorageHolder;
import com.taotao.cloud.ccsr.spi.Join;
import io.grpc.Context;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import lombok.Getter;

/**
 * @author shuigedeng
 */
@Join(order = 1, isSingleton = true)
public class MetadataConfigServiceImpl extends MetadataServiceGrpc.MetadataServiceImplBase
        implements GrpcService {

    private static final String DEFAULT_NAMESPACE = "default";

    private final List<SubscribeObserver> subscribers = new CopyOnWriteArrayList<>();

    private final RaftClientWorker raftClientWorker;

    {
        GlobalEventBus.register(new MetadataChangeListener());
    }

    public MetadataConfigServiceImpl() {
        RaftClientFactory factory = RaftClientFactory.getInstance();
        raftClientWorker = new RaftClientWorker(factory);
    }

    /**
     * MetadataChangeListener
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    public class MetadataChangeListener implements Listener<MetadataChangeEvent> {

        @Override
        @Subscribe
        public void onSubscribe( MetadataChangeEvent event ) {
            // Handle the metadata change event
            Log.print(
                    "MetadataChangeListener->收到监听消息, type=%s metadata=%s",
                    event.type(), event.metadata());
            broadcast(event.type(), event.metadata());
        }
    }

    /**
     * SubscribeObserver
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    @Getter
    private static class SubscribeObserver {

        String namespace;
        StreamObserver<MetadataSubscribeResponse> responseObserver;

        SubscribeObserver(
                String namespace, StreamObserver<MetadataSubscribeResponse> responseObserver ) {
            this.namespace = namespace != null ? namespace : DEFAULT_NAMESPACE;
            this.responseObserver = responseObserver;
        }
    }

    /**
     * subscribe method
     */
    @Override
    public void subscribe(
            MetadataSubscribeRequest request,
            StreamObserver<MetadataSubscribeResponse> responseObserver ) {
        SubscribeObserver subscribeObserver =
                new SubscribeObserver(request.getNamespace(), responseObserver);
        subscribers.add(subscribeObserver);

        // Automatically cleans up disconnected connections
        Context.current()
                .addListener(
                        context -> subscribers.remove(subscribeObserver),
                        MoreExecutors.directExecutor());
    }

    @Override
    public void get( MetadataReadRequest request, StreamObserver<Response> responseObserver ) {
        try {
            // TODO 这里应该改成从多级缓存中取
            MetadaStorage storage = StorageHolder.getInstance("metadata");
            String key =
                    storage.key(
                            request.getNamespace(),
                            request.getGroup(),
                            request.getTag(),
                            request.getDataId());
            Metadata data = storage.get(key);

            Response response = ResponseHelper.success(Any.pack(data));
            // Respond to the client immediately
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void put( MetadataWriteRequest request, StreamObserver<Response> responseObserver ) {
        try {
            Response response = raftClientWorker.invoke(request);

            // Respond to the client immediately
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void delete( MetadataDeleteRequest request, StreamObserver<Response> responseObserver ) {
        try {
            Response response = raftClientWorker.invoke(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    /**
     * broadcast to all subscribers
     */
    private void broadcast( EventType eventType, Metadata metadata ) {
        // TODO 这里通过长链接持有，进行向客户端广播的时候。如果客户端数量较多，可能会阻塞Raft主线程
        Iterator<SubscribeObserver> it = subscribers.iterator();
        while (it.hasNext()) {
            SubscribeObserver subscribe = it.next();
            try {
                if (isMatch(metadata, subscribe) && !eventType.isGet()) {
                    MetadataSubscribeResponse response =
                            buildSubscribeResponse(metadata, eventType);
                    // TODO 采集广播日志
                    Log.print("broadcast【执行广播】: eventType=%s, response=%s", eventType, response);
                    subscribe.getResponseObserver().onNext(response);
                }
            } catch (StatusRuntimeException e) {
                // Automatically cleans up invalid connections
                Log.print("remove invalid subscriber: %s", subscribe);
                it.remove();
            }
        }
    }

    private static boolean isMatch( Metadata metadata, SubscribeObserver subscribe ) {
        return subscribe.getNamespace().equals(metadata.getNamespace());
    }

    private MetadataSubscribeResponse buildSubscribeResponse(
            Metadata metadata, EventType eventType ) {
        return MetadataSubscribeResponse.newBuilder()
                .setNamespace(metadata.getNamespace())
                .setMsg(ResponseCode.SUCCESS.getMsg())
                .setSuccess(true)
                .setOpType(eventType.name())
                .setMetadata(metadata)
                .setRaftGroup(RaftGroup.CONFIG_CENTER_GROUP.getName())
                .build();
    }
}
