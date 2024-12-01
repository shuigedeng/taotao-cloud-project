package com.taotao.cloud.job.server.jobserver.register;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import com.taotao.cloud.common.constant.RemoteConstant;
import com.taotao.cloud.remote.api.RegisterToNameServerGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.RegisterCausa;
import com.taotao.cloud.server.common.config.TtcJobServerConfig;
import com.taotao.cloud.server.extension.singletonpool.GrpcStubSingletonPool;
import com.taotao.cloud.server.persistence.mapper.InstanceInfoMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
@Slf4j
public class ServerRegisterStarter implements InitializingBean {

    @Autowired
    TtcJobServerConfig ttcJobServerConfig;
    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadFactory registerThreadFactory = new ThreadFactoryBuilder().setNameFormat("ttcjob-server-register-%d").build();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3, registerThreadFactory);

        // get stub
        String s = ttcJobServerConfig.getNameServerAddress().split(":")[0];
        RegisterToNameServerGrpc.RegisterToNameServerBlockingStub stubSingleton = GrpcStubSingletonPool.getStubSingleton(s, RegisterToNameServerGrpc.class, RegisterToNameServerGrpc.RegisterToNameServerBlockingStub.class, RemoteConstant.NAMESERVER);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    RegisterCausa.ServerRegisterReporter build = RegisterCausa.ServerRegisterReporter.newBuilder()
                            .setServerIpAddress(ttcJobServerConfig.getAddress() + ":" + ttcJobServerConfig.getServerPort())
                            .build();
                    CommonCausa.Response response = stubSingleton.serverRegister(build);
                    log.info("server register to nameServer success");
                } catch (Exception e){
                    log.error("server register to nameServer error");
                }
            }
        }, 0, 10,TimeUnit.SECONDS);
    }
}
