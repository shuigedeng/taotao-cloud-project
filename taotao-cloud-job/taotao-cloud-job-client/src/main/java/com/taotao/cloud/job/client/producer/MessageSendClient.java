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

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.taotao.cloud.job.client.producer.entity.ResponseFuture;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.MqCausa;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSendClient {
    private final ProducerManager producerManager;

    /**
     * server的个数
     */
    private final int retryTime;

    public MessageSendClient(String nameServerAddress) {
        ArrayList<String> nameServerAddressList = Lists.newArrayList(nameServerAddress);
        producerManager = new ProducerManager(nameServerAddressList);
        retryTime = producerManager.getRetryTime();
    }

    /**
     * 其实跟netty的addListener类似，都是封装了Future（这里是ResponseFuture）
     * 在异步得到数据后触发封装Future的回调
     * 回调后判断结果，再进行重试
     *
     * @param curTryTimes
     * @param msg
     */
    public void sendMessageAsync(AtomicInteger curTryTimes, MqCausa.Message msg) {
        invokeAsync(
                msg,
                new InvokeCallback() {
                    @Override
                    public void operationComplete(ResponseFuture responseFuture) {
                        if (responseFuture.isTimeout()) {
                            log.error("[TtcJobProducer] send message timeout");
                            onExceptionImpl(retryTime, curTryTimes, msg);
                        } else if (!responseFuture.isSendResponseOK()) {
                            log.error("[TtcJobProducer] send message error");
                            onExceptionImpl(retryTime, curTryTimes, msg);
                        } else {
                            log.error("[TtcJobProducer] send message error for unknownReason");
                            onExceptionImpl(retryTime, curTryTimes, msg);
                        }
                    }
                });
    }

    private void onExceptionImpl(int retryTime, AtomicInteger curRetryTimes, MqCausa.Message msg) {
        curRetryTimes.incrementAndGet();
        if (curRetryTimes.get() < retryTime) {
            try {
                log.info("[TtcJobProducer] send message retry times:{}", curRetryTimes);
                sendMessageAsync(curRetryTimes, msg);
            } catch (Exception ignored) {
            }
        }
    }

    private void invokeAsync(MqCausa.Message msg, InvokeCallback invokeCallback) {

        ListenableFuture<CommonCausa.Response> future = producerManager.getStub().send(msg);
        ResponseFuture responseFuture = new ResponseFuture(invokeCallback);
        producerManager.addResponseFuture(responseFuture);
        future.addListener(
                () -> {
                    try {
                        CommonCausa.Response response = future.get();
                        // 检查响应结果
                        if (response.getCode() == 200) {
                            // 这里不用马上回调，无需保证实时性，否则在scanResponse中还要去重
                            responseFuture.setSendResponseOK(true);
                            return;
                        } else {
                            responseFuture.setSendResponseOK(false);
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                },
                producerManager.getThreadPoolExecutor());
    }
}
