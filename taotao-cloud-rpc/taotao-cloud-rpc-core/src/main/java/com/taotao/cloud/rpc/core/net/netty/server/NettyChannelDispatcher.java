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

package com.taotao.cloud.rpc.core.net.netty.server;

import com.alibaba.nacos.common.utils.StringUtils;
import com.taotao.cloud.rpc.common.factory.ThreadPoolFactory;
import com.taotao.cloud.rpc.common.idworker.utils.JRedisHelper;
import com.taotao.cloud.rpc.common.idworker.utils.LRedisHelper;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import com.taotao.cloud.rpc.common.util.JsonUtils;
import com.taotao.cloud.rpc.common.util.PropertiesConstants;
import com.taotao.cloud.rpc.core.handler.RequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Netty Channel分发器
 */
@Slf4j
public class NettyChannelDispatcher {

    /**
     * netty 服务端采用 线程池处理耗时任务 抛弃 - 减少内存 开销，除了 Netty 内部必要的 事件线程组，其他线程的生命周期都由 ThreadPoolFactory 来管理
     * <p>
     * private static final EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(16);
     */
    private static ExecutorService operationExecutorService =
            ThreadPoolFactory.createDefaultThreadPool("operation-executor-pool");

    /**
     * Lettuce 分布式缓存采用 HESSIAN 序列化方式
     */
    private static CommonSerializer serializer =
            CommonSerializer.getByCode(CommonSerializer.HESSIAN_SERIALIZER);

    /**
     * 请求处理器
     */
    private static RequestHandler requestHandler;

    /**
     * redisServerWay: 超时重试 Redis 服务端 api 方式 redisServerAsync: 超时重试 Redis 服务端 异步开关
     */
    private static String redisServerWay = "";

    private static String redisServerAsync = "";

    static {
        // 使用InPutStream流读取properties文件
        String currentWorkPath = System.getProperty("user.dir");
        PropertyResourceBundle configResource = null;
        try (BufferedReader bufferedReader =
                new BufferedReader(
                        new FileReader(currentWorkPath + "/config/resource.properties")); ) {

            configResource = new PropertyResourceBundle(bufferedReader);
            redisServerWay = configResource.getString(PropertiesConstants.REDIS_SERVER_WAY);

            if ("jedis".equals(redisServerWay)
                    || "default".equals(redisServerWay)
                    || StringUtils.isBlank(redisServerWay)) {
                log.info("find redis client way attribute is jedis");
            } else if ("lettuce".equals(redisServerWay)) {
                log.info("find redis client way attribute is lettuce");
                /**
                 * 由于 LRedisHelper 首次启动需要创建线程池，主动触发懒加载进行预加载
                 */
                // LRedisHelper.preLoad();
                try {
                    redisServerAsync =
                            configResource.getString(PropertiesConstants.REDIS_SERVER_ASYNC);

                    if ("false".equals(redisServerAsync)
                            || "default".equals(redisServerAsync)
                            || StringUtils.isBlank(redisServerAsync)) {
                        log.info("find redis server async attribute is false");
                    } else if ("true".equals(redisServerAsync)) {
                        log.info("find redis server async attribute is lettuce");
                    } else {
                        throw new RuntimeException("redis server async attribute is illegal!");
                    }

                } catch (MissingResourceException redisServerAsyncException) {
                    log.warn("redis server async attribute is missing");
                    log.info("use default redis server default async: false");
                    redisServerAsync = "false";
                }
            } else {
                throw new RuntimeException("redis server async attribute is illegal!");
            }

        } catch (MissingResourceException redisServerWayException) {
            log.warn("redis client way attribute is missing");
            log.info("use default redis client default way: jedis");
            redisServerWay = "jedis";
        } catch (IOException ioException) {
            log.info(
                    "not found resource from resource path: {}",
                    currentWorkPath + "/config/resource.properties");
            try {
                ResourceBundle resource = ResourceBundle.getBundle("resource");
                redisServerWay = resource.getString(PropertiesConstants.REDIS_SERVER_WAY);
                if ("jedis".equals(redisServerWay)
                        || "default".equals(redisServerWay)
                        || StringUtils.isBlank(redisServerWay)) {
                    log.info("find redis server way attribute is jedis");
                } else if ("lettuce".equals(redisServerWay)) {
                    log.info("find redis server way attribute is lettuce");
                    /**
                     * 由于 LRedisHelper 首次启动需要创建线程池，主动触发懒加载进行预加载
                     */
                    // LRedisHelper.preLoad();
                    try {
                        redisServerAsync =
                                resource.getString(PropertiesConstants.REDIS_SERVER_ASYNC);

                        if ("false".equals(redisServerAsync)
                                || "default".equals(redisServerAsync)
                                || StringUtils.isBlank(redisServerAsync)) {
                            log.info("find redis server async attribute is false");
                        } else if ("true".equals(redisServerAsync)) {
                            log.info("find redis server async attribute is lettuce");
                        } else {
                            throw new RuntimeException("redis server async attribute is illegal!");
                        }

                    } catch (MissingResourceException redisServerAsyncException) {
                        log.warn("redis server async attribute is missing");
                        log.info("use default redis server default async: false");
                        redisServerAsync = "false";
                    }
                } else {
                    throw new RuntimeException("redis client way attribute is illegal!");
                }

            } catch (MissingResourceException resourceException) {
                log.info("not found resource from resource path: {}", "resource.properties");
                log.info("use default redis server way: jedis");
                redisServerWay = "jedis";
            }
            log.info("read resource from resource path: {}", "resource.properties");
        }
        requestHandler = new RequestHandler();
    }

