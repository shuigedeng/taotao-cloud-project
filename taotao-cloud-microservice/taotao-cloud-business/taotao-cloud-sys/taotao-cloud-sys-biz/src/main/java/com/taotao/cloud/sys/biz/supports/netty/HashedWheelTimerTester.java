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

package com.taotao.cloud.sys.biz.supports.netty;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * HashedWheelTimerTester
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class HashedWheelTimerTester {

    @SneakyThrows
    public static void simpleHashedWheelTimer() {
        log.info("init task 1...");

        HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 8);

        // add a new timeout
        timer.newTimeout(
                timeout -> {
                    log.info("running task 1...");
                },
                5,
                TimeUnit.SECONDS);
    }

    @SneakyThrows
    public static void reScheduleHashedWheelTimer() {
        log.info("init task 2...");

        HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 8);

        Thread.sleep(5000);

        // add a new timeout
        Timeout tm = timer.newTimeout(
                timeout -> {
                    log.info("running task 2...");
                },
                5,
                TimeUnit.SECONDS);

        // cancel
        if (!tm.isExpired()) {
            log.info("cancel task 2...");
            tm.cancel();
        }

        // reschedule
        timer.newTimeout(tm.task(), 3, TimeUnit.SECONDS);
    }

    public static void main( String[] args ) {
        simpleHashedWheelTimer();

        reScheduleHashedWheelTimer();
    }
}
