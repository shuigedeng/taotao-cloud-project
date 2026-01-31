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

import com.taotao.cloud.rpc.common.factory.ThreadPoolFactory;
import com.taotao.cloud.rpc.common.idworker.exception.InvalidSystemClockException;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IdWorker
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class IdWorker {

    /**
     * 服务器运行时 开始时间戳
     */
    protected long epoch = 1288834974657L;
    /**
     * 机器 id 所占位数
     */
    protected long workerIdBits = 10L;

    /**
     * 最大机器 id 结果为 1023
     */
    protected long maxWorkerId = -1L ^ ( -1L << workerIdBits );

    /**
     * 并发序列在 id 中占的位数
     */
    protected long sequenceBits = 12L;

    /**
     * 机器 id 掩码
     */
    protected long workerIdShift = sequenceBits;

    /**
     * 时间戳 掩码
     */
    protected long timestampLeftShift = sequenceBits + workerIdBits;

    /**
     * 并发序列掩码 二进制表示为 12 位 1 (ob111111111111=0xfff=4095) 也表示序列号最大数
     */
    protected long sequenceMask = -1L ^ ( -1L << sequenceBits );

    /**
     * 上一次系统时间 时间戳 用于 判断系统 是否发生 时钟回拨 异常
     */
    protected long lastMillis = -1L;

    /**
     * 工作机器 ID (0-1023)
     */
    protected final long workerId;

    /**
     * 并发冲突序列 (0-4095) 即毫秒内并发量
     */
    protected long sequence = 0L;

    /**
     * 最近时间阈值
     */
    protected long thresholdMills = 500L;

    /**
     * 最近时间缓存时间戳大小阈值
     */
    protected long thresholdSize = 3000;

    /**
     * 时间戳 - 序列号 id
     */
    private ConcurrentHashMap<Long, Long> timeStampMaxSequenceMap = new ConcurrentHashMap<>();

    /**
     * 自定义 最近时间保存 时间戳 清理启用的线程池
     */
    private ExecutorService timeStampClearExecutorService =
            ThreadPoolFactory.createDefaultThreadPool("timestamp-clear-pool");

    protected Logger logger = LoggerFactory.getLogger(IdWorker.class);

    public IdWorker( long workerId ) {
        this.workerId = checkWorkerId(workerId);

        logger.debug(
                "worker starting. timestamp left shift {}, worker id {}",
                timestampLeftShift,
                workerId);
    }

    public long getEpoch() {
        return epoch;
    }

    private long checkWorkerId( long workerId ) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            int rand = new SecureRandom().nextInt((int) maxWorkerId + 1);
            logger.warn(
                    "worker Id can't be greater than {} or less than 0, use a random {}",
                    maxWorkerId,
                    rand);
            return rand;
        }

        return workerId;
    }

    public synchronized long nextId() {
        long timestamp = millisGen();
        // 解决时钟回拨问题（低并发场景）
        if (timestamp < lastMillis) {
            // 可以去 获取 之前该 时间戳 最大的序列号
            // 并发还没有到最大值，可以对序列号递增
            /**
             * 当前回拨后 没有之前 时间戳生成 id，且可阻塞时间不超过 500 ms
             * 1、保证最坏情况 不阻塞超过 500ms
             * 2、否则 抛出异常，不再阻塞，因为超时该时间大概率会导致超时
             */
            Long clockBackMaxSequence;
            long diff = lastMillis - timestamp;

            if (diff > 1000L) {
                logger.warn("clock is moving backwards more then 1000ms");
                logger.error(
                        "clock is moving backwards.  Rejecting requests until {}.", lastMillis);
                throw new InvalidSystemClockException(
                        String.format(
                                "Clock moved backwards.  Refusing to generate id for {} milliseconds",
                                lastMillis - timestamp));
            } else if (diff > 500L) {
                logger.warn("clock is moving backwards more than 500ms");
                long blockMillis = lastMillis - 500L;
                // 阻塞到 500 ms 内，因为 500ms内 保留能获取到最大序列号的 时间戳
                timestamp = tilNextMillis(blockMillis);
                clockBackMaxSequence = timeStampMaxSequenceMap.get(timestamp);

                if (clockBackMaxSequence != null && clockBackMaxSequence < sequenceMask) {
                    sequence = clockBackMaxSequence + 1;
                    // 继续维护 该时间戳的最大 序列号
                    timeStampMaxSequenceMap.put(timestamp, sequence);
                } else {
                    logger.error(
                            "clock is moving backwards.  Rejecting requests until {}.", lastMillis);
                    throw new InvalidSystemClockException(
                            String.format(
                                    "Clock moved backwards.  Refusing to generate id for {} milliseconds",
                                    lastMillis - timestamp));
                }
                // 恢复 发生时钟回拨的 状态
            } else {
                logger.info("clock is moving backwards less than 500ms");
                clockBackMaxSequence = timeStampMaxSequenceMap.get(timestamp);

                if (clockBackMaxSequence != null && clockBackMaxSequence < sequenceMask) {
                    sequence = clockBackMaxSequence + 1;
                    // 继续维护 该时间戳的最大 序列号
                    timeStampMaxSequenceMap.put(timestamp, sequence);
                    // 时间回拨 小于 500ms，恢复回拨前时间戳 + 1ms，序列号 id
                } else {
                    timestamp = tilNextMillis(lastMillis);
                    sequence = 0;
                }
            }
        }
        // 毫秒并发
        if (lastMillis == timestamp) {
            sequence = ( sequence + 1 ) & sequenceMask;
            // 序列号已经最大了，需要阻塞新的时间戳
            // 表示这一毫秒并发量已达上限，新的请求会阻塞到新的时间戳中去
            if (sequence == 0) {
                timestamp = tilNextMillis(lastMillis);
            }
        } else {
            sequence = 0;
        }

        /**
         * 由于 时间戳 跟 序列号 都是递增，所以序列号总会保持最大值
         * reason：清理工作应该 由后台来完成，不与 业务线程 串行 影响 业务执行效率
         */
        timeStampMaxSequenceMap.put(timestamp, sequence);
        // 当并发严重致保存时间戳超过阈值时，考虑垃圾清理
        if (timeStampMaxSequenceMap.size() > thresholdSize) {
            timeStampClearExecutorService.execute(
                    () -> {
                        // 遍历清除 timeStamp 比 最近时间阈值 更前的时间戳
                        timeStampMaxSequenceMap.keySet().stream()
                                .forEach(
                                        entryTimeStamp -> {
                                            if (lastMillis - entryTimeStamp > thresholdMills) {
                                                timeStampMaxSequenceMap.remove(entryTimeStamp);
                                            }
                                        });
                    });
        }
        if (timestamp > lastMillis) {
            // 保存 上一次时间戳
            lastMillis = timestamp;
        }

        long diff = timestamp - getEpoch();
        return ( diff << timestampLeftShift ) | ( workerId << workerIdShift ) | sequence;
    }

    /**
     * 阻塞生成下一个更大的时间戳
     */
    protected long tilNextMillis( long lastMillis ) {
        long millis = millisGen();
        while (millis <= lastMillis) {
            millis = millisGen();
        }

        return millis;
    }

    protected long millisGen() {
        return System.currentTimeMillis();
    }

    public long getLastMillis() {
        return lastMillis;
    }

    public long getWorkerId() {
        return workerId;
    }
}
