package com.taotao.cloud.log.biz.log.controller;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatisplus.pagehelper.PageParam;
import com.taotao.cloud.log.biz.log.dto.LoginLogDto;
import com.taotao.cloud.log.biz.log.param.LoginLogParam;
import com.taotao.cloud.log.biz.log.service.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuigedeng
 * @date 2021/9/7
 */
@Tag(name = "登录日志")
@RestController
@RequestMapping("/log/login")
@RequiredArgsConstructor
public class LoginLogController {
	private final LoginLogService loginLogService;

	@Operation(summary = "分页")
	@GetMapping("/page")
	public Result<PageResult<LoginLogDto>> page(LoginLogParam loginLogParam) {
		return Result.success(loginLogService.page (loginLogParam));
	}

	@Operation(summary = "获取")
	@GetMapping("/findById")
	public Result<LoginLogDto> findById(Long id) {
		return Result.success(loginLogService.findById(id));
	}
}
