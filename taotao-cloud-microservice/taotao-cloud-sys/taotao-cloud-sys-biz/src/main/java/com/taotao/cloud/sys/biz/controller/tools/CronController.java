package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.biz.service.ICronService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CronController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:48:47
 */
@Validated
@RestController
@Tag(name = "工具管理-cron管理API", description = "工具管理-cron管理API")
@RequestMapping("/sys/tools/cron")
public class CronController {

	@Autowired
	private ICronService cronService;

	@Operation(summary = "计算cron表达式下次执行时间 计算的后面所有执行时间", description = "计算cron表达式下次执行时间 计算的后面所有执行时间", method = CommonConstant.GET)
	@RequestLogger(description = "计算cron表达式下次执行时间 计算的后面所有执行时间")
	@GetMapping("/next-execution-time")
	public Result<List<String>> cronNextExecutionTime(
		@Parameter(description = "cron 表达式", required = true) @NotBlank String expression) {
		return Result.success(cronService.cronNextExecutionTime(expression));
	}
}
