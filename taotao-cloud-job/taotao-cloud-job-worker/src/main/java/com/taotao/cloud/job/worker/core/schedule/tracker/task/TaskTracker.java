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

package com.taotao.cloud.job.worker.core.schedule.tracker.task;

import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.job.worker.common.module.InstanceInfo;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuigedeng
 * @since 2022/9/19
 */
@Slf4j
public abstract class TaskTracker {

    /**
     * TaskTracker创建时间
     */
    protected final long createTime;

    /**
     * 任务实例ID，使用频率过高，从 InstanceInfo 提取出来单独保存一份
     */
    protected final long instanceId;

    /**
     * 任务实例信息
     */
    protected final InstanceInfo instanceInfo;

    /**
     * worker 运行时元数据
     */
    protected final TtcJobWorkerConfig config;

    /**
     * 是否结束
     */
    protected final AtomicBoolean finished;

    /**
     * 连续上报多次失败后放弃上报，视为结果不可达，TaskTracker down
     */
    protected int reportFailedCnt = 0;

    protected static final int MAX_REPORT_FAILED_THRESHOLD = 5;

    protected TaskTracker(ScheduleCausa.ServerScheduleJobReq req, TtcJobWorkerConfig config) {
        this.createTime = System.currentTimeMillis();
        this.config = config;
        this.instanceId = req.getInstanceId();

        this.instanceInfo = new InstanceInfo();

        instanceInfo.setJobId(req.getJobId());
        instanceInfo.setInstanceId(req.getInstanceId());
        instanceInfo.setProcessorType(req.getProcessorType());
        instanceInfo.setProcessorInfo(req.getProcessorInfo());
        instanceInfo.setJobParams(req.getJobParams());

        this.finished = new AtomicBoolean(false);
    }

    /**
     * 销毁
     */
    public abstract void destroy();

    /**
     * 停止任务
     */
    public abstract void stopTask();

    /**
     * 查询任务实例的详细运行状态
     *
     * @return 任务实例的详细运行状态
     */
    // public abstract InstanceDetail fetchRunningStatus(ServerQueryInstanceStatusReq req);

    //    public static void reportCreateErrorToServer(ScheduleCausa.ServerScheduleJobReq req,
    // KJobWorkerConfig config, Exception e) {
    //        log.warn("[TaskTracker-{}] create TaskTracker from request({}) failed.",
    // req.getInstanceId(), req, e);
    //        // 直接发送失败请求
    //        TaskTrackerReportInstanceStatusReq response = new
    // TaskTrackerReportInstanceStatusReq();
    //
    //        response.setAppId(config.getAppId());
    //        response.setJobId(req.getJobId());
    //        response.setInstanceId(req.getInstanceId());
    //        response.setWfInstanceId(req.getWfInstanceId());
    //
    //        response.setInstanceStatus(InstanceStatus.FAILED.getV());
    //        response.setResult(String.format("init TaskTracker failed, reason: %s",
    // e.toString()));
    //        response.setReportTime(System.currentTimeMillis());
    //        response.setStartTime(System.currentTimeMillis());
    //        response.setSourceAddress(config.getWorkerAddress());
    //
    //        TransportUtils.ttReportInstanceStatus(response,
    // config.getServerDiscoveryService().getCurrentServerAddress(), config.getTransporter());
    //    }

    //    protected void reportFinalStatusThenDestroy(KJobWorkerConfig config,
    // TaskTrackerReportInstanceStatusReq reportInstanceStatusReq) {
    //        String currentServerAddress =
    // config.getServerDiscoveryService().getCurrentServerAddress();
    //        // 最终状态需要可靠上报
    //        boolean serverAccepted = false;
    //        try {
    //            serverAccepted =
    // TransportUtils.reliableTtReportInstanceStatus(reportInstanceStatusReq, currentServerAddress,
    // config.getTransporter());
    //        } catch (Exception e) {
    //            log.warn("[TaskTracker-{}] report finished status failed, req={}.", instanceId,
    // reportInstanceStatusReq, e);
    //        }
    //        if (!serverAccepted) {
    //            if (++reportFailedCnt > MAX_REPORT_FAILED_THRESHOLD) {
    //                log.error("[TaskTracker-{}] try to report finished status(detail={}) lots of
    // times but all failed, it's time to give up, so the process result will be dropped",
    // instanceId, reportInstanceStatusReq);
    //                destroy();
    //            }
    //            return;
    //        }
    //        log.info("[TaskTracker-{}] report finished status(detail={}) success", instanceId,
    // reportInstanceStatusReq);
    //        destroy();
    //    }
}
