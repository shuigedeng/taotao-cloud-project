package com.taotao.cloud.sys.biz.service.grpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService(DeviceFixServiceGrpc.class)
public class DeviceGrpcServerService extends DeviceFixServiceGrpc.DeviceFixServiceImplBase {

	@Autowired
	private IDevicesFixService deviceService;

	@Override
	public void insertDeviceFix(deviceFix request, StreamObserver<booleanReply> responseObserver) {
		DevicesFix deviceFix = DevicesFix.builder().id(request.getId())
			.serialNum(request.getSerialNum())
			.address(request.getAddress())
			.createtime(DateUtil.toDate(request.getCreatetime(), DatePattern.TIMESTAMP))
			.updatetime(DateUtil.toDate(request.getUpdatetime(), DatePattern.TIMESTAMP))
			.userNum(request.getUserNum())
			.status(request.getStatus())
			.type(request.getType())
			.build();
		log.info(deviceFix.toString());
		boolean replyTag = deviceService.insert(deviceFix);
		booleanReply reply = booleanReply.newBuilder().setReply(replyTag).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}
