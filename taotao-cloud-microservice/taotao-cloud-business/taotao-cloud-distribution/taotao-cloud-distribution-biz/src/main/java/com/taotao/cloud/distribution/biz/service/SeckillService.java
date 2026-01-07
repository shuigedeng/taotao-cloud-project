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

import java.util.List;

public interface SeckillService {

    /**
     * 查询全部的秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);
    /**
     * 查询秒杀售卖商品
     *
     * @param seckillId
     * @return
     */
    Long getSeckillCount(long seckillId);
    /**
     * 删除秒杀售卖商品记录
     *
     * @param seckillId
     * @return
     */
    void deleteSeckill(long seckillId);

    /**
     * 秒杀 一、会出现数量错误
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckil(long seckillId, long userId);

    /**
     * 秒杀 二、程序锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilLock(long seckillId, long userId);
    /**
     * 秒杀 二、程序锁AOP
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilAopLock(long seckillId, long userId);

    /**
     * 秒杀 二、数据库悲观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilDBPCC_ONE(long seckillId, long userId);
    /**
     * 秒杀 三、数据库悲观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilDBPCC_TWO(long seckillId, long userId);
    /**
     * 秒杀 三、数据库乐观锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilDBOCC(long seckillId, long userId, long number);

    /**
     * 秒杀 四、事物模板
     *
     * @param seckillId
     * @param userId
     * @return
     */
    Result startSeckilTemplate(long seckillId, long userId, long number);
}
