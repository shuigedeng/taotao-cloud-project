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

package com.taotao.cloud.shortlink.biz.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.log.api.api.dto.ShortLinkDTO;
import com.taotao.cloud.shortlink.biz.web.biz.ShortLinkBiz;
import com.taotao.cloud.shortlink.biz.web.service.IShortLinkService;
import com.taotao.cloud.shortlink.biz.web.web.request.ShortLinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短链 - 简单缓存方案 - service
 *
 * @since 2022/05/06
 */
@Slf4j
@Service
public class ShortLinkServiceSimpleImpl implements IShortLinkService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ShortLinkBiz shortLinkBiz;

    private static final String CACHE_KEY_SHORT_CODE = "SHORT_LINK_MAPPING:%s";

    private static final String LOCK_KEY_SHORT_CODE = "SHORT_LINK_LOCK:%s";

    private final long SHORT_LINK_TIME = 1000 * 60 * 60 * 24;

    private static final String CACHE_EMPTY = "null";

    public Optional<ShortLinkDTO> getShortLinkFromCache(String shortLinkCode) {
        RBucket<String> cacheBucket = redissonClient.getBucket(String.format(CACHE_KEY_SHORT_CODE, shortLinkCode));

        String cacheJson = cacheBucket.get();
        if (StringUtils.isNoneBlank(cacheJson) || !CACHE_EMPTY.equals(cacheJson)) {
            ShortLinkDTO shortLinkDTO = JSONObject.parseObject(cacheJson, ShortLinkDTO.class);
            if (shortLinkBiz.checkShortLinkCodeValid(shortLinkDTO)) {
                // TIPS 缓存自动延期
                cacheBucket.expire(Duration.ofMillis(getShortLinkCodeCacheTime()));
                return Optional.ofNullable(shortLinkDTO);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> parseShortLinkCode(String shortLinkCode) {
        RBucket<String> codeBucket = redissonClient.getBucket(String.format(CACHE_KEY_SHORT_CODE, shortLinkCode));

        String cacheJson = codeBucket.get();
        if (StringUtils.isNoneBlank(cacheJson) || !CACHE_EMPTY.equals(cacheJson)) {
            ShortLinkDTO shortLinkDTO = JSONObject.parseObject(cacheJson, ShortLinkDTO.class);
            if (shortLinkBiz.checkShortLinkCodeValid(shortLinkDTO)) {
                // TIPS 缓存自动延期
                codeBucket.expire(Duration.ofMillis(getShortLinkCodeCacheTime()));
                return Optional.ofNullable(shortLinkDTO.getOriginUrl());
            }
        }

        // 使用下面加锁版本，避免数据不一致
        //        Optional<ShortLinkDTO> shortLinkCodeOpt =
        // shortLinkBiz.getShortLinkCodeDto(shortLinkCode);
        //        if (!shortLinkCodeOpt.isPresent()) {
        //            return Optional.empty();
        //        }
        //        ShortLinkDTO shortLinkDTO = shortLinkCodeOpt.get();
        //        codeBucket.set(JSONObject.toJSONString(shortLinkDTO), getShortLinkCodeCacheTime(),
        // TimeUnit.MILLISECONDS);
        //        return Optional.ofNullable(shortLinkDTO.getOriginUrl());

        // TIPS: 双写一致性的解决：引入分布式锁，to see ShortLinkServiceSimpleImpl.updateShortLinkCode
        // TIPS: 极端情况：突发流量请求的是冷门数据，导致缓存击穿，大量请求都囤积在这里，发生严重锁竞争,并发变成串行(load db + write cache)，即使有double
        // check
        // TIPS: 部分拿到锁后，会走缓存，但串行不太好。优化：分布式锁超时串行转并发机制
        RLock lock = redissonClient.getLock(String.format(LOCK_KEY_SHORT_CODE, shortLinkCode));
        try {
            if (lock.tryLock(1000L, TimeUnit.MILLISECONDS)) {
                // TIPS: double check: 避免积压在锁上的请求，一个个进来后都得查库
                cacheJson = codeBucket.get();
                if (StringUtils.isNoneBlank(cacheJson) || !CACHE_EMPTY.equals(cacheJson)) {
                    ShortLinkDTO shortLinkDTO = JSONObject.parseObject(cacheJson, ShortLinkDTO.class);
                    if (shortLinkBiz.checkShortLinkCodeValid(shortLinkDTO)) {
                        codeBucket.expire(Duration.ofMillis(getShortLinkCodeCacheTime()));
                        log.info("解析短链：double check缓存，拿到缓存数据，code -> {}", shortLinkDTO.getCode());
                        return Optional.ofNullable(shortLinkDTO.getOriginUrl());
                    }
                }

                Optional<ShortLinkDTO> shortLinkCodeOpt = shortLinkBiz.getShortLinkCodeDto(shortLinkCode);
                if (!shortLinkCodeOpt.isPresent()) {
                    // TODO 缓存穿透的解决：缓存空值，新增该短码时，要不空数据去掉或用实际数据替换
                    // fixme 消费失败，却被误以为是拿不到值，缓存了空值，导致数据不一致
                    codeBucket.set(CACHE_EMPTY, getShortLinkCodeCacheTime(), TimeUnit.MILLISECONDS);
                    return Optional.empty();
                }

                ShortLinkDTO shortLinkDTO = shortLinkCodeOpt.get();
                codeBucket.set(
                        JSONObject.toJSONString(shortLinkDTO), getShortLinkCodeCacheTime(), TimeUnit.MILLISECONDS);
                return Optional.ofNullable(shortLinkDTO.getOriginUrl());
            }

            // 串行转并发机制： 在特定时间内没有拿到锁，此时从缓存中读，因为在锁竞争期间，陆陆续续有一些请求会进入到同步块内（load db + write cache）
            cacheJson = codeBucket.get();
            if (StringUtils.isNoneBlank(cacheJson) || !CACHE_EMPTY.equals(cacheJson)) {
                ShortLinkDTO shortLinkDTO = JSONObject.parseObject(cacheJson, ShortLinkDTO.class);
                if (shortLinkBiz.checkShortLinkCodeValid(shortLinkDTO)) {
                    codeBucket.expire(Duration.ofMillis(getShortLinkCodeCacheTime()));
                    log.info("解析短链：double check缓存，拿到缓存数据，code -> {}", shortLinkDTO.getCode());
                    return Optional.ofNullable(shortLinkDTO.getOriginUrl());
                }
            }
        } catch (InterruptedException e) {
            log.warn("parseShortLinkCode: 解析短链异常，e -> {}", e.toString());
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

        return Optional.empty();
    }

    // 只更新长链，其它字段更新暂时不实现
    @Override
    public Boolean updateShortLinkCode(ShortLinkUpdateRequest request) {
        // TIPS: 双写一致性解决：更新数据库，再写缓存，极端情况下，更新数据库后写入缓存，而其它请求将旧数据再写入缓存，导致数据不一致，加分布式锁解决
        RLock lock = redissonClient.getLock(String.format(LOCK_KEY_SHORT_CODE, request.getCode()));
        if (lock.tryLock()) {
            try {
                Boolean result = shortLinkBiz.updateShortLinkCode(ShortLinkDTO.builder()
                        .id(request.getId())
                        .code(request.getCode())
                        .originUrl(request.getOriginUrl())
                        .build());
                if (Boolean.FALSE.equals(result)) {
                    return Boolean.FALSE;
                }
                // 查库
                Optional<ShortLinkDTO> shortLinkCodeOpt = shortLinkBiz.getShortLinkCodeDto(request.getCode());
                if (!shortLinkCodeOpt.isPresent()) {
                    return Boolean.FALSE;
                }
                // 写缓存
                ShortLinkDTO shortLinkDTO = shortLinkCodeOpt.get();
                redissonClient
                        .getBucket(String.format(CACHE_KEY_SHORT_CODE, request.getCode()))
                        .set(JSONObject.toJSONString(shortLinkDTO), getShortLinkCodeCacheTime(), TimeUnit.MILLISECONDS);

                return Boolean.TRUE;
            } catch (Exception e) {
                log.warn("updateShortLinkCode: 更新短链数据异常,e -> {}", e.toString());
            } finally {
                lock.unlock();
            }
        }

        return Boolean.FALSE;
    }

    private long getShortLinkCodeCacheTime() {
        // 3000ms内的随机时间
        return SHORT_LINK_TIME + new Random().nextInt(3000);
    }
}
