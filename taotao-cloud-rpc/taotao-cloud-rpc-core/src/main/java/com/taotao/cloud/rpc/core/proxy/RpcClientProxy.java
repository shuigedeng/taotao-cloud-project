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

package com.taotao.cloud.rpc.core.proxy;

import com.taotao.cloud.rpc.common.annotation.Reference;
import com.taotao.cloud.rpc.common.exception.AsyncTimeUnreasonableException;
import com.taotao.cloud.rpc.common.exception.RetryTimeoutException;
import com.taotao.cloud.rpc.common.exception.RpcTransmissionException;
import com.taotao.cloud.rpc.common.factory.SingleFactory;
import com.taotao.cloud.rpc.common.idworker.Sid;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import com.taotao.cloud.rpc.common.util.NacosUtils;
import com.taotao.cloud.rpc.common.util.RpcMessageChecker;
import com.taotao.cloud.rpc.core.hook.ClientShutdownHook;
import com.taotao.cloud.rpc.core.net.RpcClient;
import com.taotao.cloud.rpc.core.net.netty.client.NettyClient;
import com.taotao.cloud.rpc.core.net.netty.client.UnprocessedRequests;
import com.taotao.cloud.rpc.core.net.socket.client.SocketClient;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * RpcClientProxy
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    /**
     * 在调用 invoke 返回数据给 服务器时 连同 主机号和端口号 一起发送
     * <p>
     * private String hostName; private int port;
     */
    private RpcClient rpcClient;

    /**
     * 需要获取的成员所在类
     */
    private Class<?> pareClazz = null;

    private AtomicInteger sucRes = new AtomicInteger(0);
    private AtomicInteger errRes = new AtomicInteger(0);
    private AtomicInteger timeoutRes = new AtomicInteger(0);

    /**
     * 未处理请求，主要处理失败请求，多线程共享
     */
    private static UnprocessedRequests unprocessedRequests =
            SingleFactory.getInstance(UnprocessedRequests.class);

    static {
        /**
         * 预加载
         */
        NacosUtils.init();
    }

    /**
     *
     */
    public RpcClientProxy( RpcClient rpcClient ) {
        this.rpcClient = rpcClient;
        /**
         * 客户端 清除钩子
         */
        ClientShutdownHook.getShutdownHook().addClearAllHook();
    }

    /**
     * 用于可 超时重试 的动态代理，需要配合 @Reference使用 兼容 阻塞模式 asyncTime 字段 缺省 或者 <= 0 将启用 注意，此时 timeout 、 retries 字段将失效
     *
     * @param clazz 获取的服务类
     * @param pareClazz 使用 @Reference 所在类
     */
    public <T> T getProxy( Class<T> clazz, Class<?> pareClazz ) {
        this.pareClazz = pareClazz;
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 用于普通动态代理，@Reference 将失效，已过时，不推荐使用 原因：无法识别到 @Reference, 服务名 和 版本号 不可用
     *
     * @param clazz 获取的服务类
     */
    @Deprecated
    public <T> T getProxy( Class<T> clazz ) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {

        RpcRequest rpcRequest =
                new RpcRequest.Builder()
                        /**
                         * 使用雪花算法 解决分布式 RPC 各节点生成请求号 id 一致性问题
                         */
                        .requestId(Sid.next())
                        /**
                         * 没有处理时间戳一致问题，可通过 synchronized 锁阻塞来获取
                         */
                        // .requestId(UUID.randomUUID().toString())
                        .interfaceName(method.getDeclaringClass().getName())
                        .methodName(method.getName())
                        .parameters(args)
                        .paramTypes(method.getParameterTypes())
                        .returnType(method.getReturnType())
                        /**这里心跳指定为false，一般由另外其他专门的心跳 handler 来发送
                         * 如果发送 并且 hearBeat 为 true，说明触发发送心跳包
                         */
                        .heartBeat(false)
                        .build();

        RpcResponse rpcResponse = null;

        if (pareClazz == null) {
            log.info("invoke method:{}#{}", method.getDeclaringClass().getName(), method.getName());
            if (rpcClient instanceof NettyClient) {
                CompletableFuture<RpcResponse> completableFuture =
                        (CompletableFuture<RpcResponse>) rpcClient.sendRequest(rpcRequest);
                rpcResponse = completableFuture.get();
            }
            if (rpcClient instanceof SocketClient) {
                rpcResponse = (RpcResponse) rpcClient.sendRequest(rpcRequest);
            }
            RpcMessageChecker.checkAndThrow(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        }

        Field[] fields = pareClazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Reference.class)
                    && method.getDeclaringClass().getName().equals(field.getType().getName())) {
                String name = field.getAnnotation(Reference.class).name();
                String group = field.getAnnotation(Reference.class).group();
                if (!"".equals(name)) {
                    rpcRequest.setInterfaceName(name);
                }
                if (!"".equals(group)) {
                    rpcRequest.setGroup(group);
                }
                break;
            }
        }

        log.info("invoke method:{}#{}", method.getDeclaringClass().getName(), method.getName());

        if (rpcClient instanceof NettyClient) {
            /**
             * 重试机制实现
             */
            long timeout = 0L;
            long asyncTime = 0L;
            int retries = 0;
            boolean useRetry = false;

            /**
             * 匹配该代理方法所调用 的服务实例，是则获取相关注解信息 并跳出循环
             */
            for (Field field : fields) {
                if (field.isAnnotationPresent(Reference.class)
                        && method.getDeclaringClass().getName().equals(field.getType().getName())) {
                    retries = field.getAnnotation(Reference.class).retries();
                    timeout = field.getAnnotation(Reference.class).timeout();
                    asyncTime = field.getAnnotation(Reference.class).asyncTime();
                    useRetry = true;
                    break;
                }
            }
            /**
             * 1、识别不到 @Reference 注解执行
             * 2、识别到 @Reference 且 asyncTime 缺省 或 asyncTime <= 0
             */
            if (!useRetry || asyncTime <= 0) {
                log.debug("discover @Reference or asyncTime <= 0, will use blocking mode");
                long startTime = System.currentTimeMillis();
                CompletableFuture<RpcResponse> completableFuture =
                        (CompletableFuture<RpcResponse>) rpcClient.sendRequest(rpcRequest);
                rpcResponse = completableFuture.get();
                long endTime = System.currentTimeMillis();

                log.info("handling the task takes time {} ms", endTime - startTime);

                RpcMessageChecker.checkAndThrow(rpcRequest, rpcResponse);
            } else {
                /**
                 * 识别到 @Reference 注解 且 asyncTime > 0 执行
                 */
                log.debug("discover @Reference and asyncTime > 0, will use blocking mode");
                if (timeout >= asyncTime) {
                    log.error(
                            "asyncTime [ {} ] should be greater than timeout [ {} ]",
                            asyncTime,
                            timeout);
                    throw new AsyncTimeUnreasonableException(
                            "Asynchronous time is unreasonable, it should greater than timeout");
                }
                long handleTime = 0;
                boolean checkPass = false;
                for (int i = 0; i < retries; i++) {
                    long startTime = System.currentTimeMillis();

                    CompletableFuture<RpcResponse> completableFuture =
                            (CompletableFuture<RpcResponse>) rpcClient.sendRequest(rpcRequest);
                    try {
                        rpcResponse = completableFuture.get(asyncTime, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException e) {
                        // 忽视 超时引发的异常，自行处理，防止程序中断
                        timeoutRes.incrementAndGet();
                        log.warn(
                                "recommend that asyncTime [ {} ] should be greater than current task runeTime [ {} ]",
                                asyncTime,
                                System.currentTimeMillis() - startTime);
                        continue;
                    }

                    long endTime = System.currentTimeMillis();
                    handleTime = endTime - startTime;
                    log.info("handling the task takes time {} ms", handleTime);

                    if (handleTime >= timeout) {
                        // 超时重试
                        log.warn(
                                "invoke service timeout and retry to invoke [ rms: {}, tms: {} ]",
                                handleTime,
                                timeout);
                        log.info("client  call timeout counts {}", timeoutRes.incrementAndGet());
                    } else {
                        // 没有超时不用再重试
                        // 进一步校验包
                        checkPass = RpcMessageChecker.check(rpcRequest, rpcResponse);
                        if (checkPass) {
                            log.info(
                                    "client call success counts {} [ rms: {}, tms: {} ]",
                                    sucRes.incrementAndGet(),
                                    handleTime,
                                    timeout);
                            return rpcResponse.getData();
                        }
                        // 包被 劫持触发 超时重发机制 保护重发
                    }
                }
                // 最后一次 重发请求包 仍被劫持
                if (!checkPass) {
                    throw new RpcTransmissionException(
                            "RPC data transmission is abnormal, the packet is hijacked to trigger the retransmission mechanism, the retransmission mechanism is frequently hijacked under retry, and the operation is interrupted here");
                }
                log.info(
                        "client call failed counts {} [ rms: {}, tms: {} ]",
                        errRes.incrementAndGet(),
                        handleTime,
                        timeout);
                // 客户端在这里无法探知是否成功收到服务器响应，只能确定该请求包 客户端已经抛弃了
                unprocessedRequests.remove(rpcRequest.getRequestId());
                throw new RetryTimeoutException(
                        "The retry call timeout exceeds the threshold, the channel is closed, the thread is interrupted, and an exception is forced to be thrown!");
            }
        }

        if (rpcClient instanceof SocketClient) {
            rpcResponse = (RpcResponse) rpcClient.sendRequest(rpcRequest);
            RpcMessageChecker.checkAndThrow(rpcRequest, rpcResponse);
        }
        return rpcResponse.getData();
    }
}
