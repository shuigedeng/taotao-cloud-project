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

package com.taotao.cloud.ccsr.example.web;
//
// import com.google.protobuf.Any;
// import com.google.protobuf.InvalidProtocolBufferException;
// import lombok.Getter;
// import org.ccsr.api.event.EventType;
// import org.ccsr.api.grpc.auto.Metadata;
// import org.ccsr.api.grpc.auto.MetadataReadRequest;
// import org.ccsr.api.grpc.auto.Response;
// import org.ccsr.common.utils.GsonUtils;
// import org.ccsr.dto.ServerAddress;
// import org.ccsr.request.Payload;
// import org.springframework.web.bind.annotation.*;
//
// import javax.annotation.Resource;
//
// @RestController
// @RequestMapping("/config")
// public class TestConfigController {
//
//    @Resource
//    private CcsrService ccsrService;
//
//    @GetMapping("/get")
//    public String get() {
//        Payload payload = Payload.builder().build();
//        payload.setConfigData(new ServerAddress("127.0.0.3", 8000, true));
//        payload.setNamespace("default");
//        payload.setGroup("default_group");
//        payload.setDataId("default_data_id");
//        Response response = ccsrService.request(payload, EventType.GET);
//        Any data = response.getData();
//        try {
//            Metadata metadata = data.unpack(Metadata.class);
//            return GsonUtils.getInstance().toJson(metadata);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @GetMapping("/put")
//    public String put(@RequestParam("port") String port) {
//        Payload payload = Payload.builder().build();
//        payload.setConfigData(new ServerAddress("127.0.0.5", Integer.parseInt(port), true));
//        payload.setNamespace("default");
//        payload.setGroup("default_group");
//        payload.setDataId("default_data_id");
//        Response response = ccsrService.request(payload, EventType.PUT);
//        Any data = response.getData();
//        try {
//            Metadata metadata = data.unpack(Metadata.class);
//            return GsonUtils.getInstance().toJson(metadata);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @RequestMapping("/delete")
//    public String delete() {
//        Payload payload = Payload.builder().build();
//        payload.setConfigData(new ServerAddress("127.0.0.1", 8000, true));
//        payload.setNamespace("default");
//        payload.setGroup("default_group");
//        payload.setDataId("default_data_id");
//        Response response = ccsrService.request(payload, EventType.DELETE);
//        Any data = response.getData();
//        try {
//            Metadata metadata = data.unpack(Metadata.class);
//            return GsonUtils.getInstance().toJson(metadata);
//        } catch (Exception e) {
//            return null;
//        }
//    }
// }
