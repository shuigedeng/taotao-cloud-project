package com.taotao.cloud.job.core.worker;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.domain.WorkerAppInfo;
import com.taotao.cloud.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.worker.common.executor.ExecutorManager;
import com.taotao.cloud.worker.core.discover.TtcJobServerDiscoverService;
import com.taotao.cloud.worker.core.schedule.WorkerHealthReporter;
import com.taotao.cloud.worker.processor.TtcJobProcessorLoader;
import com.taotao.cloud.worker.processor.ProcessorLoader;
import com.taotao.cloud.worker.processor.factory.BuiltInDefaultProcessorFactory;
import com.taotao.cloud.worker.processor.factory.ProcessorFactory;
import com.taotao.cloud.worker.subscribe.WorkerSubscribeStarter;

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

        TtcJobServerDiscoverService kJobServerDiscoverService = new TtcJobServerDiscoverService(config);

        try{
            // subscribe to nameServer
            WorkerSubscribeStarter.start(config.getAppName());

            // get appId
            kJobServerDiscoverService.assertApp();

            // init ThreadPool
            ExecutorManager.initExecutorManager();

            // init processorLoader for handler task
            ProcessorLoader processorLoader = buildProcessorLoader();
            TtcJobWorkerConfig.setProcessorLoader(processorLoader);

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

        return new TtcJobProcessorLoader(finalPF);
    }

    public void destroy() {
        ExecutorManager.shutdown();
    }
}
