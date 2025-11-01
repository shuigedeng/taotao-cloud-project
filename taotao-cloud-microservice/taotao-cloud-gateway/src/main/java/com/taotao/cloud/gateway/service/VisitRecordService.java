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

package com.taotao.cloud.gateway.service;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.taotao.boot.common.utils.servlet.RequestUtils;
import com.taotao.cloud.gateway.model.AccessRecord;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

/**
 * 访问服务
 */
@Slf4j
@Service
public class VisitRecordService {

    private final VisitLogService visitLogService;

    // 自定义的一个标识
    private final String attributeKey = "visitRecord";

    public VisitRecordService(VisitLogService visitLogService) {
        this.visitLogService = visitLogService;

        this.shutdownHook();
    }

    private void shutdownHook() {
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    this.batchSave();
                                    threadPool.shutdown();
                                }));
    }

    @Nullable
    public AccessRecord get(ServerWebExchange exchange) {
        return exchange.getAttribute(attributeKey);
    }

    /**
     * 从 ServerWebExchange 中获取当前访问信息
     * <p>
     * 如果没有，则构建一个新的对象存入exchange中
     *
     * @param exchange gateway访问合同
     */
    public AccessRecord getOrBuild(ServerWebExchange exchange) {
        AccessRecord visitRecord = get(exchange);
        if (visitRecord == null) {
            visitRecord = build(exchange);
            put(exchange, visitRecord);
        }
        return visitRecord;
    }

    /**
     * 构建一个 VisitRecord 实体类，但仅适用于获取 request 信息
     *
     * @param exchange gateway访问
     * @return 访问信息
     */
    public AccessRecord build(ServerWebExchange exchange) {
        // 获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        String ip = RequestUtils.getServerHttpRequestIpAddress(request);
        // 请求路径
        String path = request.getPath().pathWithinApplication().value();
        // 请求schema: http/https
        String scheme = request.getURI().getScheme();
        // 请求方法
        HttpMethod method = request.getMethod();
        // 路由服务地址
        URI targetUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        // 请求头
        HttpHeaders headers = request.getHeaders();
        // 获取请求地址
        InetSocketAddress remoteAddress = request.getRemoteAddress();

        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setPath(path);
        accessRecord.setScheme(scheme);
        accessRecord.setTargetUri(targetUri);
        accessRecord.setIp(ip);
        accessRecord.setCreateTime(LocalDateTime.now());
        return accessRecord;
    }

    /**
     * 将访问信息存入 ServerWebExchange 当中，将会与当前请求关联起来， 以便于后续在任何地方均可获得
     *
     * @param exchange    gateway访问合同
     * @param visitRecord 访问信息
     */
    public void put(ServerWebExchange exchange, AccessRecord visitRecord) {
        Map<String, Object> attributes = exchange.getAttributes();
        attributes.put(attributeKey, visitRecord);
    }

    /**
     * 保存访问记录
     *
     * @param exchange      gateway访问
     * @param consumingTime 访问耗时
     */
    public void add(ServerWebExchange exchange, Long consumingTime) {
        // 获取数据
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        // 获取保存的日志记录体
        AccessRecord visitRecord = getOrBuild(exchange);
        // 设置访问时间 单位毫秒
        visitRecord.setConsumingTime(consumingTime);

        // 设置访问状态
        if (response.getStatusCode() != null) {
            visitRecord.setStatus(response.getStatusCode().value());
        }
        // 设置访问的用户ID 我这里是保存在请求头中
        String userId = request.getHeaders().getFirst("userId");
        if (StringUtils.isNotEmpty(userId)) {
            visitRecord.setUserId(Long.parseLong(userId));
        }
        // 打印访问情况
        log.info(visitRecord.toString());
        // 添加记录到缓存中
        visitCache.add(visitRecord);
        // 执行任务，保存数据
        doTask();
    }

    /**
     * 缓存，在插入数据库前先存入此。 为防止数据被重复插入，故使用Set，但不能确保100%不会被重复存储。
     */
    private HashSet<AccessRecord> visitCache = new HashSet<>();

    /**
     * 信号量，用于标记当前是否有任务正在执行，{@code true}表示当前无任务进行。
     */
    private volatile boolean taskFinish = true;

    private final ThreadFactory namedThreadFactory =
            new ThreadFactoryBuilder().setNamePrefix("visit-record-").build();

    private final ThreadPoolExecutor threadPool =
            new ThreadPoolExecutor(
                    1,
                    3,
                    15,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10),
                    namedThreadFactory,
                    new ThreadPoolExecutor.AbortPolicy());

    private void doTask() {
        if (taskFinish) {
            // 当前没有任务的情况下，加锁并执行任务
            synchronized (this) {
                if (taskFinish) {
                    taskFinish = false;
                    threadPool.execute(
                            () -> {
                                try {
                                    // 当数据量较小时，则等待一段时间再插入数据，从而做到将数据尽可能的批量插入数据库
                                    if (visitCache.size() <= BATCH_SIZE) {
                                        Thread.sleep(500);
                                    }
                                    // 批量保存
                                    batchSave();
                                } catch (InterruptedException e) {
                                    log.error("睡眠时发生了异常: {}", e.getMessage());
                                } finally {
                                    // 任务执行完毕后修改标志位
                                    taskFinish = true;
                                }
                            });
                }
            }
        }
    }

    /**
     * 单次批量插入的数据量
     */
    private final int BATCH_SIZE = 500;

    /**
     * 缩减因子，每次更新缓存Set时缩小的倍数，对应HashSet的扩容倍数
     */
    private final float REDUCE_FACTOR = 0.5f;

    private void batchSave() {
        log.debug("访问记录准备插入数据库，当前数据量：{}", visitCache.size());
        if (visitCache.isEmpty()) {
            return;
        }
        // 构造新对象来存储数据，旧对象保存到数据库后不再使用
        HashSet<AccessRecord> oldCache = visitCache;
        visitCache = new HashSet<>((int) (oldCache.size() * REDUCE_FACTOR));
        boolean isSave = false;
        try {
            // 批量保存
            isSave = visitLogService.saveBatch(oldCache, BATCH_SIZE);
        } finally {
            if (!isSave) {
                // 如果插入失败，则重新添加所有数据
                visitCache.addAll(oldCache);
            }
        }
    }
}
