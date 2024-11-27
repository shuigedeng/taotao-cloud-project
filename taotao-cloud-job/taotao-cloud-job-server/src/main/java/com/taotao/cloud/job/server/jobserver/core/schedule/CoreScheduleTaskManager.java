package com.taotao.cloud.job.server.jobserver.core.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.enums.TimeExpressionType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CoreScheduleTaskManager implements InitializingBean, DisposableBean {
    private final List<Thread> coreThreadContainer = new ArrayList<>();
    @Autowired
    TtcJobScheduleService kJobScheduleService;

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Override
    public void afterPropertiesSet() {
        // 定时调度
        coreThreadContainer.add(new Thread(new LoopRunnable("ScheduleCronJob", TtcJobScheduleService.SCHEDULE_RATE, () -> kJobScheduleService.scheduleNormalJob(TimeExpressionType.CRON)), "Thread-ScheduleCronJob"));
        coreThreadContainer.add(new Thread(new LoopRunnable("ScheduleDailyTimeIntervalJob", TtcJobScheduleService.SCHEDULE_RATE, () -> kJobScheduleService.scheduleNormalJob(TimeExpressionType.DAILY_TIME_INTERVAL)), "Thread-ScheduleDailyTimeIntervalJob"));
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
