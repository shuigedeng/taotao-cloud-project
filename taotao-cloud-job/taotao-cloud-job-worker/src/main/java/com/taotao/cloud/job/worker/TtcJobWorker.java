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

package com.taotao.cloud.job.worker;

import com.google.common.collect.Lists;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.job.worker.common.executor.ExecutorManager;
import com.taotao.cloud.job.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.worker.core.discover.TtcJobServerDiscoverService;
import com.taotao.cloud.job.worker.core.schedule.WorkerHealthReporter;
import com.taotao.cloud.job.worker.processor.ProcessorLoader;
import com.taotao.cloud.job.worker.processor.TtcJobProcessorLoader;
import com.taotao.cloud.job.worker.processor.factory.BuiltInDefaultProcessorFactory;
import com.taotao.cloud.job.worker.processor.factory.ProcessorFactory;
import com.taotao.cloud.job.worker.subscribe.WorkerSubscribeStarter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端启动类
 */
@Slf4j
public class TtcJobWorker {

    TtcJobWorkerConfig config;

    public TtcJobWorker(TtcJobWorkerConfig config) {
        this.config = config;
    }

    public void init() {
        log.info("[TtcJob] starting ...");

        // init rpc
        RpcInitializer rpcInitializer =
                new RpcInitializer(
                        config.getServerPort(),
                        config.getPort(),
                        config.getServerAddress(),
                        config.getNameServerAddress());
        rpcInitializer.initRpcStrategies();
        rpcInitializer.initRpcServer(config);

        TtcJobServerDiscoverService ttcJobServerDiscoverService =
                new TtcJobServerDiscoverService(config);

        try {
            // subscribe to nameServer
            WorkerSubscribeStarter.start(config.getAppName());

            // get appId
            ttcJobServerDiscoverService.assertApp();

            // init ThreadPool
            ExecutorManager.initExecutorManager();

            // init processorLoader for handler task
            ProcessorLoader processorLoader = buildProcessorLoader();
            TtcJobWorkerConfig.setProcessorLoader(processorLoader);

            // connect server
            ttcJobServerDiscoverService.heartbeatCheck(ExecutorManager.getHeartbeatExecutor());

            // init health reporter
            ExecutorManager.getHealthReportExecutor()
                    .scheduleAtFixedRate(
                            new WorkerHealthReporter(ttcJobServerDiscoverService, config),
                            0,
                            config.getHealthReportInterval(),
                            TimeUnit.SECONDS);

        } catch (Exception e) {
            log.error("[TtcJob] start error");
        }
    }

    private ProcessorLoader buildProcessorLoader() {
        List<ProcessorFactory> customPF =
                Optional.ofNullable(config.getProcessorFactoryList())
                        .orElse(Collections.emptyList());
        List<ProcessorFactory> finalPF = Lists.newArrayList(customPF);

        finalPF.add(new BuiltInDefaultProcessorFactory());

        return new TtcJobProcessorLoader(finalPF);
    }

    public void destroy() {
        ExecutorManager.shutdown();
    }
}
