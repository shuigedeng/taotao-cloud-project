package com.taotao.cloud.job.server.jobserver.remote.worker.selector.impl;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RANDOM
 *
 * @since 2024/2/24
 */
@Component
public class RandomTaskTrackerSelector implements TaskTrackerSelector {

    @Override
    public DispatchStrategy strategy() {
        return DispatchStrategy.RANDOM;
    }

    @Override
    public WorkerInfo select(JobInfo jobInfoDO, InstanceInfo instanceInfoDO, List<WorkerInfo> availableWorkers) {
        int randomIdx = ThreadLocalRandom.current().nextInt(availableWorkers.size());
        return availableWorkers.get(randomIdx);
    }
}
