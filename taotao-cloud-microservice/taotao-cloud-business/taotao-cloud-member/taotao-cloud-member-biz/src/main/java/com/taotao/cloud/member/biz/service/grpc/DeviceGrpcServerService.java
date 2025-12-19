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

package com.taotao.cloud.member.biz.service.grpc;

import com.taotao.cloud.sys.api.grpc.BooleanReply;
import com.taotao.cloud.sys.api.grpc.DeviceFix;
import com.taotao.cloud.sys.api.grpc.DeviceFixServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * DeviceGrpcServerService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
@GrpcService
public class DeviceGrpcServerService extends DeviceFixServiceGrpc.DeviceFixServiceImplBase {

    @Override
    public void insertDeviceFix( DeviceFix request, StreamObserver<BooleanReply> responseObserver ) {
        // DevicesFix deviceFix = DevicesFix.builder().id(request.getId())
        //	.serialNum(request.getSerialNum())
        //	.address(request.getAddress())
        //	.createtime(DateUtil.toDate(request.getCreatetime(), DatePattern.TIMESTAMP))
        //	.updatetime(DateUtil.toDate(request.getUpdatetime(), DatePattern.TIMESTAMP))
        //	.userNum(request.getUserNum())
        //	.status(request.getStatus())
        //	.type(request.getType())
        //	.build();
        // log.info(deviceFix.toString());
        // boolean replyTag = deviceService.insert(deviceFix);
        boolean replyTag = false;
        BooleanReply reply = BooleanReply.newBuilder().setReply(replyTag).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
