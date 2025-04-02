package com.taotao.cloud.job.server.remote.worker.selector.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HealthFirst
 *
 * @author （疑似）新冠帕鲁
 * @since 2024/2/24
 */
@Component
public class HealthFirstTaskTrackerSelector implements TaskTrackerSelector {

    @Override
    public DispatchStrategy strategy() {
        return DispatchStrategy.HEALTH_FIRST;
    }

    @Override
    public WorkerInfo select(JobInfo jobInfoDO, InstanceInfo instanceInfoDO, List<WorkerInfo> availableWorkers) {
        List<WorkerInfo> workers = Lists.newArrayList(availableWorkers);
        workers.sort((o1, o2) -> o2.getSystemMetrics().calculateScore() - o1.getSystemMetrics().calculateScore());
        return workers.get(0);
    }
}
