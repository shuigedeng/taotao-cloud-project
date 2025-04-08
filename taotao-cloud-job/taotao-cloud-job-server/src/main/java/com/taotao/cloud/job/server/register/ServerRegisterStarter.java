package com.taotao.cloud.job.server.register;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.RegisterCausa;
import com.taotao.cloud.job.server.common.config.TtcJobServerConfig;
import com.taotao.cloud.job.server.extension.singletonpool.GrpcStubSingletonPool;
import com.taotao.cloud.remote.api.RegisterToNameServerGrpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ServerRegisterStarter implements InitializingBean {

	@Autowired
    TtcJobServerConfig ttcJobServerConfig;

	@Override
	public void afterPropertiesSet() throws Exception {
		ThreadFactory registerThreadFactory = new ThreadFactoryBuilder().setNameFormat("TtcJob-server-register-%d").build();
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
						.setRegisterTimestamp(System.currentTimeMillis())
						.build();
					CommonCausa.Response response = stubSingleton.serverRegister(build);
					log.info("server register to nameServer success");
				} catch (Exception e) {
					log.error("server register to nameServer error");
				}
			}
		}, 0, 10, TimeUnit.SECONDS);
	}
}
