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
import com.taotao.cloud.sys.api.grpc.MyServiceGrpc.MyServiceBlockingStub;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * DeviceGrpcService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Service
@Slf4j
public class DeviceGrpcService {

    @GrpcClient("myService")
    private MyServiceBlockingStub myServiceStub;

    public String insertDeviceFix() {
        // DeviceFixServiceGrpc.DeviceFixServiceBlockingStub stub =
        // DeviceFixServiceGrpc.newBlockingStub(
        //	serverChannel);
        // booleanReply response = stub.insertDeviceFix(
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
