package com.taotao.cloud.job.worker;

import com.google.common.collect.Lists;
import com.taotao.cloud.job.worker.common.KJobWorkerConfig;
import com.taotao.cloud.job.worker.common.executor.ExecutorManager;
import com.taotao.cloud.job.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.worker.core.discover.KJobServerDiscoverService;
import com.taotao.cloud.job.worker.core.schedule.WorkerHealthReporter;
import com.taotao.cloud.job.worker.processor.KJobProcessorLoader;
import com.taotao.cloud.job.worker.processor.ProcessorLoader;
import com.taotao.cloud.job.worker.processor.factory.BuiltInDefaultProcessorFactory;
import com.taotao.cloud.job.worker.processor.factory.ProcessorFactory;
import com.taotao.cloud.job.worker.subscribe.WorkerSubscribeStarter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 客户端启动类
 *
 */
@Slf4j
public class KJobWorker {

    KJobWorkerConfig config;
    public KJobWorker(KJobWorkerConfig config) {
        this.config = config;
    }
    public void init() {
        log.info("[KJob] starting ...");

        // init rpc
        RpcInitializer rpcInitializer = new RpcInitializer(config.getServerPort(),config.getPort(),config.getServerAddress(),config.getNameServerAddress());
        rpcInitializer.initRpcStrategies();
        rpcInitializer.initRpcServer(config);

        KJobServerDiscoverService kJobServerDiscoverService = new KJobServerDiscoverService(config);

        try{
            // subscribe to nameServer
            WorkerSubscribeStarter.start(config.getAppName());

            // get appId
            kJobServerDiscoverService.assertApp();

            // init ThreadPool
            ExecutorManager.initExecutorManager();

            // init processorLoader for handler task
            ProcessorLoader processorLoader = buildProcessorLoader();
            KJobWorkerConfig.setProcessorLoader(processorLoader);

            // connect server
            kJobServerDiscoverService.heartbeatCheck(ExecutorManager.getHeartbeatExecutor());

            // init health reporter
            ExecutorManager.getHealthReportExecutor().scheduleAtFixedRate(new WorkerHealthReporter(kJobServerDiscoverService, config), 0, config.getHealthReportInterval(), TimeUnit.SECONDS);

        } catch (Exception e){
            log.error("[kJob] start error");
        }


    }
    private ProcessorLoader buildProcessorLoader() {
        List<ProcessorFactory> customPF = Optional.ofNullable(config.getProcessorFactoryList()).orElse(Collections.emptyList());
        List<ProcessorFactory> finalPF = Lists.newArrayList(customPF);

        finalPF.add(new BuiltInDefaultProcessorFactory());

        return new KJobProcessorLoader(finalPF);
    }

    public void destroy() {
        ExecutorManager.shutdown();
    }
}
