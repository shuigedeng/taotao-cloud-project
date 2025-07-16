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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * Redis分布式锁 api
 */
@Slf4j
public class JRedisHelper {

    private static Object lock = new Object();
    private static final String workerIds = "worker-ids";
    private static final String workerIdsSet = "worker-ids-set";
    private static final String retryReqIds = "retry-req-ids";
    private static Jedis jedis;
    private static final String DEFAULT_ADDRESS = "127.0.0.1:6379";

    // 2. 加载配置文件，只需加载一次
    static {
        // 使用InPutStream流读取properties文件
        String currentWorkPath = System.getProperty("user.dir");
        InputStream is = null;
        PropertyResourceBundle configResource = null;
        String redisAuth = "";
        String redisPwd = "";
        String redisAddr = "";
        try (BufferedReader bufferedReader =
                new BufferedReader(
                        new FileReader(currentWorkPath + "/config/resource.properties")); ) {

            configResource = new PropertyResourceBundle(bufferedReader);
            redisAuth = configResource.getString(PropertiesConstants.REDIS_SERVER_AUTH);

            if ("false".equals(redisAuth)
                    || "default".equals(redisAuth)
                    || StringUtils.isBlank(redisAuth)) {
                log.info("--- no redis auth ---");
                try {
                    redisAddr = configResource.getString(PropertiesConstants.REDIS_SERVER_ADDR);
                    String[] hostAndPort = redisAddr.split(":");
                    jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                } catch (MissingResourceException redisServerAddressException) {
                    String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                    jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
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
                    jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                }
                try {
                    redisPwd = configResource.getString(PropertiesConstants.REDIS_SERVER_PWD);
                    String[] hostAndPort = redisAddr.split(":");
                    jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                    jedis.auth(redisPwd);
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
                jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            } catch (MissingResourceException redisServerAddressException) {
                String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
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
                            jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                        } catch (MissingResourceException redisServerAddressException) {
                            String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                            jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
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
                            jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                        }
                        try {
                            redisPwd = resource.getString(PropertiesConstants.REDIS_SERVER_PWD);
                            String[] hostAndPort = redisAddr.split(":");
                            jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                            jedis.auth(redisPwd);
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
                    jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                }

            } catch (MissingResourceException resourceException) {
                log.info("not found resource from resource path: {}", "resource.properties");
                log.info("Connect to default address {}", DEFAULT_ADDRESS);
                String[] hostAndPort = DEFAULT_ADDRESS.split(":");
                jedis = new Jedis(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            }
            log.info("read resource from resource path: {}", "resource.properties");
        }
    }

    public static boolean exists(String key) {
        return jedis.exists(key);
    }

    public static void set(String key, String value) {
        jedis.set(key, value);
    }

    public static String get(String key) {
        return jedis.get(key);
    }

    public static boolean existsWorkerId(String hostName) {
        synchronized (lock) {
            return jedis.exists(workerIds + ":" + hostName);
        }
    }

    public static String getForHostName(String hostName) {
        synchronized (lock) {
            String value = jedis.get(workerIds + ":" + hostName);
            log.debug("getForHostName key[{}] - value[{}]", hostName, value);
            return value;
        }
    }

    public static void setWorkerId(String hostName, long workerId) {
        synchronized (lock) {
            log.debug("setWorkerId key[{}] - value[{}]", hostName, workerId);
            jedis.set(workerIds + ":" + hostName, String.valueOf(workerId));
        }
    }

    public static void remWorkerId(String hostName) {
        synchronized (lock) {
            log.debug("remWorkerId key[{}]", hostName);
            String workerId = getForHostName(hostName);
            jedis.del(workerIds + ":" + hostName);
            if (workerId != null) {
                jedis.srem(workerIdsSet, workerId);
            }
        }
    }

    public static boolean existsWorkerIdSet(long workerId) {
        return jedis.sismember(workerIdsSet, String.valueOf(workerId));
    }

    public static void setWorkerIdSet(long workerId) {
        log.debug("setWorkerIdSet set[{}] - value[{}]", workerIdsSet, workerId);
        jedis.sadd(workerIdsSet, String.valueOf(workerId));
    }

    public static boolean existsRetryResult(String retryRequestId) {
        synchronized (lock) {
            return jedis.exists(retryReqIds + ":" + retryRequestId);
        }
    }

    public static String getForRetryRequestId(String retryRequestId) {
        synchronized (lock) {
            String value = jedis.get(retryReqIds + ":" + retryRequestId);
            log.debug("getForRetryRequestId key[{}] - value[{}]", retryRequestId, value);
            return value;
        }
    }

    public static void setRetryRequestResult(String retryRequestId, String result) {
        synchronized (lock) {
            log.debug("setRetryRequestResult key[{}]- value[{}]", retryRequestId, result);
            jedis.set(
                    retryReqIds + ":" + retryRequestId, result, SetParams.setParams().ex(60).nx());
        }
    }

    public static void main(String[] args) {}
}
