package com.taotao.cloud.job.core.worker.common.grpc.strategies.strategy;

import com.taotao.cloud.job.core.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.core.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.core.worker.common.grpc.strategies.GrpcStrategy;
import com.taotao.cloud.remote.api.ScheduleGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ScheduleCausa;
import io.grpc.ManagedChannel;

import java.util.HashMap;

public class HeartHealthReportRpcService implements GrpcStrategy<TransportTypeEnum> {
	HashMap<String, ScheduleGrpc.ScheduleBlockingStub> ip2Stubs = new HashMap<>();

	@Override
	public void init() {
		HashMap<String, ManagedChannel> ip2ChannelsMap = RpcInitializer.getIp2ChannelsMap();
		for (String ip : ip2ChannelsMap.keySet()) {
			ip2Stubs.put(ip, ScheduleGrpc.newBlockingStub(ip2ChannelsMap.get(ip)));
		}
	}

	@Override
	public Object execute(Object params) {
		ScheduleCausa.WorkerHeartbeat workerHeartbeat = (ScheduleCausa.WorkerHeartbeat) params;
		ScheduleGrpc.ScheduleBlockingStub stub = ip2Stubs.get(workerHeartbeat.getServerIpAddress());
		CommonCausa.Response response = stub.reportWorkerHeartbeat(workerHeartbeat);
		return null;
	}

	@Override
	public TransportTypeEnum getTypeEnumFromStrategyClass() {
		return TransportTypeEnum.HEARTBEAT_HEALTH_REPORT;
	}
}
