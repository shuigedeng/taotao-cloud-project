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

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("redPacketService")
public class RedPacketService implements com.taotao.cloud.distribution.biz.service.RedPacketService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DynamicQuery dynamicQuery;

    @Override
    public RedPacket get(long redPacketId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startSeckil(long redPacketId, int userId) {
        Integer money = 0;
        boolean res = false;
        try {
            /** 获取锁 */
            res = RedissLockUtil.tryLock(redPacketId + "", TimeUnit.SECONDS, 3, 10);
            if (res) {
                long restPeople = redisUtil.decr(redPacketId + "-restPeople", 1);
                /** 如果是最后一人 */
                if (restPeople == 0) {
                    money = Integer.parseInt(
                            redisUtil.getValue(redPacketId + "-money").toString());
                } else {
                    Integer restMoney = Integer.parseInt(
                            redisUtil.getValue(redPacketId + "-money").toString());
                    Random random = new Random();
                    // 随机范围：[1,剩余人均金额的两倍]
                    money = random.nextInt((int) (restMoney / (restPeople + 1) * 2 - 1)) + 1;
                }
                redisUtil.decr(redPacketId + "-money", money);
                /** 异步入库 */
                RedPacketRecord record = new RedPacketRecord();
                record.setMoney(money);
                record.setRedPacketId(redPacketId);
                record.setUid(userId);
                record.setCreateTime(new Timestamp(System.currentTimeMillis()));
                saveRecord(record);
                /** 异步入账 */
            } else {
                /** 获取锁失败相当于抢红包失败，红包个数加一 */
                redisUtil.incr(redPacketId + "-num", 1);
            }
        } catch (Exception e) {
            LogUtils.error(e);
        } finally {
            // 释放锁
            if (res) {
                RedissLockUtil.unlock(redPacketId + "");
            }
        }
        return Result.ok(money);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startTwoSeckil(long redPacketId, int userId) {
        Integer money = 0;
        boolean res = false;
        try {
            /** 获取锁 保证红包数量和计算红包金额的原子性操作 */
            res = RedissLockUtil.tryLock(redPacketId + "", TimeUnit.SECONDS, 3, 10);
            if (res) {
                long restPeople = redisUtil.decr(redPacketId + "-num", 1);
                if (restPeople < 0) {
                    return Result.error("手慢了，红包派完了");
                } else {
                    /** 如果是最后一人 */
                    if (restPeople == 0) {
                        money = Integer.parseInt(
                                redisUtil.getValue(redPacketId + "-money").toString());
                    } else {
                        Integer restMoney = Integer.parseInt(
                                redisUtil.getValue(redPacketId + "-money").toString());
                        Random random = new Random();
                        // 随机范围：[1,剩余人均金额的两倍]
                        money = random.nextInt((int) (restMoney / (restPeople + 1) * 2 - 1)) + 1;
                    }
                    redisUtil.decr(redPacketId + "-money", money);
                    /** 异步入库 */
                    RedPacketRecord record = new RedPacketRecord();
                    record.setMoney(money);
                    record.setRedPacketId(redPacketId);
                    record.setUid(userId);
                    record.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    saveRecord(record);
                    /** 异步入账 */
                }
            } else {
                /** 获取锁失败相当于抢红包失败 */
                return Result.error("手慢了，红包派完了");
            }
        } catch (Exception e) {
            LogUtils.error(e);
        } finally {
            // 释放锁
            if (res) {
                RedissLockUtil.unlock(redPacketId + "");
            }
        }
        return Result.ok(money);
    }

    @Async
    void saveRecord(RedPacketRecord record) {
        dynamicQuery.save(record);
    }
}
