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

import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.data.mybatis.mybatisplus.interceptor.datachanage.service.DataVersionLogService;
import com.taotao.cloud.sys.biz.model.dto.DataVersionLogDto;
import com.taotao.cloud.sys.biz.model.param.DataVersionLogParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuigedeng
 * @since 2022/1/10
 */
@Tag(name = "数据版本日志")
@RestController
@RequestMapping("/log/dataVersion")
@RequiredArgsConstructor
public class DataVersionLogController {
	private final DataVersionLogService service;

	@Operation(summary = "分页")
	@GetMapping("/page")
	public Result<PageResult<DataVersionLogDto>> page(DataVersionLogParam param) {
		return Result.success(service.page(param));
	}

	@Operation(summary = "获取")
	@GetMapping("/findById")
	public Result<DataVersionLogDto> findById(Long id) {
		return Result.success(service.findById(id));
	}
}
