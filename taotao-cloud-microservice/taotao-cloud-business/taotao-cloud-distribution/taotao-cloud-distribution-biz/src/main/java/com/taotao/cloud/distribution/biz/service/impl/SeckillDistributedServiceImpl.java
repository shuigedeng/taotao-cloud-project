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

package com.taotao.cloud.distribution.biz.service.impl;

import com.taotao.cloud.distribution.biz.common.dynamicquery.DynamicQuery;
import com.taotao.cloud.distribution.biz.common.entity.Result;
import com.taotao.cloud.distribution.biz.common.entity.SuccessKilled;
import com.taotao.cloud.distribution.biz.common.exception.RrException;
import com.taotao.cloud.distribution.biz.distributedlock.redis.RedissLockUtil;
import com.taotao.cloud.distribution.biz.distributedlock.zookeeper.ZkLockUtil;
import com.taotao.cloud.distribution.biz.service.SeckillDistributedService;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillDistributedServiceImpl implements SeckillDistributedService {

    @Autowired
    private DynamicQuery dynamicQuery;

    @Override
    @Transactional
    public Result startSeckilRedisLock(long seckillId, long userId) {
        boolean res = false;
        try {
            /**
             * 尝试获取锁，最多等待3秒，上锁以后20秒自动解锁（实际项目中推荐这种，以防出现死锁）、这里根据预估秒杀人数，设定自动释放锁时间.
             * 看过博客的朋友可能会知道(Lcok锁与事物冲突的问题)：https://blog.52itstyle.com/archives/2952/
             * 分布式锁的使用和Lock锁的实现方式是一样的，但是测试了多次分布式锁就是没有问题，当时就留了个坑
             * 闲来咨询了《静儿1986》，推荐下博客：https://www.cnblogs.com/xiexj/p/9119017.html
             * 先说明下之前的配置情况：Mysql在本地，而Redis是在外网。 回复是这样的：
             * 这是因为分布式锁的开销是很大的。要和锁的服务器进行通信，它虽然是先发起了锁释放命令，涉及网络IO，延时肯定会远远大于方法结束后的事务提交。
             * ==========================================================================================
             * 分布式锁内部都是Runtime.exe命令调用外部，肯定是异步的。分布式锁的释放只是发了一个锁释放命令就算完活了。真正其作用的是下次获取锁的时候，要确保上次是释放了的。
             * 就是说获取锁的时候耗时比较长，那时候事务肯定提交了就是说获取锁的时候耗时比较长，那时候事务肯定提交了。
             * ==========================================================================================
             * 周末测试了一下，把redis配置在了本地，果然出现了超卖的情况；或者还是使用外网并发数增加在10000+也是会有问题的，之前自己没有细测，我的锅。
             * 所以这钟实现也是错误的，事物和锁会有冲突，建议AOP实现。
             */
            res = RedissLockUtil.tryLock(seckillId + "", TimeUnit.SECONDS, 3, 20);
            if (res) {
                String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
                Object object = dynamicQuery.nativeQueryObject(nativeSql, new Object[] {seckillId});
                Long number = ((Number) object).longValue();
                if (number > 0) {
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState((short) 0);
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    dynamicQuery.save(killed);
                    nativeSql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=? AND number>0";
                    dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[] {seckillId});
                } else {
                    return Result.error(SeckillStatEnum.END);
                }
            } else {
                return Result.error(SeckillStatEnum.MUCH);
            }
        } catch (Exception e) {
            throw new RrException("异常了个乖乖");
        } finally {
            if (res) { // 释放锁
                RedissLockUtil.unlock(seckillId + "");
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Override
    @Transactional
    public Result startSeckilZksLock(long seckillId, long userId) {
        boolean res = false;
        try {
            // 基于redis分布式锁 基本就是上面这个解释 但是 使用zk分布式锁 使用本地zk服务 并发到10000+还是没有问题，谁的锅？
            res = ZkLockUtil.acquire(3, TimeUnit.SECONDS);
            if (res) {
                String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
                Object object = dynamicQuery.nativeQueryObject(nativeSql, new Object[] {seckillId});
                Long number = ((Number) object).longValue();
                if (number > 0) {
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState((short) 0);
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    dynamicQuery.save(killed);
                    nativeSql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=? AND number>0";
                    dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[] {seckillId});
                } else {
                    return Result.error(SeckillStatEnum.END);
                }
            } else {
                return Result.error(SeckillStatEnum.MUCH);
            }
        } catch (Exception e) {
            throw new RrException("异常了个乖乖");
        } finally {
            if (res) { // 释放锁
                ZkLockUtil.release();
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Override
    @Transactional
    public Result startSeckilLock(long seckillId, long userId, long number) {
        boolean res = false;
        try {
            // 尝试获取锁，最多等待3秒，上锁以后10秒自动解锁（实际项目中推荐这种，以防出现死锁）
            res = RedissLockUtil.tryLock(seckillId + "", TimeUnit.SECONDS, 3, 10);
            if (res) {
                String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
                Object object = dynamicQuery.nativeQueryObject(nativeSql, new Object[] {seckillId});
                Long count = ((Number) object).longValue();
                if (count >= number) {
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState((short) 0);
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    dynamicQuery.save(killed);
                    nativeSql = "UPDATE seckill  SET number=number-? WHERE seckill_id=? AND number>0";
                    dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[] {number, seckillId});
                } else {
                    return Result.error(SeckillStatEnum.END);
                }
            } else {
                return Result.error(SeckillStatEnum.MUCH);
            }
        } catch (Exception e) {
            throw new RrException("异常了个乖乖");
        } finally {
            if (res) { // 释放锁
                RedissLockUtil.unlock(seckillId + "");
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }
}
