/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.dto.log.LogQueryCriteria;
import com.taotao.cloud.sys.biz.service.ILogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LogController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 08:51:32
 */
@Validated
@RestController
@Tag(name = "工具管理端-日志管理API", description = "工具管理端-日志管理API")
@RequestMapping("/sys/tools/logs")
public class LogController {

	private final ILogService logService;

	public LogController(ILogService logService) {
		this.logService = logService;
	}

	@Operation(summary = "导出数据", description = "导出数据", method = CommonConstant.GET)
	@RequestLogger(description = "导出数据")
	@GetMapping(value = "/download")
	@PreAuthorize("@el.check('admin','log:list')")
	public void download(HttpServletResponse response, LogQueryCriteria criteria)
		throws IOException {
		criteria.setLogType("INFO");
		logService.download(logService.queryAll(criteria), response);
	}

	@Operation(summary = "导出错误数据", description = "导出错误数据", method = CommonConstant.GET)
	@RequestLogger(description = "导出错误数据")
	@GetMapping(value = "/error/download")
	@PreAuthorize("@el.check('admin','log:list')")
	public void errorDownload(HttpServletResponse response, LogQueryCriteria criteria)
		throws IOException {
		criteria.setLogType("ERROR");
		logService.download(logService.queryAll(criteria), response);
	}

	@Operation(summary = "日志查询", description = "日志查询", method = CommonConstant.GET)
	@RequestLogger(description = "日志查询")
	@GetMapping
	@PreAuthorize("@el.check('admin','log:list')")
	public Result<Object> getLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("INFO");
		criteria.setType(0);
		return Result.success(logService.queryAll(criteria, pageable));
	}

	@Operation(summary = "查询api日志", description = "查询api日志", method = CommonConstant.GET)
	@RequestLogger(description = "查询api日志")
	@GetMapping(value = "/mlogs")
	@PreAuthorize("@el.check('admin','log:list')")
	public Result<Object> getApiLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("INFO");
		criteria.setType(1);
		return Result.success(logService.findAllByPageable(criteria.getBlurry(), pageable));
	}

	@Operation(summary = "用户日志查询", description = "用户日志查询", method = CommonConstant.GET)
	@RequestLogger(description = "用户日志查询")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/user")
	public Result<Object> getUserLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("INFO");
		criteria.setBlurry(SecurityUtil.getUsername());
		return Result.success(logService.queryAllByUser(criteria, pageable));
	}

	@Operation(summary = "错误日志查询", description = "错误日志查询", method = CommonConstant.GET)
	@RequestLogger(description = "错误日志查询")
	@GetMapping(value = "/error")
	public Result<Object> getErrorLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("ERROR");
		return Result.success(logService.queryAll(criteria, pageable));
	}

	@Operation(summary = "日志异常详情查询", description = "日志异常详情查询", method = CommonConstant.GET)
	@RequestLogger(description = "日志异常详情查询")
	@GetMapping(value = "/error/{id}")
	@PreAuthorize("@el.check('admin','logError:detail')")
	public Result<Object> getErrorLogs(@PathVariable Long id) {
		return Result.success(logService.findByErrDetail(id));
	}

	@Operation(summary = "删除所有ERROR日志", description = "删除所有ERROR日志", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除所有ERROR日志")
	@DeleteMapping(value = "/del/error")
	@PreAuthorize("@el.check('admin','logError:remove')")
	public Result<Object> delAllByError() {
		logService.delAllByError();
		return Result.success(true);
	}

	@Operation(summary = "删除所有INFO日志", description = "删除所有INFO日志", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除所有INFO日志")
	@DeleteMapping(value = "/del/info")
	@PreAuthorize("@el.check('admin','logInfo:remove')")
	public Result<Boolean> delAllByInfo() {
		logService.delAllByInfo();
		return Result.success(true);
	}
}
