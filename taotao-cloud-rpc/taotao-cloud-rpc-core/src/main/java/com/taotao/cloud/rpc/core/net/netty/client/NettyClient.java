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

package com.taotao.cloud.rpc.core.net.netty.client;

import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.factory.SingleFactory;
import com.taotao.cloud.rpc.common.loadbalancer.LoadBalancer;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import com.taotao.cloud.rpc.core.discovery.ServiceDiscovery;
import com.taotao.cloud.rpc.core.net.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 客户端
 */
@Slf4j
public class NettyClient implements RpcClient {

    /**
     * 不放到 抽象层 是 因为 不希望 被继承，子类会 破坏 CommonSerializer 的 完整性
     */
    private final CommonSerializer serializer;

    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;

    private String hostName;
    private int port;
    private ServiceDiscovery serviceDiscovery;

    private static UnprocessedRequests unprocessedRequests;

    /**
     * 采用直连模式
     *
     * @param hostName
     * @param port
     * @param serializerCode
     */
    public NettyClient(String hostName, int port, Integer serializerCode) {
        this.hostName = hostName;
        this.port = port;
        serializer = CommonSerializer.getByCode(serializerCode);
        unprocessedRequests = SingleFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * 采用 负载均衡 模式, nacos 作为 服务 注册中心
     *
     * @param loadBalancer
     * @param serializerCode
     */
    public NettyClient(LoadBalancer loadBalancer, Integer serializerCode) {
        // serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        /**
         * SPI 机制，接口与实现类解耦到 配置文件
         */
        serviceDiscovery = ServiceLoader.load(ServiceDiscovery.class).iterator().next();
        serviceDiscovery.setLoadBalancer(loadBalancer);
        serializer = CommonSerializer.getByCode(serializerCode);
        /**
         * fix bug
         */
        unprocessedRequests = SingleFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * 让 客户端 保持 连接，启动器（Bootstrap） 不中断，实际上的 处理操作 是在 ChannelProvider 中的 启动器（Bootstrap）
     */
    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
    }

    /**
     * 发送请求
     *
     * @param rpcRequest 请求体
     * @return 异步响应体，可使用get()方法在未来获取结果
     * @throws RpcException
     */
    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) throws RpcException {
        /**
         * if (serializer == null) {
         * log.error("Serializer not set" );
         * throw new SerializerNotSetException("Serializer not set Exception");
         * }
         */
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        /**
         * 启动 负载均衡 模式
         */
        if (serviceDiscovery != null) {
            String serviceName = rpcRequest.getInterfaceName();
            InetSocketAddress address = null;
            String group = rpcRequest.getGroup();
            if (group != null) {
                address = serviceDiscovery.lookupService(serviceName, group);
            } else {
                address = serviceDiscovery.lookupService(serviceName);
            }
            this.hostName = address.getHostName();
            this.port = address.getPort();
        }
        Channel channel = ChannelProvider.get(new InetSocketAddress(hostName, port), serializer);
        /**
         * 拿到 channel 之后 如果 出现 异常 已经在 ChannelProvider中 get channel 已经处理了，用户会得到 异常通知
         */
        if (!channel.isActive()) {
            group.shutdownGracefully();
            return null;
        }
        /**
         * fix(改进）
         * ChannelProvider 通过 CompletableFuture 的 complete 和 get 方法 获取了
         */
        unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);

        channel.writeAndFlush(rpcRequest)
                .addListener(
                        new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if (future.isSuccess()) {
                                    log.info(
                                            String.format(
                                                    "customer sent message: %s",
                                                    rpcRequest.toString()));
                                } else {
                                    future.channel().close();
                                    resultFuture.completeExceptionally(future.cause());
                                    log.error(
                                            "Error occurred while sending message: {}",
                                            future.cause());
                                }
                            }
                        });
        // channel.closeFuture().sync(); 做了 缓存处理
        /**
         * 与 NettyClientHandler 读取 channel 时 获取的是 同一个 AttributeKey<RpcResponse>
         */
        // AttributeKey<RpcResponse> key = AttributeKey.valueOf(rpcRequest.getRequestId());
        /**
         *  1. 每个 channel 都有 与之 对应 一个 DefaultAttributeMap
         *  2. netty 不 使用 ConcurrentHashMap 原因 : Not using ConcurrentHashMap due to high memory consumption.
         *  3. DefaultAttributeMap 是 一个 数组 + 链表 结构的 线程安全 map
         *  4. 数组采用的是 AtomicReferenceArray , 链表 采用的是 以 DefaultAttribute 为链表节点
         *  5. DefaultAttributeMap 中 头结点 head 不存放任何值，相当于哨兵
         *  6. next 节点 中 的 字段 key 存放 的是 已缓存的 AttributeKey
         *
         *  AttributeKey<RpcResponse> key = AttributeKey.valueOf(msg.getRequestId());
         *  ctx.channel().attr(key).set(msg);
         *
         *  存在的问题 (改正） 这里必须等到 channelRead0() 读取数据后 拿到值才能
         */
        // RpcResponse rpcResponse = channel.attr(key).get();
        /**
         *  数据包校验
         *  使用了 异步，数据校验 工具包 在这里 不再支持
         */
        // RpcMessageChecker.check(rpcRequest, rpcResponse);
        // System.out.println(resultFuture);

        return resultFuture;
    }
}
