package com.taotao.cloud.job.core.worker.subscribe;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.utils.net.MyNetUtil;
import com.taotao.cloud.remote.protos.RegisterCausa;
import com.taotao.cloud.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.worker.common.grpc.strategies.StrategyCaller;
import com.taotao.cloud.worker.core.discover.TtcJobServerDiscoverService;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
@Slf4j
public class WorkerSubscribeStarter {

    public static void start(String appName){
        ThreadFactory registerThreadFactory = new ThreadFactoryBuilder().setNameFormat("ttcjob-server-register-%d").build();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3, registerThreadFactory);
        RegisterCausa.WorkerSubscribeReq build = RegisterCausa.WorkerSubscribeReq.newBuilder()
                .setAppName(appName)
                .setScheduleTime(0)
                .setWorkerIpAddress(MyNetUtil.address)
                .setServerIpAddress(WorkerSubscribeManager.getCurrentServerIp())
                .build();
        StrategyCaller.call(TransportTypeEnum.REGISTER_TO_NAMESERVER, build);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    StrategyCaller.call(TransportTypeEnum.REGISTER_TO_NAMESERVER, build);

                } catch (Exception e){
                    log.error("worker register to nameServer error");
                }
            }
        }, 10, 20, TimeUnit.SECONDS);
    }

}
