package com.taotao.cloud.job.server.common.module;

import com.taotao.cloud.job.common.domain.WorkerHeartbeat;
import com.taotao.cloud.job.common.module.SystemMetrics;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * worker info
 *
 * @author shuigedeng
 * @since 2021/2/7
 */
@Data
@Slf4j
public class WorkerInfo {

	private String address;

	private long lastActiveTime;


	private String client;


	private int lightTaskTrackerNum;


	private long lastOverloadTime;

	private boolean overloading;

	private SystemMetrics systemMetrics;


	private static final long WORKER_TIMEOUT_MS = 60000;

	public void refresh(WorkerHeartbeat workerHeartbeat) {
		address = workerHeartbeat.getWorkerAddress();
		lastActiveTime = workerHeartbeat.getHeartbeatTime();
		client = workerHeartbeat.getClient();
		systemMetrics = workerHeartbeat.getSystemMetrics();

		lightTaskTrackerNum = workerHeartbeat.getLightTaskTrackerNum();

		if (workerHeartbeat.isOverload()) {
			overloading = true;
			lastOverloadTime = workerHeartbeat.getHeartbeatTime();
			log.warn("[WorkerInfo] worker {} is overload!", getAddress());
		} else {
			overloading = false;
		}
	}

	public boolean timeout() {
		long timeout = System.currentTimeMillis() - lastActiveTime;
		return timeout > WORKER_TIMEOUT_MS;
	}

	public boolean overload() {
		return overloading;
	}
}
