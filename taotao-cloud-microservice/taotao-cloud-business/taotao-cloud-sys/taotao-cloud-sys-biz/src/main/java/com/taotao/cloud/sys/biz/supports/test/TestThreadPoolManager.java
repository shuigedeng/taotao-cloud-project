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

package com.taotao.cloud.sys.biz.supports.test;

import com.taotao.boot.common.utils.log.LogUtils;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * TestThreadPoolManager
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Component
public class TestThreadPoolManager implements BeanFactoryAware {

    // 用于从IOC里取对象
    private BeanFactory factory; // 如果实现Runnable的类是通过spring的application.xml文件进行注入,可通过

    // 线程池维护线程的最少数量
    private static final int CORE_POOL_SIZE = 2;
    // 线程池维护线程的最大数量
    private static final int MAX_POOL_SIZE = 10;
    // 线程池维护线程所允许的空闲时间
    private static final int KEEP_ALIVE_TIME = 0;
    // 线程池所使用的缓冲队列大小
    private static final int WORK_QUEUE_SIZE = 50;

    @Override
    public void setBeanFactory( BeanFactory beanFactory ) throws BeansException {
        factory = beanFactory;
    }

    /**
     * 用于储存在队列中的订单,防止重复提交,在真实场景中，可用redis代替 验证重复
     */
    Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    /**
     * 订单的缓冲队列,当线程池满了，则将订单存入到此缓冲队列
     */
    Queue<Object> msgQueue = new LinkedBlockingQueue<Object>();

    /**
     * 当线程池的容量满了，执行下面代码，将订单存入到缓冲队列
     */
    final RejectedExecutionHandler handler = ( r, executor ) -> {
        // 订单加入到缓冲队列
        msgQueue.offer(( (BusinessThread) r ).getAcceptStr());
        LogUtils.info(
                "系统任务太忙了,把此订单交给(调度线程池)逐一处理，订单号：" + ( (BusinessThread) r ).getAcceptStr());
    };

    /**
     * 创建线程池
     */
    final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(WORK_QUEUE_SIZE),
            this.handler);

    /**
     * 将任务加入订单线程池
     */
    public void addOrders( String orderId ) {
        LogUtils.info("此订单准备添加到线程池，订单号：" + orderId);
        // 验证当前进入的订单是否已经存在
        if (cacheMap.get(orderId) == null) {
            cacheMap.put(orderId, new Object());
            BusinessThread businessThread = new BusinessThread(orderId);
            threadPool.execute(businessThread);
        }
    }

    /**
     * 线程池的定时任务----> 称为(调度线程池)。此线程池支持 定时以及周期性执行任务的需求。
     */
    final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(5);

    /**
     * 检查(调度线程池)，每秒执行一次，查看订单的缓冲队列是否有 订单记录，则重新加入到线程池
     */
    final ScheduledFuture scheduledFuture = scheduler.scheduleAtFixedRate(
            () -> {
                // 判断缓冲队列是否存在记录
                if (!msgQueue.isEmpty()) {
                    // 当线程池的队列容量少于WORK_QUEUE_SIZE，则开始把缓冲队列的订单 加入到 线程池
                    if (threadPool.getQueue().size() < WORK_QUEUE_SIZE) {
                        String orderId = (String) msgQueue.poll();
                        BusinessThread businessThread = new BusinessThread(orderId);
                        threadPool.execute(businessThread);
                        LogUtils.info("(调度线程池)缓冲队列出现订单业务，重新添加到线程池，订单号：" + orderId);
                    }
                }
            },
            0,
            1,
            TimeUnit.SECONDS);

    /**
     * 获取消息缓冲队列
     */
    public Queue<Object> getMsgQueue() {
        return msgQueue;
    }

    /**
     * 终止订单线程池+调度线程池
     */
    public void shutdown() {
        // true表示如果定时任务在执行，立即中止，false则等待任务结束后再停止
        LogUtils.info("终止订单线程池+调度线程池：" + scheduledFuture.cancel(false));
        scheduler.shutdown();
        threadPool.shutdown();
    }
}
