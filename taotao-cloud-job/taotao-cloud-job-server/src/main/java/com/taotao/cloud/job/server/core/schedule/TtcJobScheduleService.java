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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taotao.cloud.job.common.enums.SwitchableStatus;
import com.taotao.cloud.job.common.enums.TimeExpressionType;
import com.taotao.cloud.job.common.module.LifeCycle;
import com.taotao.cloud.job.server.common.config.TtcJobServerConfig;
import com.taotao.cloud.job.server.core.instance.InstanceService;
import com.taotao.cloud.job.server.core.timewheel.holder.InstanceTimeWheelService;
import com.taotao.cloud.job.server.extension.lock.LockService;
import com.taotao.cloud.job.server.persistence.domain.AppInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import com.taotao.cloud.job.server.persistence.mapper.AppInfoMapper;
import com.taotao.cloud.job.server.persistence.mapper.JobInfoMapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class TtcJobScheduleService {
    private static final int MAX_APP_NUM = 10;
    public static final long SCHEDULE_RATE = 10000;
    @Autowired AppInfoMapper appInfoMapper;
    @Autowired TtcJobServerConfig ttcJobServerConfig;
    @Autowired JobInfoMapper jobInfoMapper;
    @Autowired InstanceService instanceService;
    @Autowired DispatchService dispatchService;
    @Autowired TimingStrategyService timingStrategyService;
    @Autowired LockService lockService;

    public void scheduleNormalJob(TimeExpressionType timeExpressionType) {
        long start = System.currentTimeMillis();
        // 调度 CRON 表达式 JOB
        try {
            Map<Long, String> allAppInfos =
                    appInfoMapper
                            .selectList(
                                    new QueryWrapper<AppInfo>()
                                            .lambda()
                                            .eq(
                                                    AppInfo::getCurrentServer,
                                                    ttcJobServerConfig.getAddress()))
                            .stream()
                            .collect(Collectors.toMap(AppInfo::getId, AppInfo::getAppName));
            if (CollectionUtils.isEmpty(allAppInfos)) {
                log.info("[NormalScheduler] current server has no app's job to schedule.");
                return;
            }
            scheduleNormalJob0(timeExpressionType, allAppInfos);
        } catch (Exception e) {
            log.error("[NormalScheduler] schedule cron job failed.", e);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("[NormalScheduler] {} job schedule use {} ms.", timeExpressionType, cost);
        if (cost > SCHEDULE_RATE) {
            log.warn(
                    "[NormalScheduler] The database query is using too much time({}ms), please check if the database load is too high!",
                    cost);
        }
    }

    private void scheduleNormalJob0(
            TimeExpressionType timeExpressionType, Map<Long, String> allAppInfos) {

        long nowTime = System.currentTimeMillis();
        long timeThreshold = nowTime + 2 * SCHEDULE_RATE;
        // 可能有些appId处于同一个appName下
        ArrayList<Long> appIds = new ArrayList<>(allAppInfos.keySet());
        Lists.partition(appIds, MAX_APP_NUM)
                .forEach(
                        partAppIds -> {
                            try {
                                // 对appName尝试进行加锁，成功则进行调度，避免重复调度
                                ArrayList<String> targetAppNames = new ArrayList<>();
                                for (String appName : allAppInfos.values()) {
                                    boolean lockStatus = lockService.tryLock(appName, 30000);
                                    if (lockStatus) {
                                        targetAppNames.add(appName);
                                    }
                                }
                                if (CollectionUtils.isEmpty(targetAppNames)) {
                                    return;
                                }
                                // 查询条件：任务开启 + 使用CRON表达调度时间 + 指定appName + 即将需要调度执行
                                List<JobInfo> jobInfos =
                                        jobInfoMapper.selectList(
                                                new QueryWrapper<JobInfo>()
                                                        .lambda()
                                                        .in(JobInfo::getAppName, targetAppNames)
                                                        .eq(
                                                                JobInfo::getStatus,
                                                                SwitchableStatus.ENABLE.getV())
                                                        .eq(
                                                                JobInfo::getTimeExpressionType,
                                                                timeExpressionType.getV())
                                                        .le(
                                                                JobInfo::getNextTriggerTime,
                                                                timeThreshold));
                                if (CollectionUtils.isEmpty(jobInfos)) {
                                    return;
                                }
                                // 1. 批量写日志表
                                Map<Long, Long> jobId2InstanceId = Maps.newHashMap();
                                Map<String, Integer> appName2JobNum = Maps.newHashMap();
                                log.info(
                                        "[NormalScheduler] These {} jobs will be scheduled: {}.",
                                        timeExpressionType.name(),
                                        jobInfos);

                                jobInfos.forEach(
                                        jobInfo -> {
                                            Long instanceId =
                                                    instanceService
                                                            .create(
                                                                    jobInfo.getId(),
                                                                    jobInfo.getAppName(),
                                                                    jobInfo.getJobParams(),
                                                                    null,
                                                                    null,
                                                                    jobInfo.getNextTriggerTime())
                                                            .getInstanceId();
                                            jobId2InstanceId.put(jobInfo.getId(), instanceId);
                                            appName2JobNum.put(
                                                    jobInfo.getAppName(),
                                                    appName2JobNum.getOrDefault(
                                                                    jobInfo.getAppName(), 0)
                                                            + 1);
                                        });

                                // 2. 推入时间轮中等待调度执行
                                jobInfos.forEach(
                                        JobInfo -> {
                                            Long instanceId = jobId2InstanceId.get(JobInfo.getId());

                                            long targetTriggerTime = JobInfo.getNextTriggerTime();
                                            long delay = 0;
                                            // 这里之前可能耗时的是，执行数据库的插入操作，但是其实插入的时间也可以忽略
                                            if (targetTriggerTime < nowTime) {
                                                log.warn(
                                                        "[Job-{}] schedule delay, expect: {}, current: {}",
                                                        JobInfo.getId(),
                                                        targetTriggerTime,
                                                        System.currentTimeMillis());
                                            } else {
                                                delay = targetTriggerTime - nowTime;
                                            }

                                            InstanceTimeWheelService.schedule(
                                                    instanceId,
                                                    delay,
                                                    () ->
                                                            dispatchService.dispatch(
                                                                    JobInfo,
                                                                    instanceId,
                                                                    Optional.empty(),
                                                                    Optional.empty(),
                                                                    appName2JobNum));
                                        });

                                // 3. 计算下一次调度时间（忽略5S内的重复执行，即CRON模式下最小的连续执行间隔为 SCHEDULE_RATE ms）
                                jobInfos.forEach(
                                        JobInfo -> {
                                            try {
                                                refreshJob(timeExpressionType, JobInfo);
                                            } catch (Exception e) {
                                                log.error(
                                                        "[Job-{}] refresh job failed.",
                                                        JobInfo.getId(),
                                                        e);
                                            }
                                        });

                            } catch (Exception e) {
                                log.error(
                                        "[NormalScheduler] schedule {} job failed.",
                                        timeExpressionType.name(),
                                        e);
                            }
                        });
    }

    private void refreshJob(TimeExpressionType timeExpressionType, JobInfo jobInfo) {

        LifeCycle lifeCycle = LifeCycle.parse(jobInfo.getLifecycle());
        Long nextTriggerTime =
                timingStrategyService.calculateNextTriggerTime(
                        jobInfo.getNextTriggerTime(),
                        timeExpressionType,
                        jobInfo.getTimeExpression(),
                        lifeCycle.getStart(),
                        lifeCycle.getEnd());

        JobInfo updatedJobInfo = new JobInfo();
        BeanUtils.copyProperties(jobInfo, updatedJobInfo);

        if (nextTriggerTime == null) {
            log.warn(
                    "[Job-{}] this job won't be scheduled anymore, system will set the status to DISABLE!",
                    jobInfo.getId());
            updatedJobInfo.setStatus(SwitchableStatus.DISABLE.getV());
        } else {
            updatedJobInfo.setNextTriggerTime(nextTriggerTime);
        }
        updatedJobInfo.setGmtModified(new Date());

        jobInfoMapper.updateById(updatedJobInfo);
    }
}
