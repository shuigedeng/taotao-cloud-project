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

package com.taotao.cloud.distribution.biz.service;

public interface SeckillDistributedService {

    /**
     * 秒杀 一 单个商品
     *
     * @param seckillId 秒杀商品ID
     * @param userId 用户ID
     * @return
     */
    Result startSeckilRedisLock(long seckillId, long userId);
    /**
     * 秒杀 一 单个商品
     *
     * @param seckillId 秒杀商品ID
     * @param userId 用户ID
     * @return
     */
    Result startSeckilZksLock(long seckillId, long userId);

    /**
     * 秒杀 二 多个商品
     *
     * @param seckillId 秒杀商品ID
     * @param userId 用户ID
     * @param number 秒杀商品数量
     * @return
     */
    Result startSeckilLock(long seckillId, long userId, long number);
}
