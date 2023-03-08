package com.taotao.cloud.sys.biz.service.grpc;

@Service
@Slf4j
public class DeviceGrpcService {

	@GrpcClient("device-grpc-server")
	private Channel serverChannel;
 
	public String insertDeviceFix() {
		DeviceFixServiceGrpc.DeviceFixServiceBlockingStub stub = DeviceFixServiceGrpc.newBlockingStub(
			serverChannel);
		booleanReply response = stub.insertDeviceFix(
			deviceFix.newBuilder()
				.setId("UUID-O1")
				.setSerialNum("AUCCMA-01")
				.setAddress("SHENZHEN")
				.setCreatetime(DateUtil.toString(new Date(), DatePattern.TIMESTAMP))
				.setUpdatetime(DateUtil.toString(new Date(), DatePattern.TIMESTAMP))
				.setStatus(1)
				.setType(1)
				.build());
		log.info("grpc消费者收到：--》" + response.getReply());
		if (response.getReply()) {
			return "success";
		} else {
			return "fail";
		}
	}
}