    public static void init() {
        log.info("netty channel dispatcher initialize successfully!");
    }

    public static void dispatch(ChannelHandlerContext ctx, RpcRequest msg) {
        operationExecutorService.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            log.info("server has received request package: {}", msg);

                            // 到了这一步，如果请求包在上一次已经被 服务器成功执行，接下来要做幂等性处理，也就是客户端设置超时重试处理

                            /**
                             * 改良
                             * 使用 Redis 实现分布式缓存
                             *
                             */
                            Object result = null;

                            if ("jedis".equals(redisServerWay)
                                    || "default".equals(redisServerWay)
                                    || StringUtils.isBlank(redisServerWay)) {
                                if (!JRedisHelper.existsRetryResult(msg.getRequestId())) {
                                    log.info(
                                            "requestId[{}] does not exist, store the result in the distributed cache",
                                            msg.getRequestId());
                                    result = requestHandler.handler(msg);
                                    if (result != null) {
                                        JRedisHelper.setRetryRequestResult(
                                                msg.getRequestId(), JsonUtils.objectToJson(result));
                                    } else {
                                        JRedisHelper.setRetryRequestResult(
                                                msg.getRequestId(), null);
                                    }
                                } else {
                                    result = JRedisHelper.getForRetryRequestId(msg.getRequestId());
                                    if (result != null) {
                                        result =
                                                JsonUtils.jsonToPojo(
                                                        (String) result, msg.getReturnType());
                                    }
                                    log.info("Previous results:{} ", result);
                                    log.info(
                                            " >>> Capture the timeout packet and call the previous result successfully <<< ");
                                }
                            } else {

                                if (LRedisHelper.existsRetryResult(msg.getRequestId()) == 0L) {
                                    log.info(
                                            "requestId[{}] does not exist, store the result in the distributed cache",
                                            msg.getRequestId());
                                    result = requestHandler.handler(msg);

                                    if ("true".equals(redisServerAsync) && result != null) {
                                        LRedisHelper.asyncSetRetryRequestResult(
                                                msg.getRequestId(), serializer.serialize(result));
                                    } else {
                                        if (result != null) {
                                            LRedisHelper.syncSetRetryRequestResult(
                                                    msg.getRequestId(),
                                                    serializer.serialize(result));
                                        } else {
                                            LRedisHelper.syncSetRetryRequestResult(
                                                    msg.getRequestId(), null);
                                        }
                                    }
                                } else {
                                    result = LRedisHelper.getForRetryRequestId(msg.getRequestId());
                                    if (result != null) {
                                        result =
                                                serializer.deserialize(
                                                        (byte[]) result, msg.getReturnType());
                                    }
                                    log.info("Previous results:{} ", result);
                                    log.info(
                                            " >>> Capture the timeout packet and call the previous result successfully <<< ");
                                }
                            }

                            /**
                             * 这里要防止重试
                             * 分为两种情况
                             * 1. 如果是 客户端发送给服务端 途中出现问题，请求包之前 服务器未获取到，也就是 唯一请求id号 没有重复
                             * 2. 如果是 服务端发回客户端途中出现问题，导致客户端触发 超时重试，这时服务端会 接收 重试请求包，也就是有 重复请求id号
                             *
                             * // 请求id 为第一次请求 id
                             * Object result = null;
                             * if (timeoutRetryRequestIdSet.add(msg.getRequestId())) {
                             * result = requestHandler.handler(msg);
                             * resMap.put(msg.getRequestId(), result);
                             * //请求id 为第二次或以上请求
                             * } else {
                             * result = resMap.get(msg.getRequestId());
                             * }
                             */
                            // 生成 校验码，客户端收到后 会 对 数据包 进行校验
                            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                                /**
                                 * 这里要分两种情况：
                                 * 1. 当数据无返回值时，保证 checkCode 与 result 可以检验，客户端 也要判断 result 为 null 时 checkCode 是否也为 null，才能认为非他人修改
                                 * 2. 当数据有返回值时，校验 checkCode 与 result 的 md5 码 是否相同
                                 */
                                String checkCode = "";
                                // 这里做了 当 data为 null checkCode 为 null，checkCode可作为 客户端的判断 返回值 依据
                                if (result != null) {
                                    try {
                                        checkCode =
                                                new String(
                                                        DigestUtils.md5(
                                                                result.toString()
                                                                        .getBytes("UTF-8")));
                                    } catch (UnsupportedEncodingException e) {
                                        log.error("binary stream conversion failure: ", e);
                                        // e.printStackTrace();
                                    }
                                } else {
                                    checkCode = null;
                                }
                                RpcResponse rpcResponse =
                                        RpcResponse.success(result, msg.getRequestId(), checkCode);
                                log.info(
                                        String.format(
                                                "server send back response package {requestId: %s, message: %s, statusCode: %s ]}",
                                                rpcResponse.getRequestId(),
                                                rpcResponse.getMessage(),
                                                rpcResponse.getStatusCode()));
                                ChannelFuture future = ctx.writeAndFlush(rpcResponse);

                                /**
                                 * 大于 1000 条请求id 时，及时清除不用的请求 id
                                 * 保存此时 服务接收的请求 id
                                 * 考虑多线程中 对其他 线程刚添加的请求id 进行清除的影响
                                 *
                                 * if (timeoutRetryRequestIdSet.size() >= 1000) {
                                 * synchronized (this) {
                                 * if (timeoutRetryRequestIdSet.size() >= 1000) {
                                 * timeoutRetryRequestIdSet.clear();
                                 * resMap.clear();
                                 * timeoutRetryRequestIdSet.add(msg.getRequestId());
                                 * resMap.put(msg.getRequestId(), result);
                                 * } else {
                                 * timeoutRetryRequestIdSet.add(msg.getRequestId());
                                 * resMap.put(msg.getRequestId(), result);
                                 * }
                                 * }
                                 * } */
                            } else {
                                log.info(
                                        "channel status [active: {}, writable: {}]",
                                        ctx.channel().isActive(),
                                        ctx.channel().isWritable());
                                log.error("channel is not writable");
                            }
                            /**
                             * 1. 通道关闭后，对于 心跳包 将不可用
                             * 2. 由于客户端 使用了 ChannelProvider 来 缓存 channel，这里关闭后，无法 发挥 channel 缓存的作用
                             */
                            // future.addListener(ChannelFutureListener.CLOSE);
                        } finally {
                            ReferenceCountUtil.release(msg);
                        }
                    }
                });
    }
}
