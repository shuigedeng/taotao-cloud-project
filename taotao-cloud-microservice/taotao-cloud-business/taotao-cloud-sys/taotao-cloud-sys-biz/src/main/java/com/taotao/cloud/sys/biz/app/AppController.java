/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.app;

import com.art.common.core.util.ValidationUtil;
import com.art.common.core.constant.ValidationGroup;
import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.system.api.app.dto.AppDTO;
import com.art.system.api.app.dto.AppPageDTO;
import com.art.system.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统应用表
 *
 * @author fxz
 * @date 2022-09-12
 */
@Tag(name = "应用管理")
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppController {

	private final AppService appService;

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<AppDTO>> pageSysApp(AppPageDTO appPageDTO) {
		ValidationUtil.validateParam(appPageDTO, ValidationGroup.query.class);
		return Result.success(PageResult.success(appService.pageApp(appPageDTO)));
	}

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	public Result<Void> add(@RequestBody AppDTO appDTO) {
		ValidationUtil.validateParam(appDTO, ValidationGroup.add.class);
		return Result.judge(appService.addApp(appDTO));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Void> update(@RequestBody AppDTO appDTO) {
		ValidationUtil.validateParam(appDTO, ValidationGroup.update.class);
		return Result.judge(appService.updateApp(appDTO));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	public Result<Void> delete(Long id) {
		return Result.judge(appService.deleteApp(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<AppDTO> findById(Long id) {
		return Result.success(appService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<AppDTO>> findAll() {
		return Result.success(appService.findAll());
	}

}
