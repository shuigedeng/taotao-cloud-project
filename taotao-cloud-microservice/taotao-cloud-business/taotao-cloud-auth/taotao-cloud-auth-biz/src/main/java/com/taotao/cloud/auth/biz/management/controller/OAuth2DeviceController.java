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

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Device;
import com.taotao.cloud.auth.biz.management.service.OAuth2DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>OAuth2DeviceController </p>
 *
 *
 * @since : 2023/5/15 16:58
 */
@RestController
@RequestMapping("/authorize/device")
@Tags({@Tag(name = "OAuth2 认证服务接口"), @Tag(name = "物联网管理接口"), @Tag(name = "物联网设备接口")})
public class OAuth2DeviceController {

    private final OAuth2DeviceService deviceService;

    public OAuth2DeviceController(OAuth2DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(summary = "给设备分配Scope", description = "给设备分配Scope")
    @Parameters({
        @Parameter(name = "deviceId", required = true, description = "设备ID"),
        @Parameter(name = "scopes[]", required = true, description = "Scope对象组成的数组")
    })
    @PutMapping
    public Result<OAuth2Device> authorize(
            @RequestParam(name = "deviceId") String deviceId,
            @RequestParam(name = "scopes[]") String[] scopes) {
        OAuth2Device device = deviceService.authorize(deviceId, scopes);
        return Result.success(device);
    }
}
