/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.biz.entity.config.GenConfig;
import com.taotao.cloud.sys.biz.service.IGenConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Validated
@RestController
@Tag(name = "工具管理端-代码生成器配置管理API", description = "工具管理端-代码生成器配置管理API")
@RequestMapping("/sys/tools/generator/config")
public class GenConfigController {

	private final IGenConfigService IGenConfigService;

	public GenConfigController(IGenConfigService IGenConfigService) {
		this.IGenConfigService = IGenConfigService;
	}

	@Operation(summary = "查询", description = "查询", method = CommonConstant.GET)
	@RequestLogger(description = "查询")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{tableName}")
	public Result<GenConfig> get(@PathVariable String tableName) {
		return Result.success(IGenConfigService.find(tableName));
	}

	@Operation(summary = "修改", description = "修改", method = CommonConstant.PUT)
	@RequestLogger(description = "修改")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<GenConfig> emailConfig(@Validated @RequestBody GenConfig genConfig) {
		return Result.success(IGenConfigService.update(genConfig.getTableName(), genConfig));
	}
}
