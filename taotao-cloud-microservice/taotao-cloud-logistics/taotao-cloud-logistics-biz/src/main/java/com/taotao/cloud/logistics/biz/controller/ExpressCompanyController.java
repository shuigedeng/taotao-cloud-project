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
package com.taotao.cloud.logistics.biz.controller;

import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.logistics.api.vo.ExpressCompanyVO;
import com.taotao.cloud.logistics.biz.entity.ExpressCompany;
import com.taotao.cloud.logistics.biz.mapper.ExpressCompanyMapper;
import com.taotao.cloud.logistics.biz.service.IExpressCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物流公司管理API
 *
 * @author shuigedeng
 * @since 2020/11/13 09:58
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping("/express/company")
@Api(value = "物流公司管理API", tags = {"物流公司管理API"})
public class ExpressCompanyController {

	private final IExpressCompanyService expressCompanyService;

	public ExpressCompanyController(
		IExpressCompanyService expressCompanyService) {
		this.expressCompanyService = expressCompanyService;
	}

	@ApiOperation("根据id查询物流公司信息")
	@RequestOperateLog(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('express:company:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<ExpressCompanyVO> findExpressCompanyById(@PathVariable(value = "id") Long id) {
		ExpressCompany expressCompany = expressCompanyService.findExpressCompanyById(id);
		ExpressCompanyVO vo = ExpressCompanyMapper.INSTANCE.expressCompanyToExpressCompanyVO(expressCompany);
		return Result.success(vo);
	}

}
