/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.pay.alipay.alipay.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.taotao.cloud.pay.alipay.alipay.definition.AlipayPaymentTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 支付宝异步通知、返回接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/9 11:41
 */
@RestController
@RequestMapping("/open/pay/alipay")
@Tags({@Tag(name = "支付接口"), @Tag(name = "支付宝支付接口"),
	@Tag(name = "支付宝异步通知返回接口")})
public class AlipayFeedbackController {

	@Autowired
	private AlipayPaymentTemplate alipayPaymentTemplate;

	@Operation(summary = "支付宝通知", description = "获取支付宝POST过来反馈信息",
		responses = {
			@ApiResponse(description = "单位树", content = @Content(mediaType = "application/json"))})
	@Parameters({
		@Parameter(name = "httpServletRequest", description = "标准请求对象实体HttpServletRequest", schema = @Schema(implementation = HttpServletRequest.class)),
	})
	@PostMapping("/notify")
	public void paymentNotify(HttpServletRequest httpServletRequest) {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = ServletUtil.getParamMap(httpServletRequest);
		alipayPaymentTemplate.paymentNotify(params);
	}

	@Operation(summary = "支付宝返回", description = "获取支付宝POST过来反馈信息",
		responses = {
			@ApiResponse(description = "单位树", content = @Content(mediaType = "application/json"))})
	@Parameters({
		@Parameter(name = "httpServletRequest", description = "标准请求对象实体HttpServletRequest", schema = @Schema(implementation = HttpServletRequest.class)),
	})
	@GetMapping("/return")
	public void paymentReturn(HttpServletRequest request) {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = ServletUtil.getParamMap(request);
		alipayPaymentTemplate.paymentReturn(params);
	}

}
