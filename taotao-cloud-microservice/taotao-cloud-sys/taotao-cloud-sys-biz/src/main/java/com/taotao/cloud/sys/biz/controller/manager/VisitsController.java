package com.taotao.cloud.sys.biz.controller.manager;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.servlet.RequestUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.biz.service.IVisitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VisitsController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:26:45
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-访问记录管理API", description = "工具管理端-访问记录管理API")
@RequestMapping("/sys/tools/visits")
public class VisitsController {

	private final IVisitsService visitsService;

	@Operation(summary = "创建访问记录", description = "创建访问记录", method = CommonConstant.POST)
	@RequestLogger("创建访问记录")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> create() {
		visitsService.count(RequestUtil.getHttpServletRequest());
		return Result.success(true);
	}

	@Operation(summary = "查询访问记录", description = "查询访问记录", method = CommonConstant.GET)
	@RequestLogger("查询访问记录")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<Object> get() {
		return Result.success(visitsService.get());
	}

	@Operation(summary = "查询图表数据", description = "查询图表数据", method = CommonConstant.GET)
	@RequestLogger("查询图表数据")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/chartData")
	public Result<Object> getChartData() {
		return Result.success(visitsService.getChartData());
	}
}
