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
import com.itstyle.distribution.common.utils.BigDecimalUtil;
import com.itstyle.distribution.queue.delay.jvm.RedPacketMessage;
import com.itstyle.distribution.queue.delay.jvm.RedPacketQueue;
import com.itstyle.distribution.service.IRedPacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 高并发抢红包案例 https://blog.52itstyle.vip */
@Api(tags = "抢红包")
@RestController
@RequestMapping("/redPacket")
public class RedPacketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketController.class);

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    /** 创建线程池 调整队列数 拒绝服务 */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IRedPacketService redPacketService;

    /**
     * 抢红包 拆红包 抢到基本能拆到 建议使用抢红包二的方式
     *
     * @param redPacketId
     * @return
     */
    @ApiOperation(value = "抢红包一", nickname = "爪哇笔记")
    @PostMapping("/start")
    public Result start(long redPacketId) {
        int skillNum = 100;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        /** 初始化红包数据，抢红包拦截 */
        redisUtil.cacheValue(redPacketId + "-num", 10);
        /** 初始化剩余人数，拆红包拦截 */
        redisUtil.cacheValue(redPacketId + "-restPeople", 10);
        /** 初始化红包金额，单位为分 */
        redisUtil.cacheValue(redPacketId + "-money", 20000);
        /** 模拟100个用户抢10个红包 */
        for (int i = 1; i <= skillNum; i++) {
            int userId = i;
            Runnable task = () -> {
                /** 抢红包拦截，其实应该分两步，为了演示方便 */
                long count = redisUtil.decr(redPacketId + "-num", 1);
                if (count >= 0) {
                    Result result = redPacketService.startSeckil(redPacketId, userId);
                    BigDecimal amount = BigDecimalUtil.divide(
                            BigDecimal.parseBigDecimal(result.get("msg").toString()), (BigDecimal) 100);
                    LOGGER.info("用户{}抢红包成功，金额：{}", userId, amount);
                } else {
                    LOGGER.info("用户{}抢红包失败", userId);
                }
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();
            Integer restMoney =
                    Integer.parseInt(redisUtil.getValue(redPacketId + "-money").toString());
            LOGGER.info("剩余金额：{}", restMoney);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }

    /**
     * 抢红包 拆红包 抢到不一定能拆到
     *
     * @param redPacketId
     * @return
     */
    @ApiOperation(value = "抢红包二", nickname = "爪哇笔记")
    @PostMapping("/startTwo")
    public Result startTwo(long redPacketId) {
        int skillNum = 100;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        /** 初始化红包数据，抢红包拦截 */
        redisUtil.cacheValue(redPacketId + "-num", 10);
        /** 初始化红包金额，单位为分 */
        redisUtil.cacheValue(redPacketId + "-money", 20000);
        /** 模拟100个用户抢10个红包 */
        for (int i = 1; i <= skillNum; i++) {
            int userId = i;
            Runnable task = () -> {
                /** 抢红包 判断剩余金额 */
                Integer money = (Integer) redisUtil.getValue(redPacketId + "-money");
                if (money > 0) {
                    /** 虽然能抢到 但是不一定能拆到 类似于微信的 点击红包显示抢的按钮 */
                    Result result = redPacketService.startTwoSeckil(redPacketId, userId);
                    if (result.get("code").toString().equals("500")) {
                        LOGGER.info("用户{}手慢了，红包派完了", userId);
                    } else {
                        BigDecimal amount = BigDecimalUtil.divide(
                                BigDecimal.parseBigDecimal(result.get("msg").toString()), (BigDecimal) 100);
                        LOGGER.info("用户{}抢红包成功，金额：{}", userId, amount);
                    }
                } else {
                    /** 直接显示手慢了，红包派完了 */
                    // LOGGER.info("用户{}手慢了，红包派完了",userId);
                }
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();
            Integer restMoney =
                    Integer.parseInt(redisUtil.getValue(redPacketId + "-money").toString());
            LOGGER.info("剩余金额：{}", restMoney);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }

    /**
     * 有人没抢 红包发多了 红包进入延迟队列 实现过期失效
     *
     * @param redPacketId
     * @return
     */
    @ApiOperation(value = "抢红包三", nickname = "爪哇笔记")
    @PostMapping("/startThree")
    public Result startThree(long redPacketId) {
        int skillNum = 9;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        /** 初始化红包数据，抢红包拦截 */
        redisUtil.cacheValue(redPacketId + "-num", 10);
        /** 初始化红包金额，单位为分 */
        redisUtil.cacheValue(redPacketId + "-money", 20000);
        /** 加入延迟队列 24s秒过期 */
        RedPacketMessage message = new RedPacketMessage(redPacketId, 24);
        RedPacketQueue.getQueue().produce(message);
        /** 模拟 9个用户抢10个红包 */
        for (int i = 1; i <= skillNum; i++) {
            int userId = i;
            Runnable task = () -> {
                /** 抢红包 判断剩余金额 */
                Integer money = (Integer) redisUtil.getValue(redPacketId + "-money");
                if (money > 0) {
                    Result result = redPacketService.startTwoSeckil(redPacketId, userId);
                    if (result.get("code").toString().equals("500")) {
                        LOGGER.info("用户{}手慢了，红包派完了", userId);
                    } else {
                        BigDecimal amount = BigDecimalUtil.divide(
                                BigDecimal.parseBigDecimal(result.get("msg").toString()), (BigDecimal) 100);
                        LOGGER.info("用户{}抢红包成功，金额：{}", userId, amount);
                    }
                }
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();
            Integer restMoney =
                    Integer.parseInt(redisUtil.getValue(redPacketId + "-money").toString());
            LOGGER.info("剩余金额：{}", restMoney);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }
        return Result.ok();
    }
}
