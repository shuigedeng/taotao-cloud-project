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

package com.taotao.cloud.job.worker.core.schedule;

import com.taotao.cloud.job.common.domain.WorkerHeartbeat;
import com.taotao.cloud.job.common.enhance.SafeRunnable;
import com.taotao.cloud.job.common.module.SystemMetrics;
import com.taotao.cloud.job.common.utils.net.MyNetUtil;
import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.worker.common.grpc.strategies.StrategyCaller;
import com.taotao.cloud.job.worker.common.utils.SystemInfoUtils;
import com.taotao.cloud.job.worker.core.discover.ServerDiscoverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * Worker健康度定时上报
 *
 * @author shuigedeng
 * @since 2020/3/25
 */
@Slf4j
@RequiredArgsConstructor
public class WorkerHealthReporter extends SafeRunnable {

    private final ServerDiscoverService serverDiscoverService;
    private final TtcJobWorkerConfig config;

    @Override
    public void run0() {

        // 没有可用Server，无法上报
        String currentServer = serverDiscoverService.getCurrentServerAddress();
        if (StringUtils.isEmpty(currentServer)) {
            log.warn("[WorkerHealthReporter] no available server,fail to report health info!");
            return;
        }

        SystemMetrics systemMetrics;
        systemMetrics = SystemInfoUtils.getSystemMetrics();

        WorkerHeartbeat heartbeat = new WorkerHeartbeat();
        heartbeat.setServerIpAddress(currentServer);
        heartbeat.setSystemMetrics(systemMetrics);
        heartbeat.setAppName(config.getAppName());
        heartbeat.setAppId(serverDiscoverService.getCurrentAppId());
        heartbeat.setHeartbeatTime(System.currentTimeMillis());
        heartbeat.setWorkerAddress(MyNetUtil.address);
        heartbeat.setClient("KingPenguin");
        //        heartbeat.setTag(config.getWorkerConfig().getTag());

        // 上报 Tracker 数量
        //
        // heartbeat.setLightTaskTrackerNum(LightTaskTrackerManager.currentTaskTrackerSize());
        ////
        // heartbeat.setHeavyTaskTrackerNum(HeavyTaskTrackerManager.currentTaskTrackerSize());
        //        // 是否超载
        //        if (config.getMaxLightweightTaskNum() <=
        // LightTaskTrackerManager.currentTaskTrackerSize() || config.getMaxHeavyweightTaskNum() <=
        // HeavyTaskTrackerManager.currentTaskTrackerSize()){
        //            heartbeat.setOverload(true);
        //        }

        // 发送请求
        if (StringUtils.isEmpty(currentServer)) {
            return;
        }
        // log
        log.info(
                "[WorkerHealthReporter] report health status,appId:{},appName:{},isOverload:{},maxLightweightTaskNum:{},currentLightweightTaskNum:{},maxHeavyweightTaskNum:{}",
                heartbeat.getAppId(),
                heartbeat.getAppName(),
                heartbeat.isOverload(),
                config.getMaxLightweightTaskNum(),
                heartbeat.getLightTaskTrackerNum(),
                config.getMaxHeavyweightTaskNum());

        ScheduleCausa.SystemMetrics builder0 =
                ScheduleCausa.SystemMetrics.newBuilder()
                        .setCpuLoad(heartbeat.getSystemMetrics().getCpuLoad())
                        .setCpuProcessors(heartbeat.getSystemMetrics().getCpuProcessors())
                        .setDiskTotal(heartbeat.getSystemMetrics().getDiskTotal())
                        .setDiskUsage(heartbeat.getSystemMetrics().getDiskUsage())
                        .setScore(heartbeat.getSystemMetrics().getScore())
                        .setJvmMaxMemory(heartbeat.getSystemMetrics().getJvmMaxMemory())
                        .setJvmUsedMemory(heartbeat.getSystemMetrics().getJvmUsedMemory())
                        .setJvmMemoryUsage(heartbeat.getSystemMetrics().getJvmMemoryUsage())
                        .build();
        ScheduleCausa.WorkerHeartbeat builder =
                ScheduleCausa.WorkerHeartbeat.newBuilder()
                        .setServerIpAddress(currentServer)
                        .setAppId(heartbeat.getAppId())
                        .setAppName(heartbeat.getAppName())
                        .setHeartbeatTime(heartbeat.getHeartbeatTime())
                        .setClient(heartbeat.getClient())
                        .setIsOverload(heartbeat.isOverload())
                        .setWorkerAddress(heartbeat.getWorkerAddress())
                        .setSystemMetrics(builder0)
                        .build();

        StrategyCaller.call(TransportTypeEnum.HEARTBEAT_HEALTH_REPORT, builder);
    }
}
