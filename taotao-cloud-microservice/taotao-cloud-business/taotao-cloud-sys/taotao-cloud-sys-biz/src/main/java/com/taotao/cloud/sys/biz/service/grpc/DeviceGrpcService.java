package com.taotao.cloud.sys.biz.service.grpc;

import com.taotao.cloud.sys.api.grpc.BooleanReply;
import com.taotao.cloud.sys.api.grpc.MyServiceGrpc.MyServiceBlockingStub;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceGrpcService {

	@GrpcClient("myService")
	private MyServiceBlockingStub myServiceStub;

	public String insertDeviceFix() {
		//DeviceFixServiceGrpc.DeviceFixServiceBlockingStub stub = DeviceFixServiceGrpc.newBlockingStub(
		//	serverChannel);
		//booleanReply response = stub.insertDeviceFix(
		//	deviceFix.newBuilder()
		//		.setId("UUID-O1")
		//		.setSerialNum("AUCCMA-01")
		//		.setAddress("SHENZHEN")
		//		.setCreatetime(DateUtil.toString(new Date(), DatePattern.TIMESTAMP))
		//		.setUpdatetime(DateUtil.toString(new Date(), DatePattern.TIMESTAMP))
		//		.setStatus(1)
		//		.setType(1)
		//		.build());
		BooleanReply response = null;
		log.info("grpc消费者收到：--》" + response.getReply());
		if (response.getReply()) {
			return "success";
		} else {
			return "fail";
		}
	}
}
