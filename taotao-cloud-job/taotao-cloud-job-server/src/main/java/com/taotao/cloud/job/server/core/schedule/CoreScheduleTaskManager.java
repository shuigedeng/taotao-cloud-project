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

package com.taotao.cloud.job.server.core.schedule;

import com.taotao.cloud.job.common.enums.TimeExpressionType;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CoreScheduleTaskManager
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Service
@Slf4j
public class CoreScheduleTaskManager implements InitializingBean, DisposableBean {

    private final List<Thread> coreThreadContainer = new ArrayList<>();
    @Autowired
    TtcJobScheduleService ttcJobScheduleService;

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Override
    public void afterPropertiesSet() {
        // 定时调度
        coreThreadContainer.add(
                new Thread(
                        new LoopRunnable(
                                "ScheduleCronJob",
                                TtcJobScheduleService.SCHEDULE_RATE,
                                () ->
                                        ttcJobScheduleService.scheduleNormalJob(
                                                TimeExpressionType.CRON)),
                        "Thread-ScheduleCronJob"));
        coreThreadContainer.add(
                new Thread(
                        new LoopRunnable(
                                "ScheduleDailyTimeIntervalJob",
                                TtcJobScheduleService.SCHEDULE_RATE,
                                () ->
                                        ttcJobScheduleService.scheduleNormalJob(
                                                TimeExpressionType.DAILY_TIME_INTERVAL)),
                        "Thread-ScheduleDailyTimeIntervalJob"));
        coreThreadContainer.forEach(Thread::start);
    }

    @Override
    public void destroy() throws Exception {
        coreThreadContainer.forEach(Thread::interrupt);
    }

    @RequiredArgsConstructor
    private static final class LoopRunnable implements Runnable {

        private final String taskName;

        private final Long runningInterval;

        private final Runnable innerRunnable;

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            log.info("start task : {}.", taskName);
            while (true) {
                try {

                    Thread.sleep(runningInterval);

                    innerRunnable.run();
                } catch (InterruptedException e) {
                    log.warn("[{}] task has been interrupted!", taskName, e);
                    break;
                } catch (Exception e) {
                    log.error("[{}] task failed!", taskName, e);
                }
            }
        }
    }
}
