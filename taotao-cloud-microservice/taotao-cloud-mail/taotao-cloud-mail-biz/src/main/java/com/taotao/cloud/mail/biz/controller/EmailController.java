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
package com.taotao.cloud.mail.biz.controller;

import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.mail.api.vo.EmailVO;
import com.taotao.cloud.mail.biz.entity.Email;
import com.taotao.cloud.mail.biz.mapper.EmailMapper;
import com.taotao.cloud.mail.biz.service.IEmailService;
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
 * 物流公司管理API
 *
 * @author dengtao
 * @date 2020/11/13 09:58
 * @since v1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/email")
@Api(value = "物流公司管理API", tags = {"物流公司管理API"})
public class EmailController {

	private final IEmailService emailService;

	@ApiOperation("根据id查询邮件信息")
	@RequestOperateLog(description = "根据id查询邮件信息")
	@PreAuthorize("hasAuthority('email:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<EmailVO> findEmailById(@PathVariable(value = "id") Long id) {
		Email expressCompany = emailService.findEmailById(id);
		EmailVO vo = EmailMapper.INSTANCE.emailToEmailVO(expressCompany);
		return Result.succeed(vo);
	}

}
