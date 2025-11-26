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

package com.taotao.cloud.job.worker.core.discover;

import com.taotao.cloud.job.common.domain.WorkerAppInfo;
import com.taotao.cloud.job.common.exception.TtcJobException;
import com.taotao.cloud.job.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.worker.common.grpc.strategies.StrategyCaller;
import com.taotao.cloud.job.worker.subscribe.WorkerSubscribeManager;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * 服务发现
 *
 * @author shuigedeng
 * @since 2023/9/2
 */
@Slf4j
public class TtcJobServerDiscoverService implements ServerDiscoverService {

    private final TtcJobWorkerConfig config;

    // only ip address ,no port
    private String currentIpAddress;

    private static int FAILED_COUNT = 0;

    private static final int MAX_FAILED_COUNT = 3;

    private Long appId;

    public TtcJobServerDiscoverService(TtcJobWorkerConfig config) {
        this.config = config;
    }

    @Override
    public WorkerAppInfo assertApp() {
        ServerDiscoverCausa.AppName builder =
                ServerDiscoverCausa.AppName.newBuilder().setAppName(config.getAppName()).build();

        ServerDiscoverCausa.WorkInfo workInfo =
                (ServerDiscoverCausa.WorkInfo)
                        StrategyCaller.call(TransportTypeEnum.ASSERT_APP, builder);
        WorkerAppInfo workerAppInfo = new WorkerAppInfo();
        BeanUtils.copyProperties(workInfo, workerAppInfo);
        appId = workInfo.getAppId();
        return workerAppInfo;
    }

    @Override
    public String getCurrentServerAddress() {
        return currentIpAddress;
    }

    @Override
    public Long getCurrentAppId() {
        return appId;
    }

    @Override
    public void heartbeatCheck(ScheduledExecutorService heartbeatCheckExecutor) {
        // each discovery connect no more than MAX_FAILED_COUNT
        setCurrentIpAddress(discovery());
        if (StringUtils.isEmpty(this.currentIpAddress)) {
            throw new TtcJobException(
                    "can't find any available server, this worker has been quarantined.");
        }
        // check server and update currentIpAddress, asserting already success , so
        // scheduleAtFixedRate here
        heartbeatCheckExecutor.scheduleAtFixedRate(
                () -> {
                    try {
                        setCurrentIpAddress(discovery());
                        log.info("[TtcJobServerDiscovery] jump ip :{}", currentIpAddress);
                    } catch (Exception e) {
                        log.error("[TtcJobServerDiscovery] fail to discovery server!", e);
                    }
                },
                5,
                5,
                TimeUnit.SECONDS);
    }

    private void setCurrentIpAddress(String serverIpAddress) {
        this.currentIpAddress = serverIpAddress;
        WorkerSubscribeManager.setCurrentServerIp(currentIpAddress);
    }

    private String discovery() {
        String result = null;

        // ask currentIpAddress
        if (!StringUtils.isEmpty(currentIpAddress)) {
            result = acquire(currentIpAddress);
        }
        // ask other server
        for (String httpServerAddress : config.getServerAddress()) {
            if (StringUtils.isEmpty(result)) {
                result = acquire(httpServerAddress);
            } else {
                break;
            }
        }
        if (StringUtils.isEmpty(result)) {
            log.warn(
                    "[TtcJobServerDiscovery] can't find any available server, this worker has been quarantined.");

            if (FAILED_COUNT++ > MAX_FAILED_COUNT) {
                // todo frequent job
                FAILED_COUNT = 0;
            }
            return null;
        } else {
            // 重置失败次数
            FAILED_COUNT = 0;
            log.debug("[TtcJobServerDiscovery] current server is {}.", result);
            return result;
        }
    }

    private String acquire(String currentIpAddress) {

        ServerDiscoverCausa.HeartbeatCheck build =
                ServerDiscoverCausa.HeartbeatCheck.newBuilder()
                        .setCurrentServer(currentIpAddress)
                        .setAppId(appId)
                        .build();
        ServerDiscoverCausa.AvailableServer availableServer =
                (ServerDiscoverCausa.AvailableServer)
                        StrategyCaller.call(TransportTypeEnum.HEARTBEAT_CHECK, build);
        if (availableServer == null) {
            return null;
        } else {
            return availableServer.getAvailableServer();
        }
    }
}
