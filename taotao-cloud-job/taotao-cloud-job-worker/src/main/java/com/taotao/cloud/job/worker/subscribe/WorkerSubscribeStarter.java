package com.taotao.cloud.job.worker.subscribe;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.job.common.utils.net.MyNetUtil;
import com.taotao.cloud.job.remote.protos.RegisterCausa;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.worker.common.grpc.strategies.StrategyCaller;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
@Slf4j
public class WorkerSubscribeStarter {

    public static void start(String appName){
        ThreadFactory registerThreadFactory = new ThreadFactoryBuilder().setNameFormat("TtcJob-server-register-%d").build();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3, registerThreadFactory);
        RegisterCausa.WorkerSubscribeReq build = RegisterCausa.WorkerSubscribeReq.newBuilder()
                .setAppName(appName)
                .setScheduleTime(0)
                .setWorkerIpAddress(MyNetUtil.address)
                .setServerIpAddress(WorkerSubscribeManager.getCurrentServerIp())
                .setSubscribeTimestamp(System.currentTimeMillis())
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
