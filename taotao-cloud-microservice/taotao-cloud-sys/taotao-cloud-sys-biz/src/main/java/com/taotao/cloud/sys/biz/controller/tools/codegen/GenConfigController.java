/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.sys.biz.controller.tools.codegen;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.biz.entity.config.GenConfig;
import com.taotao.cloud.sys.biz.service.IGenConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GenConfigController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 09:03:18
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-代码生成器配置管理API", description = "工具管理端-代码生成器配置管理API")
@RequestMapping("/sys/tools/codegen/generator/config")
public class GenConfigController {

	private final IGenConfigService genConfigService;

	@Operation(summary = "查询", description = "查询")
	@RequestLogger("查询")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{tableName}")
	public Result<GenConfig> get(@PathVariable String tableName) {
		return Result.success(genConfigService.find(tableName));
	}

	@Operation(summary = "修改", description = "修改")
	@RequestLogger("修改")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<GenConfig> emailConfig(@Validated @RequestBody GenConfig genConfig) {
		return Result.success(genConfigService.update(genConfig.getTableName(), genConfig));
	}
}
