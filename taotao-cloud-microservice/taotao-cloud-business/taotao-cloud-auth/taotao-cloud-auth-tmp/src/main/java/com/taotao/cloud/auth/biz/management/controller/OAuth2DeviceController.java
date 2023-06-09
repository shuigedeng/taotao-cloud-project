/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.cloud.auth.biz.management.entity.OAuth2Device;
import com.taotao.cloud.auth.biz.management.service.OAuth2DeviceService;
import com.taotao.cloud.common.model.Result;
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
 * <p>Description: OAuth2DeviceController </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/15 16:58
 */
@RestController
@RequestMapping("/authorize/device")
@Tags({
	@Tag(name = "OAuth2 认证服务接口"),
	@Tag(name = "物联网管理接口"),
	@Tag(name = "物联网设备接口")
})
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
	public Result<OAuth2Device> authorize(@RequestParam(name = "deviceId") String deviceId, @RequestParam(name = "scopes[]") String[] scopes) {
		OAuth2Device device = deviceService.authorize(deviceId, scopes);
		return Result.success(device);
	}
}
