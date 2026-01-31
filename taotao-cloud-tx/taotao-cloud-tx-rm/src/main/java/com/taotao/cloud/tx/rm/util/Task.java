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

package com.taotao.cloud.tx.rm.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 子事务的等待队列：基于此实现事务控制权
/**
 * Task
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class Task {

    // 通过ReentrantLock的Condition条件等待队列实现线程阻塞/唤醒
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    // 阻塞挂起线程的方法
    public void waitTask() {
        System.out.println("事务控制权已经被拦截挂起........");
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 唤醒放下线程的方法
    public void signalTask() {
        System.out.println("事务控制权已经被拦截放下........");
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
