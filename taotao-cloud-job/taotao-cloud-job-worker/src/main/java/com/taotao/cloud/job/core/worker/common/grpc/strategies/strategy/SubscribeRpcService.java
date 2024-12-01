package com.taotao.cloud.job.core.worker.common.grpc.strategies.strategy;

import com.taotao.cloud.job.common.utils.net.MyNetUtil;
import com.taotao.cloud.job.core.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.core.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.core.worker.common.grpc.strategies.GrpcStrategy;
import com.taotao.cloud.job.core.worker.subscribe.WorkerSubscribeManager;
import com.taotao.cloud.remote.api.RegisterToNameServerGrpc;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.RegisterCausa;
import com.taotao.cloud.remote.protos.ServerDiscoverCausa;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class SubscribeRpcService implements GrpcStrategy<TransportTypeEnum> {

	RegisterToNameServerGrpc.RegisterToNameServerBlockingStub stub;
	HashMap<String, ServerDiscoverGrpc.ServerDiscoverBlockingStub> ip2serverDiscoverStubs = new HashMap<>();

	@Override
	public void init() {
		String nameServerAddress = RpcInitializer.getNameServerAddress();
		ManagedChannel channel = ManagedChannelBuilder.forAddress(nameServerAddress.split(":")[0], Integer.parseInt(nameServerAddress.split(":")[1]))
			.usePlaintext()
			.build();
		stub = RegisterToNameServerGrpc.newBlockingStub(channel);

		HashMap<String, ManagedChannel> ip2ChannelsMap = RpcInitializer.getIp2ChannelsMap();
		for (String ip : ip2ChannelsMap.keySet()) {
			ip2serverDiscoverStubs.put(ip, ServerDiscoverGrpc.newBlockingStub(ip2ChannelsMap.get(ip)));
		}
	}

	@Override
	public Object execute(Object params) {
		RegisterCausa.WorkerSubscribeReq workerSubscribeReq = (RegisterCausa.WorkerSubscribeReq) params;
		// add serverIp and scheduleTime to req
		RegisterCausa.WorkerSubscribeReq build = RegisterCausa.WorkerSubscribeReq.newBuilder()
			.setAppName(workerSubscribeReq.getAppName())
			.setServerIpAddress(WorkerSubscribeManager.getCurrentServerIp())
			.setScheduleTime(WorkerSubscribeManager.getScheduleTimes().get())
			.setWorkerIpAddress(MyNetUtil.address).build();
		CommonCausa.Response response = stub.workerSubscribe(build);
		RegisterCausa.WorkerSubscribeResponse workerSubscribeResponse = response.getWorkerSubscribeResponse();
		// 标记需要分组, assertApp时会根据标记发起分组请求
		if (workerSubscribeResponse.getIsSplit()) {
			WorkerSubscribeManager.setSplitStatus(true);
			WorkerSubscribeManager.setSubAppName(response.getWorkerSubscribeResponse().getSubAppName());
		}

		// 需要更换server, 发起更换Server请求
		if (workerSubscribeResponse.getIsChangeServer()) {
			ServerDiscoverCausa.ServerChangeReq build1 = ServerDiscoverCausa.ServerChangeReq.newBuilder()
				.setAppName(workerSubscribeReq.getAppName())
				.setTargetServer(workerSubscribeResponse.getServerAddressIpListsList().get(0))
				.build();
			ServerDiscoverGrpc.ServerDiscoverBlockingStub stub1 = ip2serverDiscoverStubs.get(build.getServerIpAddress());
			CommonCausa.Response response1 = stub1.serverChange(build1);
		}

		WorkerSubscribeManager.setServerIpList(response.getWorkerSubscribeResponse().getServerAddressIpListsList());
		log.info("[TtcJobSubscribeService] subscribe success, schedule :{} Times in this interval", WorkerSubscribeManager.getScheduleTimes());


		// 重置调度时间
		WorkerSubscribeManager.resetScheduleTimes();
		return null;
	}

	@Override
	public TransportTypeEnum getTypeEnumFromStrategyClass() {
		return TransportTypeEnum.REGISTER_TO_NAMESERVER;
	}
}
