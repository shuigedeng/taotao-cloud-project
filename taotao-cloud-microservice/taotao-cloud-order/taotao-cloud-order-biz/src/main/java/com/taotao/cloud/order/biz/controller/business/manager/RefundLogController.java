package com.taotao.cloud.order.biz.controller.business.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,退款日志API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:27
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-退款日志管理API", description = "平台管理端-退款日志管理API")
@RequestMapping("/order/manager/refund/log")
public class RefundLogController {

	private final RefundLogService refundLogService;

	@Operation(summary = "查看退款日志详情", description = "查看退款日志详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/{id}")
	public Result<RefundLog> get(@PathVariable String id) {
		return Result.success(refundLogService.getById(id));
	}

	@Operation(summary = "分页获取退款日志", description = "分页获取退款日志")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	public Result<IPage<RefundLog>> getByPage(RefundLog entity, SearchVO searchVo,
		PageVO page) {
		return Result.success(refundLogService.page(
			PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo)));
	}

}
