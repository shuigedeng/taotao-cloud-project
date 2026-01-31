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

package com.taotao.cloud.rpc.common.idworker.utils;

import com.alibaba.nacos.common.utils.StringUtils;
import com.taotao.cloud.rpc.common.util.PropertiesConstants;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

/**
 * LRedisHelper
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class LRedisHelper {

    private static Object lock = new Object();
    private static final String workerIds = "worker-ids";
    private static final String workerIdsSet = "worker-ids-set";
    private static final String retryReqIds = "retry-req-ids";
    private static StatefulRedisConnection<String, byte[]> strToByteConn;
    private static StatefulRedisConnection<String, String> strToStrConn;
    private static RedisAsyncCommands<String, byte[]> strToByteAsyncCommand;
    private static RedisAsyncCommands<String, String> strToStrAsyncCommand;
    private static RedisCommands<String, byte[]> strToByteSyncCommand;
    private static RedisCommands<String, String> strToStrSyncCommand;
    private static final String DEFAULT_ADDRESS = "127.0.0.1:6379";

    // 2. 加载配置文件，只需加载一次
    static {
        log.info("The lettuce thread pool is begin prepare");
        RedisURI.Builder uriBuilder = RedisURI.builder();
        // .withHost("47.107.63.171")
        // .withPort(6379)
        // .withAuthentication("default", "yupengRedis")
        // .build();

        // RedisClient redisClient = RedisClient.create(redisUri);   // <2> 建立客户端
        // StatefulRedisConnection<String, String> connection = redisClient.connect();     // <3>
        // 建立线程安全的链接
        // 2.1 创建Properties对象
        Properties p = new Properties();
        // 2.2 调用p对象中的load方法进行配置文件的加载
        // 使用InPutStream流读取properties文件
        String currentWorkPath = System.getProperty("user.dir");
        InputStream is = null;
        PropertyResourceBundle configResource = null;
        String redisAuth = "";
        String redisPwd = "";
        String redisAddr = "";
        try (BufferedReader bufferedReader =
                new BufferedReader(
                        new FileReader(currentWorkPath + "/config/resource.properties"));) {

            configResource = new PropertyResourceBundle(bufferedReader);
            redisAuth = configResource.getString(PropertiesConstants.REDIS_SERVER_AUTH);

            if ("false".equals(redisAuth)
                    || "default".equals(redisAuth)
                    || StringUtils.isBlank(redisAuth)) {
                log.info("--- no redis auth ---");
                try {
                    redisAddr = configResource.getString(PropertiesConstants.REDIS_SERVER_ADDR);
                    String[] hostAndPort = redisAddr.split(":");
                    uriBuilder.withHost(hostAndPort[0]).withPort(Integer.parseInt(hostAndPort[1]));
                } catch (MissingResourceException redisServerAddressException) {
                    String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                    uriBuilder.withHost(hostAndPort[0]).withPort(Integer.parseInt(hostAndPort[1]));
                    log.warn("redis server address attribute is missing");
                    log.info("use default redis server address : " + DEFAULT_ADDRESS);
                }
            } else if ("true".equals(redisAuth)) {
                log.info("redis auth attribute is true and start with auth");
                try {
                    redisAddr = configResource.getString(PropertiesConstants.REDIS_SERVER_ADDR);
                } catch (MissingResourceException redisServerAddressException) {
                    log.info(
                            "redis server address property attribute is missing: {}",
                            redisServerAddressException.getMessage());
                    log.info("use default redis server address : " + DEFAULT_ADDRESS);
                    String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                    uriBuilder.withHost(hostAndPort[0]).withPort(Integer.parseInt(hostAndPort[1]));
                }
                try {
                    redisPwd = configResource.getString(PropertiesConstants.REDIS_SERVER_PWD);
                    String[] hostAndPort = redisAddr.split(":");
                    uriBuilder
                            .withHost(hostAndPort[0])
                            .withPort(Integer.parseInt(hostAndPort[1]))
                            .withAuthentication("default", redisPwd);
                } catch (MissingResourceException redisPasswordException) {
                    log.error("redis password attribute is missing: ", redisPasswordException);
                    throw new RuntimeException("redis password attribute is missing!");
                }
            } else {
                throw new RuntimeException("redis auth attribute is illegal!");
            }
            log.info(
                    "read resource from resource path: {}",
                    currentWorkPath + "/config/resource.properties");
        } catch (MissingResourceException redisAuthException) {
            log.warn("redis auth attribute is missing and start with no auth");
            try {
                String redisAddress =
                        configResource.getString(PropertiesConstants.REDIS_SERVER_ADDR);
                redisAddr = StringUtils.isBlank(redisAddress) ? DEFAULT_ADDRESS : redisAddress;
                String[] hostAndPort = redisAddr.split(":");
                uriBuilder.withHost(hostAndPort[0]).withPort(Integer.parseInt(hostAndPort[1]));
            } catch (MissingResourceException redisServerAddressException) {
                String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                uriBuilder.withHost(hostAndPort[0]).withPort(Integer.parseInt(hostAndPort[1]));
                log.warn("redis server address attribute is missing");
                log.info("use default redis server address : " + DEFAULT_ADDRESS);
            }

        } catch (IOException ioException) {
            log.info(
                    "not found resource from resource path: {}",
                    currentWorkPath + "/config/resource.properties");
            try {
                ResourceBundle resource = ResourceBundle.getBundle("resource");

                try {
                    redisAuth = resource.getString(PropertiesConstants.REDIS_SERVER_AUTH);

                    if ("false".equals(redisAuth)
                            || "default".equals(redisAuth)
                            || StringUtils.isBlank(redisAuth)) {
                        log.info("--- no redis auth ---");
                        try {
                            redisAddr = resource.getString(PropertiesConstants.REDIS_SERVER_ADDR);
                            String[] hostAndPort = redisAddr.split(":");
                            uriBuilder
                                    .withHost(hostAndPort[0])
                                    .withPort(Integer.parseInt(hostAndPort[1]));
                        } catch (MissingResourceException redisServerAddressException) {
                            String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                            uriBuilder
                                    .withHost(hostAndPort[0])
                                    .withPort(Integer.parseInt(hostAndPort[1]));
                            log.warn("redis server address attribute is missing");
                            log.info("use default redis server address : " + DEFAULT_ADDRESS);
                        }
                    } else if ("true".equals(redisAuth)) {
                        log.info("redis auth attribute is true and start with auth");
                        try {
                            redisAddr = resource.getString(PropertiesConstants.REDIS_SERVER_ADDR);
                        } catch (MissingResourceException redisServerAddressException) {
                            log.info(
                                    "redis server address property attribute is missing: {}",
                                    redisServerAddressException.getMessage());
                            log.info("use default redis server address : " + DEFAULT_ADDRESS);
                            String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                            uriBuilder
                                    .withHost(hostAndPort[0])
                                    .withPort(Integer.parseInt(hostAndPort[1]));
                        }
                        try {
                            redisPwd = resource.getString(PropertiesConstants.REDIS_SERVER_PWD);
                            String[] hostAndPort = redisAddr.split(":");
                            uriBuilder
                                    .withHost(hostAndPort[0])
                                    .withPort(Integer.parseInt(hostAndPort[1]))
                                    .withAuthentication("default", redisPwd);
                        } catch (MissingResourceException redisPasswordException) {
                            log.error(
                                    "redis password attribute is missing: ",
                                    redisPasswordException);
                            throw new RuntimeException("redis password attribute is missing!");
                        }
                    } else {
                        throw new RuntimeException("redis auth attribute is illegal!");
                    }
                } catch (MissingResourceException clusterUseException) {
                    log.info("redis auth attribute is missing and start with no auth");
                    redisAddr = resource.getString(PropertiesConstants.REDIS_SERVER_ADDR);
                    String[] hostAndPort = redisAddr.split(":");
                    uriBuilder.withHost(hostAndPort[0]).withPort(Integer.parseInt(hostAndPort[1]));
                }

            } catch (MissingResourceException resourceException) {
                log.info("not found resource from resource path: {}", "resource.properties");
                log.info("Connect to default address {}", DEFAULT_ADDRESS);
                String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                uriBuilder.withHost(hostAndPort[0]).withPort(Integer.parseInt(hostAndPort[1]));
            }
            log.info("read resource from resource path: {}", "resource.properties");
        }

        RedisURI uri = uriBuilder.build();

        ClientResources resources =
                DefaultClientResources.builder()
                        .ioThreadPoolSize(4) // 设置I/O线程池大小（默认cpu数）仅在没有提供eventLoopGroupProvider时有效
                        .computationThreadPoolSize(
                                4) // 设置用于计算的任务线程数（默认cpu数）仅在没有提供eventExecutorGroup时有效
                        //                .reconnectDelay(Delay.constant(Duration.ofSeconds(10)))
                        //	//设置无状态尝试重连延迟，默认延迟上限30s
                        .build();
        RedisClient client = RedisClient.create(resources, uri);
        ClientOptions options =
                ClientOptions.builder()
                        .autoReconnect(true) // 设置自动重连
                        .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(5))
                                .build()) // 为客户端创建的连接设置默认超时时间，适用于尝试连接和非阻塞命令
                        .pingBeforeActivateConnection(true) // 激活连接前执行PING命令
                        //				.timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(5)))	//命令超时
                        .build();
        client.setOptions(options);
        strToByteConn = client.connect(RedisCodec.of(new StringCodec(), new ByteArrayCodec()));
        strToStrConn = client.connect();
        strToByteAsyncCommand = strToByteConn.async();
        strToStrAsyncCommand = strToStrConn.async();
        strToByteSyncCommand = strToByteConn.sync();
        strToStrSyncCommand = strToStrConn.sync();
        log.info("The lettuce thread pool is ready");
    }

    public static void preLoad() {
        log.trace("trigger preload");
    }

    public static Long exists( String key ) {
        return strToStrSyncCommand.exists(key);
    }

    public static void syncSet( String key, String value ) {
        log.debug("syncSet key[{}] - value[{}]", key, value);
        strToStrSyncCommand.set(key, value);
    }

    public static void asyncSet( String key, String value ) {
        log.debug("asyncSet key[{}] - value[{}]", key, value);
        strToStrAsyncCommand.set(key, value);
    }

    public static String get( String key ) {
        String value = strToStrSyncCommand.get(key);
        log.debug("get key[{}] - value[{}]", key, value);
        return value;
    }

    public static boolean existsWorkerIdSet( long workerId ) {
        return strToStrSyncCommand.sismember(workerIdsSet, String.valueOf(workerId));
    }

    public static void syncSetWorkerIdSet( String workerId ) {
        strToStrSyncCommand.sadd(workerIdsSet, workerId);
    }

    public static void asyncSetWorkerIdSet( long workerId ) {
        log.debug("asyncSetWorkerIdSet set[{}] - value[{}]", workerIdsSet, workerId);
        strToStrAsyncCommand.sadd(workerIdsSet, String.valueOf(workerId));
    }

    public static Long existsWorkerId( String hostName ) {
        return strToStrSyncCommand.exists(workerIds + ":" + hostName);
    }

    public static String getForHostName( String hostName ) {
        String value = strToStrSyncCommand.get(workerIds + ":" + hostName);
        log.debug("getForHostName key[{}] - value[{}]", hostName, value);
        return value;
    }

    public static void asyncSetWorkerId( String hostName, long workerId ) {
        log.debug("asyncSetWorkerId key[{}] - value[{}]", hostName, workerId);
        strToStrAsyncCommand.set(workerIds + ":" + hostName, String.valueOf(workerId));
    }

    public static void syncSetWorkerId( String hostName, long workerId ) {
        log.debug("syncSetWorkerId key[{}] - value[{}]", hostName, workerId);
        strToStrSyncCommand.set(workerIds + ":" + hostName, String.valueOf(workerId));
    }

    public static Long existsRetryResult( String retryRequestId ) {
        return strToStrSyncCommand.exists(retryReqIds + ":" + retryRequestId);
    }

    public static byte[] getForRetryRequestId( String retryRequestId ) {
        byte[] value = strToByteSyncCommand.get(retryReqIds + ":" + retryRequestId);
        log.debug("getForRetryRequestId key[{}] - value[{}]", retryRequestId, value);
        return value;
    }

    public static void asyncSetRetryRequestResult( String retryRequestId, byte[] result ) {
        log.debug("asyncSetRetryRequestResult key[{}] - value[{}]", retryRequestId, result);
        strToByteAsyncCommand.set(
                retryReqIds + ":" + retryRequestId, result, new SetArgs().nx().ex(60));
    }

    public static void syncSetRetryRequestResult( String retryRequestId, byte[] result ) {
        log.debug("syncSetRetryRequestResult key[{}] - value[{}]", retryRequestId, result);
        strToByteSyncCommand.set(
                retryReqIds + ":" + retryRequestId, result, new SetArgs().nx().ex(60));
    }

    public static void main( String[] args ) {
    }
}
