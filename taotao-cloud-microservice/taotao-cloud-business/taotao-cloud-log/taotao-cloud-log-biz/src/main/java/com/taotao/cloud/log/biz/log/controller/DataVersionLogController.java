package com.taotao.cloud.log.biz.log.controller;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.biz.log.dto.DataVersionLogDto;
import com.taotao.cloud.log.biz.log.param.DataVersionLogParam;
import com.taotao.cloud.log.biz.log.service.DataVersionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuigedeng
 * @date 2022/1/10
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
