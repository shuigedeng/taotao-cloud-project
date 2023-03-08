package com.taotao.cloud.sys.biz.service.grpc;

import com.taotao.cloud.common.support.cron.util.DateUtil;
import com.taotao.cloud.sys.api.grpc.lib.DeviceFixServiceGrpc;
import com.taotao.cloud.sys.api.grpc.lib.booleanReply;
import com.taotao.cloud.sys.api.grpc.lib.deviceFix;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService
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
