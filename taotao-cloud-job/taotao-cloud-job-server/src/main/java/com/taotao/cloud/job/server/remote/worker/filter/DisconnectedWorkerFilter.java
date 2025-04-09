package com.taotao.cloud.job.server.remote.worker.filter;

import com.taotao.cloud.job.server.common.module.WorkerInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * filter disconnected worker
 *
 * @author shuigedeng
 * @since 2021/2/19
 */
@Slf4j
@Component
public class DisconnectedWorkerFilter implements WorkerFilter {

	@Override
	public boolean filter(WorkerInfo workerInfo, JobInfo jobInfo) {
		boolean timeout = workerInfo.timeout();
		if (timeout) {
			log.info("[Job-{}] filter worker[{}] due to timeout(lastActiveTime={})", jobInfo.getId(), workerInfo.getAddress(), workerInfo.getLastActiveTime());
		}
		return timeout;
	}
}
