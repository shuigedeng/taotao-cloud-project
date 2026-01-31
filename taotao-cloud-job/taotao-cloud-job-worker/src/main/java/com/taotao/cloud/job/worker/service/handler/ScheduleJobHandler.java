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

package com.taotao.cloud.job.worker.service.handler;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.job.worker.core.schedule.tracker.manager.LightTaskTrackerManager;
import com.taotao.cloud.job.worker.core.schedule.tracker.task.light.LightTaskTracker;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * ScheduleJobHandler
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class ScheduleJobHandler implements RpcHandler {

    TtcJobWorkerConfig config;

    public ScheduleJobHandler( TtcJobWorkerConfig ttcJobWorkerConfig ) {
        config = ttcJobWorkerConfig;
    }

    public void handle( Object req, StreamObserver<CommonCausa.Response> responseObserver ) {
        ScheduleCausa.ServerScheduleJobReq scheduleJobReq =
                (ScheduleCausa.ServerScheduleJobReq) req;
        final LightTaskTracker taskTracker =
                LightTaskTrackerManager.getTaskTracker(scheduleJobReq.getInstanceId());
        if (taskTracker != null) {
            log.warn(
                    "[TaskTrackerActor] LightTaskTracker({}) for instance(id={}) already exists.",
                    taskTracker,
                    scheduleJobReq.getInstanceId());
            return;
        }
        // 判断是否已经 overload
        if (LightTaskTrackerManager.currentTaskTrackerSize()
                >= config.getMaxLightweightTaskNum() * LightTaskTrackerManager.OVERLOAD_FACTOR) {
            // ignore this request
            log.warn(
                    "[TaskTrackerActor] this worker is overload,ignore this request(instanceId={}),current size = {}!",
                    scheduleJobReq.getInstanceId(),
                    LightTaskTrackerManager.currentTaskTrackerSize());
            return;
        }
        if (LightTaskTrackerManager.currentTaskTrackerSize() >= config.getMaxLightweightTaskNum()) {
            log.warn(
                    "[TaskTrackerActor] this worker will be overload soon,current size = {}!",
                    LightTaskTrackerManager.currentTaskTrackerSize());
        }
        // 创建轻量级任务
        LightTaskTrackerManager.atomicCreateTaskTracker(
                scheduleJobReq.getInstanceId(),
                ignore -> LightTaskTracker.create(scheduleJobReq, config));
    }
}
