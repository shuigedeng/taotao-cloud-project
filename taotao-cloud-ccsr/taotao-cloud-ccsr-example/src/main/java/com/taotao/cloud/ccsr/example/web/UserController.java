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

import com.google.protobuf.Any;
import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.client.request.Payload;
import com.taotao.cloud.ccsr.client.starter.CcsrService;
import com.taotao.cloud.ccsr.common.utils.GsonUtils;
import com.taotao.cloud.ccsr.example.dto.User;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/config")
public class UserController {

    @Resource private CcsrService ccsrService;

    @GetMapping("/get")
    public String get() {
        Payload payload = Payload.builder().build();
        payload.setNamespace("default");
        payload.setGroup("default_group");
        payload.setTag("default_tag");
        payload.setDataId(
                "default#default_group#default_tag#com.taotao.cloud.ccsr.example.dto.User");
        Response response = ccsrService.request(payload, EventType.GET);
        Any data = response.getData();
        try {
            Metadata metadata = data.unpack(Metadata.class);
            return GsonUtils.getInstance().toJson(metadata);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/put")
    public String put(@RequestParam("name") String name, @RequestParam("age") String age) {
        Payload payload = Payload.builder().build();
        payload.setConfigData(new User(name, Integer.parseInt(age)));
        payload.setNamespace("default");
        payload.setGroup("default_group");
        payload.setTag("default_tag");
        // data_id 数据唯一表示
        payload.setDataId(
                "default#default_group#default_tag#com.taotao.cloud.ccsr.example.dto.User");
        Response response = ccsrService.request(payload, EventType.PUT);
        Any data = response.getData();
        try {
            Metadata metadata = data.unpack(Metadata.class);
            return GsonUtils.getInstance().toJson(metadata);
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/delete")
    public String delete() {
        Payload payload = Payload.builder().build();
        payload.setNamespace("default");
        payload.setGroup("default_group");
        payload.setTag("default_tag");
        payload.setDataId(
                "default#default_group#default_tag#com.taotao.cloud.ccsr.example.dto.User");
        Response response = ccsrService.request(payload, EventType.DELETE);
        Any data = response.getData();
        try {
            Metadata metadata = data.unpack(Metadata.class);
            return GsonUtils.getInstance().toJson(metadata);
        } catch (Exception e) {
            return null;
        }
    }
}
