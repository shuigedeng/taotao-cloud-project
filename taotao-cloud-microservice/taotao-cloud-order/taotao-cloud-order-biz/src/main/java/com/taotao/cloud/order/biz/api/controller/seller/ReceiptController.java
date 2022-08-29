package com.taotao.cloud.order.biz.api.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.web.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.web.query.order.ReceiptPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.Receipt;
import com.taotao.cloud.order.biz.service.order.IOrderService;
import com.taotao.cloud.order.biz.service.order.IReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,发票API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:45
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-发票API", description = "店铺端-发票API")
@RequestMapping("/order/seller/receipt")
public class ReceiptController {

	private final IReceiptService receiptService;

	private final IOrderService orderService;

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderReceiptDTO>> getByPage(ReceiptPageQuery receiptPageQuery) {
		receiptPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
		return Result.success(receiptService.getReceiptData(receiptPageQuery));
	}

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<Receipt> get(@PathVariable String id) {
		return Result.success(OperationalJudgment.judgment(receiptService.getById(id)));
	}

	@Operation(summary = "开发票", description = "开发票")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{id}/invoicing")
	public Result<Receipt> invoicing(@PathVariable Long id) {
		OperationalJudgment.judgment(receiptService.getById(id));
		return Result.success(receiptService.invoicing(id));
	}

	@Operation(summary = "通过订单编号获取", description = "通过订单编号获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/orderSn/{orderSn}")
	public Result<Receipt> getByOrderSn(@PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(receiptService.getByOrderSn(orderSn));
	}

}
