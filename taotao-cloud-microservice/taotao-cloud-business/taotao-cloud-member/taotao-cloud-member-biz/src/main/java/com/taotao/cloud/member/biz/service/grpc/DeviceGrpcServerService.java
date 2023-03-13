package com.taotao.cloud.member.biz.service.grpc;

import com.taotao.cloud.sys.api.grpc.BooleanReply;
import com.taotao.cloud.sys.api.grpc.DeviceFix;
import com.taotao.cloud.sys.api.grpc.DeviceFixServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class DeviceGrpcServerService extends DeviceFixServiceGrpc.DeviceFixServiceImplBase {

	//@Autowired
	//private IDevicesFixService deviceService;

	@Override
	public void insertDeviceFix(DeviceFix request, StreamObserver<BooleanReply> responseObserver) {
		//DevicesFix deviceFix = DevicesFix.builder().id(request.getId())
		//	.serialNum(request.getSerialNum())
		//	.address(request.getAddress())
		//	.createtime(DateUtil.toDate(request.getCreatetime(), DatePattern.TIMESTAMP))
		//	.updatetime(DateUtil.toDate(request.getUpdatetime(), DatePattern.TIMESTAMP))
		//	.userNum(request.getUserNum())
		//	.status(request.getStatus())
		//	.type(request.getType())
		//	.build();
		//log.info(deviceFix.toString());
		//boolean replyTag = deviceService.insert(deviceFix);
		boolean replyTag = false;
		BooleanReply reply = BooleanReply.newBuilder().setReply(replyTag).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}
}
