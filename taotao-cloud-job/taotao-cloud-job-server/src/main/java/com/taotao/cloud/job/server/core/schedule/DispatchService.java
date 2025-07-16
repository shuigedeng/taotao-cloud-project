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

package com.taotao.cloud.job.server.core.schedule;

import static com.taotao.cloud.job.common.enums.InstanceStatus.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.job.common.SystemInstanceResult;
import com.taotao.cloud.job.common.enums.TimeExpressionType;
import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.server.common.Holder;
import com.taotao.cloud.job.server.common.grpc.ServerScheduleJobRpcClient;
import com.taotao.cloud.job.server.common.module.WorkerInfo;
import com.taotao.cloud.job.server.extension.lock.LockService;
import com.taotao.cloud.job.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import com.taotao.cloud.job.server.persistence.mapper.InstanceInfoMapper;
import com.taotao.cloud.job.server.remote.worker.WorkerClusterQueryService;
import com.taotao.cloud.job.server.remote.worker.selector.TaskTrackerSelectorService;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 派送服务（将任务从Server派发到Worker）
 *
 * @author shuigedeng
 * @author shuigedeng
 * @since 2020/4/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {

    private final WorkerClusterQueryService workerClusterQueryService;
    //    private final InstanceManager instanceManager;
    //    private final InstanceMetadataService instanceMetadataService;
    private final InstanceInfoMapper instanceInfoMapper;
    private final TaskTrackerSelectorService taskTrackerSelectorService;
    private final ServerScheduleJobRpcClient serverScheduleJobRpcClient;
    private final LockService lockService;

    /**
     * 将任务从Server派发到Worker（TaskTracker）
     * 只会派发当前状态为等待派发的任务实例
     * **************************************************
     * 2021-02-03 modify by Echo009
     * 1、移除参数 当前运行次数、工作流实例ID、实例参数
     * 更改为从当前任务实例中获取获取以上信息
     * 2、移除运行次数相关的（runningTimes）处理逻辑
     * <p>
     * **************************************************
     *
     * @param jobInfo              任务的元信息
     * @param instanceId           任务实例ID
     * @param instanceInfoOptional 任务实例信息，可选
     * @param overloadOptional     超载信息，可选
     * @param appName2JobNum
     */
    public void dispatch(
            JobInfo jobInfo,
            Long instanceId,
            Optional<InstanceInfo> instanceInfoOptional,
            Optional<Holder<Boolean>> overloadOptional,
            Map<String, Integer> appName2JobNum) {
        // 允许从外部传入实例信息，减少 io 次数
        // 检查当前任务是否被取消
        InstanceInfo instanceInfo =
                instanceInfoMapper.selectOne(
                        new QueryWrapper<InstanceInfo>()
                                .lambda()
                                .eq(InstanceInfo::getInstanceId, instanceId));
        Long jobId = instanceInfo.getJobId();
        if (CANCELED.getV() == instanceInfo.getStatus()) {
            log.info(
                    "[Dispatcher-{}|{}] cancel dispatch due to instance has been canceled",
                    jobId,
                    instanceId);
            return;
        }
        // 已经被派发过则不再派发
        if (instanceInfo.getStatus() != WAITING_DISPATCH.getV()) {
            log.info(
                    "[Dispatcher-{}|{}] cancel dispatch due to instance has been dispatched",
                    jobId,
                    instanceId);
            return;
        }
        // 任务信息已经被删除
        if (jobInfo.getId() == null) {
            log.warn(
                    "[Dispatcher-{}|{}] cancel dispatch due to job(id={}) has been deleted!",
                    jobId,
                    instanceId,
                    jobId);
            //            instanceManager.processFinishedInstance(instanceId,
            // instanceInfo.getWfInstanceId(), FAILED, "can't find job by id " + jobId);
            return;
        }

        Date now = new Date();
        String dbInstanceParams =
                instanceInfo.getInstanceParams() == null ? "" : instanceInfo.getInstanceParams();
        log.info(
                "[Dispatcher-{}|{}] start to dispatch job: {};instancePrams: {}.",
                jobId,
                instanceId,
                jobInfo,
                dbInstanceParams);

        // 查询当前运行的实例数
        long current = System.currentTimeMillis();
        Integer maxInstanceNum = jobInfo.getMaxInstanceNum();
        // 秒级任务只派发到一台机器，具体的 maxInstanceNum 由 TaskTracker 控制
        if (TimeExpressionType.FREQUENT_TYPES.contains(jobInfo.getTimeExpressionType())) {
            maxInstanceNum = 1;
        }

        // 0 代表不限制在线任务，还能省去一次 DB 查询
        if (maxInstanceNum > 0) {
            // 不统计 WAITING_DISPATCH 的状态：使用 OpenAPI 触发的延迟任务不应该统计进去（比如 delay 是 1 天）
            // 由于不统计 WAITING_DISPATCH，所以这个 runningInstanceCount 不包含本任务自身
            Long runningInstanceCount =
                    instanceInfoMapper.selectCount(
                            new QueryWrapper<InstanceInfo>()
                                    .lambda()
                                    .eq(InstanceInfo::getStatus, WAITING_WORKER_RECEIVE.getV())
                                    .or()
                                    .eq(InstanceInfo::getStatus, RUNNING.getV()));
            // 超出最大同时运行限制，不执行调度
            if (runningInstanceCount >= maxInstanceNum) {
                String result =
                        String.format(
                                SystemInstanceResult.TOO_MANY_INSTANCES,
                                runningInstanceCount,
                                maxInstanceNum);
                log.warn(
                        "[Dispatcher-{}|{}] cancel dispatch job due to too much instance is running ({} > {}).",
                        jobId,
                        instanceId,
                        runningInstanceCount,
                        maxInstanceNum);
                //                instanceInfoRepository.update4TriggerFailed(instanceId,
                // FAILED.getV(), current, current, RemoteConstant.EMPTY_ADDRESS, result, now);
                //                instanceManager.processFinishedInstance(instanceId,
                // instanceInfo.getWfInstanceId(), FAILED, result);
                return;
            }
        }
        // 获取当前最合适的 worker 列表
        List<WorkerInfo> suitableWorkers = workerClusterQueryService.geAvailableWorkers(jobInfo);

        if (CollectionUtils.isEmpty(suitableWorkers)) {
            log.warn(
                    "[Dispatcher-{}|{}] cancel dispatch job due to no worker available",
                    jobId,
                    instanceId);
            //            instanceInfoRepository.update4TriggerFailed(instanceId, FAILED.getV(),
            // current, current, RemoteConstant.EMPTY_ADDRESS,
            // SystemInstanceResult.NO_WORKER_AVAILABLE, now);
            //            instanceManager.processFinishedInstance(instanceId,
            // instanceInfo.getWfInstanceId(), FAILED, SystemInstanceResult.NO_WORKER_AVAILABLE);
            return;
        }
        // 判断是否超载，在所有可用 worker 超载的情况下直接跳过当前任务
        suitableWorkers = filterOverloadWorker(suitableWorkers);
        if (suitableWorkers.isEmpty()) {
            // 直接取消派发，减少一次数据库 io
            overloadOptional.ifPresent(booleanHolder -> booleanHolder.set(true));
            log.warn(
                    "[Dispatcher-{}|{}] cancel to dispatch job due to all worker is overload",
                    jobId,
                    instanceId);
            return;
        }
        // todo 这里可能涉及到分片，所以有多个ip，实际只传一个
        List<String> workerIpList =
                suitableWorkers.stream().map(WorkerInfo::getAddress).collect(Collectors.toList());
        // 构造任务调度请求
        // 发送请求（不可靠，需要一个后台线程定期轮询状态,后续可能是instance的线程定时检查）
        WorkerInfo taskTracker =
                taskTrackerSelectorService.select(jobInfo, instanceInfo, suitableWorkers);
        String taskTrackerAddress = taskTracker.getAddress();

        sendScheduleInfo(jobInfo, instanceInfo, taskTrackerAddress);

        log.info(
                "[Dispatcher-{}|{}] send schedule request to TaskTracker[address:{}] successfully.",
                jobId,
                instanceId,
                taskTrackerAddress);

        // 修改状态
        InstanceInfo build =
                InstanceInfo.builder()
                        .id(instanceId)
                        .status(WAITING_WORKER_RECEIVE.getV())
                        .taskTrackerAddress(taskTrackerAddress)
                        .build();
        instanceInfoMapper.updateById(build);

        // 若是该appName下的最后一个任务，释放appName锁
        appName2JobNum.put(jobInfo.getAppName(), appName2JobNum.get(jobInfo.getAppName()) - 1);
        if (appName2JobNum.get(jobInfo.getAppName()) == 0) {
            lockService.unlock(jobInfo.getAppName());
        }
    }

    private void sendScheduleInfo(
            JobInfo jobInfo, InstanceInfo instanceInfo, String taskTrackerAddress) {

        ScheduleCausa.ServerScheduleJobReq build =
                ScheduleCausa.ServerScheduleJobReq.newBuilder()
                        .setInstanceId(instanceInfo.getInstanceId())
                        .setJobId(jobInfo.getJobId())
                        .setJobParams(jobInfo.getJobParams())
                        .setProcessorInfo(jobInfo.getProcessorInfo())
                        .setWorkerAddress(taskTrackerAddress)
                        .setTimeExpression(jobInfo.getTimeExpression())
                        .setTimeExpressionType(
                                TimeExpressionType.of(jobInfo.getTimeExpressionType()).name())
                        .build();

        serverScheduleJobRpcClient.call(build);
    }

    private List<WorkerInfo> filterOverloadWorker(List<WorkerInfo> suitableWorkers) {

        List<WorkerInfo> res = new ArrayList<>(suitableWorkers.size());
        for (WorkerInfo suitableWorker : suitableWorkers) {
            if (suitableWorker.overload()) {
                continue;
            }
            res.add(suitableWorker);
        }
        return res;
    }
}
