package com.taotao.cloud.order.biz.api.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.web.vo.order.PaymentLogVO;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.order.biz.service.order.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,收款日志API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:22
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-支付日志管理API", description = "平台管理端-支付日志管理API")
@RequestMapping("/order/manager/payment/log")
public class PaymentLogController {

	private final IOrderService orderService;

	@Operation(summary = "分页获取支付日志", description = "分页获取支付日志")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	public Result<IPage<PaymentLogVO>> getByPage(Order order,
												 SearchVO searchVo) {
		ElasticsearchConfiguration elasticsearchConfiguration
		return Result.success(orderService.queryPaymentLogs(
			PageUtil.initPage(page), PageUtil.initWrapper(order, searchVo)));
	}
}
