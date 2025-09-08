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

package com.taotao.cloud.sys.biz.controller;

import com.taotao.boot.common.model.Result;
import com.taotao.boot.data.mybatis.interceptor.easylog.common.audit.DataAuditLogging;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.web.request.annotation.RequestLogger;
import com.taotao.cloud.sys.api.model.dto.LogQueryCriteria;
import com.taotao.cloud.sys.biz.model.entity.Log;
import com.taotao.cloud.sys.biz.service.ILogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * LogController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 08:51:32
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-日志管理API", description = "工具管理端-日志管理API")
@RequestMapping("/log")
public class ManagerLogController {

	private final ILogService logService;

	@Operation(summary = "导出数据", description = "导出数据")
	@RequestLogger("导出数据")
	@GetMapping(value = "/download")
	@PreAuthorize("@el.check('admin','log:list')")
	public void download(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
		criteria.setLogType("INFO");
		logService.download(logService.queryAll(criteria), response);
	}

	@Operation(summary = "导出错误数据", description = "导出错误数据")
	@RequestLogger("导出错误数据")
	@GetMapping(value = "/error/download")
	@PreAuthorize("@el.check('admin','log:list')")
	public void errorDownload(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
		criteria.setLogType("ERROR");
		logService.download(logService.queryAll(criteria), response);
	}

	@Operation(summary = "日志查询", description = "日志查询")
	@RequestLogger("日志查询")
	@GetMapping
	@PreAuthorize("@el.check('admin','log:list')")
	public Result<Object> getLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("INFO");
		criteria.setType(0);
		return Result.success(logService.queryAll(criteria, pageable));
	}

	@Operation(summary = "查询api日志", description = "查询api日志")
	@RequestLogger("查询api日志")
	@GetMapping(value = "/mlogs")
	@PreAuthorize("@el.check('admin','log:list')")
	public Result<Object> getApiLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("INFO");
		criteria.setType(1);
		return Result.success(logService.findAllByPageable(criteria.getBlurry(), pageable));
	}

	@Operation(summary = "用户日志查询", description = "用户日志查询")
	@RequestLogger("用户日志查询")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/user")
	public Result<Object> getUserLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("INFO");
		criteria.setBlurry(SecurityUtils.getUsername());
		return Result.success(logService.queryAllByUser(criteria, pageable));
	}

	@Operation(summary = "错误日志查询", description = "错误日志查询")
	@RequestLogger("错误日志查询")
	@GetMapping(value = "/error")
	public Result<Object> getErrorLogs(LogQueryCriteria criteria, Pageable pageable) {
		criteria.setLogType("ERROR");
		return Result.success(logService.queryAll(criteria, pageable));
	}

	@Operation(summary = "日志异常详情查询", description = "日志异常详情查询")
	@RequestLogger("日志异常详情查询")
	@GetMapping(value = "/error/{id}")
	@PreAuthorize("@el.check('admin','logError:detail')")
	public Result<Object> getErrorLogs(@PathVariable Long id) {
		return Result.success(logService.findByErrDetail(id));
	}

	@Operation(summary = "删除所有ERROR日志", description = "删除所有ERROR日志")
	@RequestLogger("删除所有ERROR日志")
	@DeleteMapping(value = "/error")
	@PreAuthorize("@el.check('admin','logError:remove')")
	public Result<Object> delAllByError() {
		logService.delAllByError();
		return Result.success(true);
	}

	@Operation(summary = "删除所有INFO日志", description = "删除所有INFO日志")
	@RequestLogger("删除所有INFO日志")
	@DeleteMapping(value = "/info")
	@PreAuthorize("@el.check('admin','logInfo:remove')")
	public Result<Boolean> delAllByInfo() {
		logService.delAllByInfo();
		return Result.success(true);
	}

	@PostMapping(value = "/testAdd")
	@NotAuth
	public Result<Boolean> testAdd(@RequestBody Log log) {
		boolean save = logService.save(log);
		return Result.success(save);
	}

	@PostMapping(value = "/testUpdate")
	@NotAuth
	@DataAuditLogging
	public Result<Boolean> testUpdate(@RequestBody Log log) {
		boolean save = logService.updateById(log);
		return Result.success(save);
	}

	@PostMapping(value = "/testQuery")
	@NotAuth
	public Result<Log> testQuery(@RequestParam Long id) {
		Log save = logService.getById(id);
		return Result.success(save);
	}
}
