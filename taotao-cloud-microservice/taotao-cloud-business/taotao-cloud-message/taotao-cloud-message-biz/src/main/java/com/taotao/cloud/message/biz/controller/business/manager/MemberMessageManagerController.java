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

package com.taotao.cloud.message.biz.controller.business.manager; // package
                                                                  // com.taotao.cloud.message.biz.controller.manager;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.cloud.common.constant.CommonConstant;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.logger.annotation.RequestLogger;
// import com.taotao.cloud.message.api.vo.MemberMessageQueryVO;
// import com.taotao.cloud.message.biz.entity.MemberMessage;
// import com.taotao.cloud.message.biz.service.MemberMessageService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
//
// /**
//  * 管理端,会员消息消息管理接口
//  */
// @Validated
// @RestController
// @Tag(name = "管理端-会员消息消息管理API", description = "管理端-会员消息消息管理API")
// @RequestMapping("/message/manager/memberMessage")
// public class MemberMessageManagerController {
//
// 	@Autowired
// 	private MemberMessageService memberMessageService;
//
// 	@Operation(summary = "多条件分页获取", description = "多条件分页获取")
// 	@RequestLogger
// 	@PreAuthorize("hasAuthority('dept:tree:data')")
// 	@GetMapping("/page")
// 	public Result<IPage<MemberMessage>> getByCondition(
// 		MemberMessageQueryVO memberMessageQueryVO, PageVO pageVo) {
// 		IPage<MemberMessage> page = memberMessageService.getPage(memberMessageQueryVO, pageVo);
// 		return Result.success(page);
// 	}
//
// }
