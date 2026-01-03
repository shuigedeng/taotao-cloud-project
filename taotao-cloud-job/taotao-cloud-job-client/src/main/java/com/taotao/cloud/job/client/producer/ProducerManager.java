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

package com.taotao.cloud.job.client.producer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.job.client.producer.entity.ResponseFuture;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.RegisterCausa;
import com.taotao.cloud.remote.api.MqGrpc;
import com.taotao.cloud.remote.api.RegisterToNameServerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * ProducerManager
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class ProducerManager {

    private List<String> serverAddressList;
    private Long index = 0L;
    private final List<MqGrpc.MqFutureStub> stubs = new ArrayList<>();
    private List<ResponseFuture> responseList = new LinkedList<>();
    @Getter
    private ThreadPoolExecutor threadPoolExecutor;
    private ThreadPoolExecutor invokeCallbackExecute;
    private final Timer timer = new Timer("ClientHouseKeepingService", true);

    public ProducerManager( List<String> nameServerAddressList ) {
        // 初始化stub
        for (String server : nameServerAddressList) {
            ManagedChannel channel =
                    ManagedChannelBuilder.forAddress(
                                    server.split(":")[0], Integer.parseInt(server.split(":")[1]))
                            .usePlaintext()
                            .build();

            RegisterToNameServerGrpc.RegisterToNameServerBlockingStub stub =
                    RegisterToNameServerGrpc.newBlockingStub(channel);
            RegisterCausa.FetchServerAddressListReq build =
                    RegisterCausa.FetchServerAddressListReq.newBuilder().build();
            try {
                CommonCausa.Response response = stub.fetchServerList(build);
                serverAddressList = response.getServerAddressList().getServerAddressListList();
                initStubs();
                break;
            } catch (Exception e) {
                log.error("nameServer :{} connect error", server);
            }
        }

        // 初始化线程池
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        ThreadFactory producerExecuteFactory =
                new ThreadFactoryBuilder().setNameFormat("ttcjob-producer-send-%d").build();
        threadPoolExecutor =
                new ThreadPoolExecutor(
                        availableProcessors * 10,
                        availableProcessors * 10,
                        120L,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(( 1024 * 2 ), true),
                        producerExecuteFactory,
                        new ThreadPoolExecutor.AbortPolicy());
        ThreadFactory invokeCallbackExecuteFactory =
                new ThreadFactoryBuilder().setNameFormat("ttcjob-invokeCallback-%d").build();
        invokeCallbackExecute =
                new ThreadPoolExecutor(
                        availableProcessors * 10,
                        availableProcessors * 10,
                        120L,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(( 1024 * 2 ), true),
                        invokeCallbackExecuteFactory,
                        new ThreadPoolExecutor.AbortPolicy());

        // 初始化过期请求清理任务
        this.timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            scanResponseTable();
                        } catch (Throwable e) {
                            log.error("scanResponseTable exception", e);
                        }
                    }
                },
                1000 * 3,
                1000);
    }

    /**
     * 可能会在下一次扫描的时候才被检测到过期 误差只有1秒，这样设置是因为执行时间可能超过 1 秒，导致新一轮的检测稍微延迟
     */
    private void scanResponseTable() {
        final List<ResponseFuture> rfList = new LinkedList<ResponseFuture>();
        for (ResponseFuture rep : responseList) {
            if (( rep.getBeginTimestamp() + rep.getTimeoutMillis() + 1000 )
                    <= System.currentTimeMillis()) {
                responseList.remove(rep);
                rfList.add(rep);
                log.warn("remove timeout request, " + rep);
            }
        }
        for (ResponseFuture rf : rfList) {
            try {
                executeInvokeCallback(rf);
            } catch (Throwable e) {
                log.warn("scanResponseTable, operationComplete Exception", e);
            }
        }
    }

    private void executeInvokeCallback( ResponseFuture rf ) {
        invokeCallbackExecute.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rf.executeInvokeCallback();
                        } catch (Throwable e) {
                            log.warn(
                                    "execute callback in executor exception, and callback throw",
                                    e);
                        }
                    }
                });
    }

    private void initStubs() {
        for (String server : serverAddressList) {
            ManagedChannel channel =
                    ManagedChannelBuilder.forAddress(
                                    server.split(":")[0], Integer.parseInt(server.split(":")[1]))
                            .usePlaintext()
                            .build();
            stubs.add(MqGrpc.newFutureStub(channel));
        }
    }

    public MqGrpc.MqFutureStub getStub() {
        return stubs.get((int) ( index++ % stubs.size() ));
    }

    public int getRetryTime() {
        return stubs.size();
    }

    public void addResponseFuture( ResponseFuture responseFuture ) {
        responseList.add(responseFuture);
    }
}
