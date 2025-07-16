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

package com.taotao.cloud.job.server.register;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.RegisterCausa;
import com.taotao.cloud.job.server.common.config.TtcJobServerConfig;
import com.taotao.cloud.job.server.extension.singletonpool.GrpcStubSingletonPool;
import com.taotao.cloud.remote.api.RegisterToNameServerGrpc;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServerRegisterStarter implements InitializingBean {

    @Autowired TtcJobServerConfig ttcJobServerConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadFactory registerThreadFactory =
                new ThreadFactoryBuilder().setNameFormat("TtcJob-server-register-%d").build();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(3, registerThreadFactory);

        // get stub
        String s = ttcJobServerConfig.getNameServerAddress().split(":")[0];
        RegisterToNameServerGrpc.RegisterToNameServerBlockingStub stubSingleton =
                GrpcStubSingletonPool.getStubSingleton(
                        s,
                        RegisterToNameServerGrpc.class,
                        RegisterToNameServerGrpc.RegisterToNameServerBlockingStub.class,
                        RemoteConstant.NAMESERVER);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RegisterCausa.ServerRegisterReporter build =
                                    RegisterCausa.ServerRegisterReporter.newBuilder()
                                            .setServerIpAddress(
                                                    ttcJobServerConfig.getAddress()
                                                            + ":"
                                                            + ttcJobServerConfig.getServerPort())
                                            .setRegisterTimestamp(System.currentTimeMillis())
                                            .build();
                            CommonCausa.Response response = stubSingleton.serverRegister(build);
                            log.info("server register to nameServer success");
                        } catch (Exception e) {
                            log.error("server register to nameServer error");
                        }
                    }
                },
                0,
                10,
                TimeUnit.SECONDS);
    }
}
