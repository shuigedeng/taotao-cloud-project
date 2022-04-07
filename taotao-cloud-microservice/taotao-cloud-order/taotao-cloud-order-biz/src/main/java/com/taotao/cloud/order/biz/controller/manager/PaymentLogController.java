package com.taotao.cloud.order.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.vo.order.PaymentLog;
import com.taotao.cloud.order.biz.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,收款日志接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-收款日志管理API", description = "平台管理端-收款日志管理API")
@RequestMapping("/order/manager/paymentLog")
public class PaymentLogController {

	@Autowired
	private OrderService orderService;

	@Operation(summary = "分页获取支付日志", description = "分页获取支付日志", method = CommonConstant.GET)
	@RequestLogger("分页获取支付日志")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	public Result<IPage<PaymentLog>> getByPage(Order order,
		SearchVO searchVo,
		PageVO page) {
		return Result.success(orderService.queryPaymentLogs(
			PageUtil.initPage(page), PageUtil.initWrapper(order, searchVo)));
	}
}
