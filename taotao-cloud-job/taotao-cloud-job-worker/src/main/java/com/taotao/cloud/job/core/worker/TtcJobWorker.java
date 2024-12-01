package com.taotao.cloud.job.core.worker;

import com.google.common.collect.Lists;
import com.taotao.cloud.job.core.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.job.core.worker.common.executor.ExecutorManager;
import com.taotao.cloud.job.core.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.core.worker.core.discover.TtcJobServerDiscoverService;
import com.taotao.cloud.job.core.worker.core.schedule.WorkerHealthReporter;
import com.taotao.cloud.job.core.worker.processor.ProcessorLoader;
import com.taotao.cloud.job.core.worker.processor.TtcJobProcessorLoader;
import com.taotao.cloud.job.core.worker.processor.factory.BuiltInDefaultProcessorFactory;
import com.taotao.cloud.job.core.worker.processor.factory.ProcessorFactory;
import com.taotao.cloud.job.core.worker.subscribe.WorkerSubscribeStarter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
/**
 * 客户端启动类
 *
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
        RpcInitializer rpcInitializer = new RpcInitializer(config.getServerPort(),config.getPort(),config.getServerAddress(),config.getNameServerAddress());
        rpcInitializer.initRpcStrategies();
        rpcInitializer.initRpcServer(config);

        TtcJobServerDiscoverService ttcJobServerDiscoverService = new TtcJobServerDiscoverService(config);

        try{
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
            ExecutorManager.getHealthReportExecutor().scheduleAtFixedRate(new WorkerHealthReporter(ttcJobServerDiscoverService, config), 0, config.getHealthReportInterval(), TimeUnit.SECONDS);

        } catch (Exception e){
            log.error("[ttcJob] start error");
        }


    }
    private ProcessorLoader buildProcessorLoader() {
        List<ProcessorFactory> customPF = Optional.ofNullable(config.getProcessorFactoryList()).orElse(Collections.emptyList());
        List<ProcessorFactory> finalPF = Lists.newArrayList(customPF);

        finalPF.add(new BuiltInDefaultProcessorFactory());

        return new TtcJobProcessorLoader(finalPF);
    }

    public void destroy() {
        ExecutorManager.shutdown();
    }
}
