package com.taotao.cloud.job.server.remote.worker.selector.impl;

import com.taotao.cloud.job.common.enums.DispatchStrategy;
import com.taotao.cloud.job.server.common.module.WorkerInfo;
import com.taotao.cloud.job.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import com.taotao.cloud.job.server.remote.worker.selector.TaskTrackerSelector;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RANDOM
 *
 * @author （疑似）新冠帕鲁
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
