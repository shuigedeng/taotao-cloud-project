/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.log.biz.controller;

import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.log.api.vo.MemberLoginVO;
import com.taotao.cloud.log.biz.entity.MemberLogin;
import com.taotao.cloud.log.biz.mapper.MemberLoginMapper;
import com.taotao.cloud.log.biz.service.IMemberLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机器人客服管理API
 *
 * @author dengtao
 * @since 2020/11/13 09:58
 * @version 1.0.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/member/login")
@Api(value = "会员登录日志管理API", tags = {"会员登录日志管理API"})
public class MemberLoginController {

	private final IMemberLoginService memberLoginService;

	@ApiOperation("根据id查询会员登录日志信息")
	@RequestOperateLog(description = "根据id查询会员登录日志信息")
	@PreAuthorize("hasAuthority('member:login:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<MemberLoginVO> findMemberLoginById(@PathVariable(value = "id") Long id) {
		MemberLogin memberLogin = memberLoginService.findMemberLoginById(id);
		MemberLoginVO vo = MemberLoginMapper.INSTANCE.memberLoginToMemberLoginVO(memberLogin);
		return Result.succeed(vo);
	}

}
