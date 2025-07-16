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

package com.taotao.cloud.rpc.common.idworker;

import com.alibaba.nacos.common.utils.StringUtils;
import com.taotao.cloud.rpc.common.idworker.exception.WorkerIdCantApplyException;
import com.taotao.cloud.rpc.common.idworker.utils.JRedisHelper;
import com.taotao.cloud.rpc.common.idworker.utils.LRedisHelper;
import com.taotao.cloud.rpc.common.util.IpUtils;
import com.taotao.cloud.rpc.common.util.PropertiesConstants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkerIdServer {

    private static long workerId = 0;

    private static String redisClientWay = "";

    static {
        config();
        init();
    }

    private static void config() {
        // 使用InPutStream流读取properties文件
        String currentWorkPath = System.getProperty("user.dir");
        InputStream is = null;
        PropertyResourceBundle configResource = null;
        try (BufferedReader bufferedReader =
                new BufferedReader(
                        new FileReader(currentWorkPath + "/config/resource.properties")); ) {

            configResource = new PropertyResourceBundle(bufferedReader);
            redisClientWay = configResource.getString(PropertiesConstants.REDIS_CLIENT_WAY);

            if ("jedis".equals(redisClientWay)
                    || "default".equals(redisClientWay)
                    || StringUtils.isBlank(redisClientWay)) {
                log.info("find redis client way attribute is jedis");
            } else if ("lettuce".equals(redisClientWay)) {
                log.info("find redis client way attribute is lettuce");
            } else {
                throw new RuntimeException("redis client way attribute is illegal!");
            }

        } catch (MissingResourceException redisClientWayException) {
            log.warn("redis client way attribute is missing");
            log.info("use default redis client default way: jedis");
            redisClientWay = "jedis";
        } catch (IOException ioException) {
            log.info(
                    "not found resource from resource path: {}",
                    currentWorkPath + "/config/resource.properties");
            try {
                ResourceBundle resource = ResourceBundle.getBundle("resource");
                redisClientWay = resource.getString(PropertiesConstants.REDIS_CLIENT_WAY);

                if ("jedis".equals(redisClientWay)
                        || "default".equals(redisClientWay)
                        || StringUtils.isBlank(redisClientWay)) {
                    log.info("find redis client way attribute is jedis");
                } else if ("lettuce".equals(redisClientWay)) {
                    log.info("find redis client way attribute is lettuce");
                } else {
                    throw new RuntimeException("redis client way attribute is illegal!");
                }

            } catch (MissingResourceException resourceException) {
                log.info("not found resource from resource path: {}", "resource.properties");
                log.info("use default redis client way: jedis");
                redisClientWay = "jedis";
            }
            log.info("read resource from resource path: {}", "resource.properties");
        }
    }

    /**
     * 初始化 机器 Id
     */
    private static void init() {
        if (workerId == 0) {
            // 初始化为1
            workerId = 1;
            // 得到服务器机器名称
            String hostName = IpUtils.getPubIpAddr();
            if ("jedis".equals(redisClientWay)
                    || "default".equals(redisClientWay)
                    || StringUtils.isBlank(redisClientWay)) {
                if (JRedisHelper.existsWorkerId(hostName)) {
                    // 如果redis中存在该服务器名称，则直接取得workerId
                    workerId = Long.parseLong(JRedisHelper.getForHostName(hostName));
                } else {
                    /**
                     * 采用 HashMap 哈希命中的 算法
                     * 对 hash 值为负数取正
                     */
                    int h =
                            hostName.hashCode()
                                    & 0x7fffffff; // = 0b0111 1111 1111 1111 1111 1111 1111 1111 =
                    // Integer.MAX_VALUE
                    h = h ^ h >>> 16;
                    int id = h % 1024;

                    workerId = id;
                    if (!JRedisHelper.existsWorkerId(hostName)) {
                        long oldWorkerId = workerId;
                        while (JRedisHelper.existsWorkerIdSet(workerId)) {
                            workerId = (workerId + 1L) % 1024;
                            if (workerId == oldWorkerId) {
                                log.error(
                                        "machine code node cannot be applied, nodes number has reached its maximum value");
                                throw new WorkerIdCantApplyException(
                                        String.format(
                                                "Machine code node cannot be applied, Nodes number has reached its maximum value"));
                            }
                        }
                        JRedisHelper.setWorkerId(hostName, workerId);
                        JRedisHelper.setWorkerIdSet(workerId);
                    }
                }
            } else {
                if (LRedisHelper.existsWorkerId(hostName) != 0L) {
                    // 如果redis中存在该服务器名称，则直接取得workerId
                    workerId = Long.parseLong(LRedisHelper.getForHostName(hostName));
                } else {
                    /**
                     * 采用 HashMap 哈希命中的 算法
                     * 对 hash 值为负数取正
                     */
                    int h =
                            hostName.hashCode()
                                    & 0x7fffffff; // = 0b0111 1111 1111 1111 1111 1111 1111 1111 =
                    // Integer.MAX_VALUE
                    h = h ^ h >>> 16;
                    int id = h % 1024;

                    workerId = id;
                    if (LRedisHelper.exists(hostName) == 0L) {
                        long oldWorkerId = workerId;
                        while (LRedisHelper.existsWorkerIdSet(workerId)) {
                            workerId = (workerId + 1L) % 1024;
                            if (workerId == oldWorkerId) {
                                log.error(
                                        "machine code node cannot be applied, nodes number has reached its maximum value");
                                throw new WorkerIdCantApplyException(
                                        String.format(
                                                "Machine code node cannot be applied, Nodes number has reached its maximum value"));
                            }
                        }
                        LRedisHelper.asyncSetWorkerId(hostName, workerId);
                        LRedisHelper.asyncSetWorkerIdSet(workerId);
                    }
                }
            }
        }
    }

    /**
     * 获取 机器 id
     *
     * @param serverCode
     * @return
     */
    public static long getWorkerId(int serverCode) {

        switch (serverCode) {
            case 0:
                {
                    if ("jedis".equals(redisClientWay)
                            || "default".equals(redisClientWay)
                            || StringUtils.isBlank(redisClientWay)) {
                        return Long.parseLong(JRedisHelper.getForHostName(IpUtils.getPubIpAddr()));
                    } else {
                        return Long.parseLong(LRedisHelper.getForHostName(IpUtils.getPubIpAddr()));
                    }
                }
            case 1:
            case 2:
            case 3:
            default:
                {
                    if ("jedis".equals(redisClientWay)
                            || "default".equals(redisClientWay)
                            || StringUtils.isBlank(redisClientWay)) {
                        return Long.parseLong(JRedisHelper.getForHostName(IpUtils.getPubIpAddr()));
                    } else {
                        return Long.parseLong(LRedisHelper.getForHostName(IpUtils.getPubIpAddr()));
                    }
                }
        }
    }
}
