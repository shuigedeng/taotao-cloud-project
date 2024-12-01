package com.taotao.cloud.job.server.jobserver.remote.worker.filter;


import com.taotao.cloud.job.server.jobserver.common.module.WorkerInfo;
import com.taotao.cloud.job.server.jobserver.persistence.domain.JobInfo;

/**
 * filter worker by system metrics or other info
 *
 * @author shuigedeng
 * @since 2021/2/16
 */
public interface WorkerFilter {

    /**
     *
     * @param workerInfo worker info, maybe you need to use your customized info in SystemMetrics#extra
     * @param jobInfoDO job info
     * @return true will remove the worker in process list
     */
    boolean filter(WorkerInfo workerInfo, JobInfo jobInfoDO);
}
