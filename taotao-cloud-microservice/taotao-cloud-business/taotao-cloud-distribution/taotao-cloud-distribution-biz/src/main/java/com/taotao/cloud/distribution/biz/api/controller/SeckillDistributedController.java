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

package com.taotao.cloud.distribution.biz.api.controller;

import com.itstyle.distribution.common.entity.Result;
import com.itstyle.distribution.common.redis.RedisUtil;
import com.itstyle.distribution.queue.activemq.ActiveMQSender;
import com.itstyle.distribution.queue.kafka.KafkaSender;
import com.itstyle.distribution.queue.redis.RedisSender;
import com.itstyle.distribution.service.ISeckillDistributedService;
import com.itstyle.distribution.service.ISeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "分布式秒杀")
@RestController
@RequestMapping("/seckillDistributed")
public class SeckillDistributedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillDistributedController.class);

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    // 调整队列数 拒绝服务
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10000));

    @Autowired
    private ISeckillService seckillService;

    @Autowired
    private ISeckillDistributedService seckillDistributedService;

    @Autowired
    private RedisSender redisSender;

    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    private ActiveMQSender activeMQSender;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "秒杀一(Rediss分布式锁)", nickname = "科帮网")
    @PostMapping("/startRedisLock")
    public Result startRedisLock(long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀一");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                Result result = seckillDistributedService.startSeckilRedisLock(killId, userId);
                LOGGER.info("用户:{}{}", userId, result.get("msg"));
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(15000);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀二(zookeeper分布式锁)", nickname = "科帮网")
    @PostMapping("/startZkLock")
    public Result startZkLock(long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀二");
        for (int i = 0; i < 10000; i++) {
            final long userId = i;
            Runnable task = () -> {
                Result result = seckillDistributedService.startSeckilZksLock(killId, userId);
                LOGGER.info("用户:{}{}", userId, result.get("msg"));
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀三(Redis分布式队列-订阅监听)", nickname = "科帮网")
    @PostMapping("/startRedisQueue")
    public Result startRedisQueue(long seckillId) {
        redisUtil.cacheValue(seckillId + "", null); // 秒杀结束
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀三");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                if (redisUtil.getValue(killId + "") == null) {
                    // 思考如何返回给用户信息ws
                    redisSender.sendChannelMess("seckill", killId + ";" + userId);
                } else {
                    // 秒杀结束
                }
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            redisUtil.cacheValue(killId + "", null);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀四(Kafka分布式队列)", nickname = "科帮网")
    @PostMapping("/startKafkaQueue")
    public Result startKafkaQueue(long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀四");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                if (redisUtil.getValue(killId + "") == null) {
                    // 思考如何返回给用户信息ws
                    kafkaSender.sendChannelMess("seckill", killId + ";" + userId);
                } else {
                    // 秒杀结束
                }
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            redisUtil.cacheValue(killId + "", null);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀五(ActiveMQ分布式队列)", nickname = "科帮网")
    @PostMapping("/startActiveMQQueue")
    public Result startActiveMQQueue(long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀五");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                if (redisUtil.getValue(killId + "") == null) {
                    Destination destination = new ActiveMQQueue("seckill.queue");
                    // 思考如何返回给用户信息ws
                    activeMQSender.sendChannelMess(destination, killId + ";" + userId);
                } else {
                    // 秒杀结束
                }
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            redisUtil.cacheValue(killId + "", null);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀六(Redis原子递减)", nickname = "爪哇笔记")
    @PostMapping("/startRedisCount")
    public Result startRedisCount(long secKillId) {
        /** 还原数据 */
        seckillService.deleteSeckill(secKillId);
        int count = 1000;
        /** 初始化商品个数 */
        redisUtil.cacheValue(secKillId + "-num", 100);
        final long killId = secKillId;
        LOGGER.info("开始秒杀六");
        for (int i = 0; i < count; i++) {
            final long userId = i;
            Runnable task = () -> {
                /** 原子递减 */
                long number = redisUtil.decr(secKillId + "-num", 1);
                if (number >= 0) {
                    seckillService.startSeckilAopLock(secKillId, userId);
                    LOGGER.info("用户:{}秒杀商品成功", userId);
                } else {
                    LOGGER.info("用户:{}秒杀商品失败", userId);
                }
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            redisUtil.cacheValue(killId + "", null);
            Long secKillCount = seckillService.getSeckillCount(secKillId);
            LOGGER.info("一共秒杀出{}件商品", secKillCount);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }
}
