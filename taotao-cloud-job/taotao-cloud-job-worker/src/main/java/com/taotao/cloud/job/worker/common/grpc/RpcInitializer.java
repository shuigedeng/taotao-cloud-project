package com.taotao.cloud.job.worker.common.grpc;


import com.taotao.cloud.job.worker.common.KJobWorkerConfig;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.worker.common.grpc.strategies.GrpcStrategy;
import com.taotao.cloud.job.worker.common.grpc.strategies.StrategyManager;
import com.taotao.cloud.job.worker.service.WorkerScheduleGrpcService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
public class RpcInitializer {
	private final int serverPort;
	private final int workerPort;
	private List<String> serverList;
	@Getter
	private static String nameServerAddress;
	@Getter
	private static final HashMap<String, ManagedChannel> ip2ChannelsMap = new HashMap<>();

	public RpcInitializer(int serverPort, int workerPort, List<String> serverList, String nameServerAddress) {
		this.serverPort = serverPort;
		this.workerPort = workerPort;
		this.serverList = new ArrayList<>(serverList);
		RpcInitializer.nameServerAddress = nameServerAddress;
	}

	@SuppressWarnings("rawtypes")
	public void initRpcStrategies() {
		// register local available ip for channel(here set for local test)
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		String hostAddress = inetAddress.getHostAddress();
		serverList.add(hostAddress);

		// register channels for stub
		for (String server : serverList) {
			ManagedChannel channel = ManagedChannelBuilder.forAddress(server, serverPort)
				.usePlaintext()
				.build();
			ip2ChannelsMap.put(server, channel);
		}

		Reflections reflections = new Reflections("org.kjob.worker.common.grpc.strategies.strategy");
		Set<Class<? extends GrpcStrategy>> strategyClasses = reflections.getSubTypesOf(GrpcStrategy.class);

		for (Class<? extends GrpcStrategy> strategyClass : strategyClasses) {
			try {
				GrpcStrategy strategyInstance = strategyClass.getDeclaredConstructor().newInstance();
				TransportTypeEnum typeEnum = strategyInstance.getTypeEnumFromStrategyClass();
				strategyInstance.init();
				StrategyManager.registerCausa(typeEnum, strategyInstance);
			} catch (Exception e) {
				log.warn("creating strategy error");
			}
		}

	}

	public void initRpcServer(KJobWorkerConfig config) {
		new Thread(() -> {
			try {
				WorkerScheduleGrpcService myService = new WorkerScheduleGrpcService(config);

				Server server = ServerBuilder.forPort(workerPort)
					.addService(myService)
					.build()
					.start();

				log.info("GrpcServer started, listening on " + workerPort);

				// 等待服务器关闭
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					server.shutdown();
					System.out.println("Server stopped");
				}));

				server.awaitTermination();
			} catch (IOException | InterruptedException e) {
				log.error("GrpcServer started error");

			}
		}).start();
	}
}
