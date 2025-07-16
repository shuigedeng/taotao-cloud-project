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

package com.taotao.cloud.job.common.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * 分段锁
 *
 * @author shuigedeng
 * @since 2020/6/3
 */
@Slf4j
public class SegmentLock {

    private final int mask;
    private final Lock[] locks;

    public SegmentLock(int concurrency) {
        int size = CommonUtils.formatSize(concurrency);
        mask = size - 1;
        locks = new Lock[size];
        for (int i = 0; i < size; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    /**
     * 阻塞获取锁，可被打断
     * @param lockId 锁ID
     * @throws InterruptedException 线程被中断异常
     */
    public void lockInterruptible(int lockId) throws InterruptedException {
        Lock lock = locks[lockId & mask];
        lock.lockInterruptibly();
    }

    public void lockInterruptibleSafe(int lockId) {
        try {
            lockInterruptible(lockId);
        } catch (InterruptedException ignore) {
        }
    }

    /**
     * 释放锁
     * @param lockId 锁ID
     */
    public void unlock(int lockId) {
        Lock lock = locks[lockId & mask];
        lock.unlock();
    }
}
