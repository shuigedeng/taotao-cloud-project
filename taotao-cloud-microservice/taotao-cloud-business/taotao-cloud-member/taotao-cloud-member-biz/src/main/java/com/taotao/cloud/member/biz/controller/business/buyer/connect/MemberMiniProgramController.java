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

package com.taotao.cloud.member.biz.controller.business.buyer.connect; // package
                                                                       // com.taotao.cloud.member.biz.controller.buyer.connect;
//
// import com.taotao.cloud.common.constant.CommonConstant;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.logger.annotation.RequestLogger;
// import com.taotao.cloud.member.biz.connect.entity.dto.WechatMPLoginParams;
// import com.taotao.cloud.member.biz.connect.service.ConnectService;
// import com.taotao.cloud.member.biz.connect.token.Token;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import java.util.List;
// import lombok.AllArgsConstructor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * 买家端,小程序登录API
//  */
// @AllArgsConstructor
// @Validated
// @RestController
// @Tag(name = "买家端-会员小程序登录API", description = "买家端-会员小程序登录API")
// @RequestMapping("/member/buyer/passport/connect/miniProgram")
// public class MemberMiniProgramController {
//
// 	public final ConnectService connectService;
// 	public final WechatMpCodeUtil wechatMpCodeUtil;
// 	public final WechatMPMessageService wechatMPMessageService;
// 	public final ShortLinkService shortLinkService;
//
// 	@Operation(summary = "小程序自动登录", description = "小程序自动登录")
// 	@RequestLogger("小程序自动登录")
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping("/auto-login")
// 	public Result<Token> autoLogin(@RequestHeader String uuid, WechatMPLoginParams params) {
// 		params.setUuid(uuid);
// 		return Result.success(this.connectService.miniProgramAutoLogin(params));
// 	}
//
// 	@Operation(summary = "消息订阅", description = "消息订阅")
// 	@RequestLogger("消息订阅")
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping("/subscribe/message")
// 	public Result<List<WechatMPMessage>> autoLogin() {
// 		return Result.success(wechatMPMessageService.list());
// 	}
//
// 	@Operation(summary = "小程序二维码生成：不限制数量，但是限制长度，只能存放32为长度", description =
// "小程序二维码生成：不限制数量，但是限制长度，只能存放32为长度")
// 	@RequestLogger("小程序二维码生成：不限制数量，但是限制长度，只能存放32为长度")
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping("/mp/unlimited")
// 	public Result<String> unlimited(String page, String scene) {
// 		return Result.success(wechatMpCodeUtil.createCode(page, scene));
// 	}
//
// 	@Operation(summary = "小程序二维码生成:只适用于少量场景，多数场景需要unlimitedAPI实现", description =
// "小程序二维码生成:只适用于少量场景，多数场景需要unlimitedAPI实现")
// 	@RequestLogger("小程序二维码生成:只适用于少量场景，多数场景需要unlimitedAPI实现")
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping("/mp/qrcode")
// 	public Result<String> qrcode(String page) {
// 		return Result.success(wechatMpCodeUtil.createQrCode(page));
// 	}
//
// 	@Operation(summary = "根据shortlink获取页面参数", description = "根据shortlink获取页面参数")
// 	@RequestLogger("根据shortlink获取页面参数")
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping("/mp/unlimited/scene")
// 	public Result<String> getScene(String id) {
// 		return Result.success(shortLinkService.getById(id).getOriginalParams());
// 	}
//
// }
