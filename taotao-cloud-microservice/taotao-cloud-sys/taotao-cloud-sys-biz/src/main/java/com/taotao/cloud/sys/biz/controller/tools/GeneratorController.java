/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.biz.entity.config.ColumnConfig;
import com.taotao.cloud.sys.biz.service.IGenConfigService;
import com.taotao.cloud.sys.biz.service.IGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * GeneratorController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 08:59:11
 */
@Validated
@RestController
@Tag(name = "工具管理端-代码生成管理API", description = "工具管理端-代码生成管理API")
@RequestMapping("/sys/tools/generator")
public class GeneratorController {

	private final IGeneratorService IGeneratorService;

	private final IGenConfigService IGenConfigService;

	@Value("${generator.enabled:false}")
	private Boolean generatorEnabled;

	public GeneratorController(IGeneratorService IGeneratorService,
		IGenConfigService IGenConfigService) {
		this.IGeneratorService = IGeneratorService;
		this.IGenConfigService = IGenConfigService;
	}

	@Operation(summary = "查询数据库数据", description = "查询数据库数据", method = CommonConstant.GET)
	@RequestLogger(description = "查询数据库数据")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/tables/all")
	public Result<Object> getTables() {
		return Result.success(IGeneratorService.getTables());
	}

	@Operation(summary = "查询数据库数据", description = "查询数据库数据", method = CommonConstant.GET)
	@RequestLogger(description = "查询数据库数据")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/tables")
	public Result<Object> getTables(@RequestParam(defaultValue = "") String name,
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "10") Integer size) {
		return Result.success(IGeneratorService.getTables(name, page, size));
	}

	@Operation(summary = "查询字段数据", description = "查询字段数据", method = CommonConstant.GET)
	@RequestLogger(description = "查询字段数据")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/columns")
	public Result<List<ColumnConfig>> getTables(@RequestParam String tableName) {
		List<ColumnConfig> columnInfos = IGeneratorService.getColumns(tableName);
		return Result.success(columnInfos);
		//return new ResponseEntity<>(PageUtil.toPage(columnInfos, columnInfos.size()),
		//	HttpStatus.OK);
	}

	@PutMapping
	@Operation(summary = "保存字段数据", description = "保存字段数据", method = CommonConstant.PUT)
	@RequestLogger(description = "保存字段数据")
	@PreAuthorize("@el.check('admin','timing:list')")
	public Result<Boolean> save(@RequestBody List<ColumnConfig> columnInfos) {
		IGeneratorService.save(columnInfos);
		return Result.success(true);
	}

	@Operation(summary = "同步字段数据", description = "同步字段数据", method = CommonConstant.POST)
	@RequestLogger(description = "同步字段数据")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping(value = "/sync")
	public Result<Boolean> sync(@RequestBody List<String> tables) {
		for (String table : tables) {
			IGeneratorService.sync(IGeneratorService.getColumns(table),
				IGeneratorService.query(table));
		}
		return Result.success(true);
	}

	@Operation(summary = "生成代码", description = "生成代码", method = CommonConstant.POST)
	@RequestLogger(description = "生成代码")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping(value = "/{tableName}/{type}")
	public Result<Object> generator(@PathVariable String tableName,
		@PathVariable Integer type, HttpServletRequest request, HttpServletResponse response) {
		if (!generatorEnabled && type == 0) {
			throw new BusinessException("此环境不允许生成代码，请选择预览或者下载查看！");
		}
		switch (type) {
			// 生成代码
			case 0:
				IGeneratorService.generator(IGenConfigService.find(tableName),
					IGeneratorService.getColumns(tableName));
				break;
			// 预览
			case 1:
				//return generatorService.preview(genConfigService.find(tableName),
				//	generatorService.getColumns(tableName));
				// 打包
			case 2:
				IGeneratorService.download(IGenConfigService.find(tableName),
					IGeneratorService.getColumns(tableName), request, response);
				break;
			default:
				throw new BusinessException("没有这个选项");
		}
		return Result.success(true);
	}
}
