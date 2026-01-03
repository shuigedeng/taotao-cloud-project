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

package com.taotao.cloud.job.server.consumer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.job.common.enums.DispatchStrategy;
import com.taotao.cloud.job.common.enums.SwitchableStatus;
import com.taotao.cloud.job.common.enums.TimeExpressionType;
import com.taotao.cloud.job.common.module.LifeCycle;
import com.taotao.cloud.job.remote.protos.MqCausa;
import com.taotao.cloud.job.server.core.schedule.TimingStrategyService;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import com.taotao.cloud.job.server.persistence.service.JobInfoService;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Consumer
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Service
@Slf4j
public class Consumer {

    @Autowired
    JobInfoService jobInfoService;
    @Autowired
    TimingStrategyService timingStrategyService;

    private void createJob( MqCausa.Message message ) {
        MqCausa.CreateJobReq jobReq = message.getCreateJobReq();
        try {
            LifeCycle lifeCycle = LifeCycle.parse(jobReq.getLifeCycle());
            Long nextTriggerTime =
                    timingStrategyService.calculateNextTriggerTime(
                            null,
                            TimeExpressionType.getTimeExpressionTypeByProtoBuf(
                                    jobReq.getTimeExpressionType()),
                            jobReq.getTimeExpression(),
                            lifeCycle.getStart(),
                            lifeCycle.getEnd());

            JobInfo build2 =
                    JobInfo.builder()
                            .jobDescription(jobReq.getJobDescription())
                            .appName(jobReq.getAppName())
                            .jobId(jobReq.getJobId())
                            .nextTriggerTime(nextTriggerTime)
                            .jobName(jobReq.getJobName())
                            .jobParams(jobReq.getJobParams())
                            .timeExpression(jobReq.getTimeExpression())
                            .timeExpressionType(jobReq.getTimeExpressionTypeValue())
                            .maxInstanceNum(jobReq.getMaxInstanceNum())
                            .gmtCreate(new Date())
                            .gmtModified(new Date())
                            .lifecycle(jobReq.getLifeCycle())
                            .processorInfo(jobReq.getProcessorInfo())
                            .dispatchStrategy(DispatchStrategy.HEALTH_FIRST.getV())
                            .nextTriggerTime(0L)
                            .status(SwitchableStatus.ENABLE.getV())
                            .build();

            jobInfoService.save(build2);
            log.info("insert jobName :{} success", build2.getJobName());
        } catch (Exception e) {
            DelayedQueueManager.reConsume(message);
        }
    }

    private void deleteJob( MqCausa.Message message ) {
        try {
            MqCausa.DeleteJobReq jobReq = message.getDeleteJobReq();
            jobInfoService.remove(
                    new QueryWrapper<JobInfo>().lambda().eq(JobInfo::getJobId, jobReq.getJobId()));
            log.info("delete jobId :{} success", jobReq.getJobId());
        } catch (Exception e) {
            DelayedQueueManager.reConsume(message);
        }
    }

    private void updateJob( MqCausa.Message message ) {
        try {
            MqCausa.UpdateJobReq jobReq = message.getUpdateJobReq();
            LifeCycle lifeCycle = LifeCycle.parse(jobReq.getLifeCycle());
            Long nextTriggerTime =
                    timingStrategyService.calculateNextTriggerTime(
                            null,
                            TimeExpressionType.getTimeExpressionTypeByProtoBuf(
                                    jobReq.getTimeExpressionType()),
                            jobReq.getTimeExpression(),
                            lifeCycle.getStart(),
                            lifeCycle.getEnd());

            JobInfo build2 =
                    JobInfo.builder()
                            .jobDescription(jobReq.getJobDescription())
                            .appName(jobReq.getAppName())
                            .jobId(jobReq.getJobId())
                            .nextTriggerTime(nextTriggerTime)
                            .jobName(jobReq.getJobName())
                            .jobParams(jobReq.getJobParams())
                            .timeExpression(jobReq.getTimeExpression())
                            .timeExpressionType(jobReq.getTimeExpressionTypeValue())
                            .maxInstanceNum(jobReq.getMaxInstanceNum())
                            .gmtCreate(new Date())
                            .gmtModified(new Date())
                            .lifecycle(jobReq.getLifeCycle())
                            .processorInfo(jobReq.getProcessorInfo())
                            .dispatchStrategy(DispatchStrategy.HEALTH_FIRST.getV())
                            .nextTriggerTime(0L)
                            .status(SwitchableStatus.ENABLE.getV())
                            .build();

            jobInfoService.update(
                    build2,
                    new QueryWrapper<JobInfo>().lambda().eq(JobInfo::getJobId, build2.getJobId()));
            log.info("update jobName :{} success", build2.getJobName());
        } catch (Exception e) {
            DelayedQueueManager.reConsume(message);
        }
    }

    public void consume( MqCausa.Message message ) {
        switch (message.getMessageType()) {
            case JOB_CREATE:
                createJob(message);
                break;
            case JOB_UPDATE:
                updateJob(message);
                break;
            case JOB_DELETE:
                deleteJob(message);
                break;
        }
    }
}
