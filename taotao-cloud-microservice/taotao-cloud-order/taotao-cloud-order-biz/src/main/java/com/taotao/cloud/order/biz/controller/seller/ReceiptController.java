package com.taotao.cloud.order.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.dto.order.ReceiptSearchParams;
import com.taotao.cloud.order.biz.entity.order.Receipt;
import com.taotao.cloud.order.biz.service.order.OrderService;
import com.taotao.cloud.order.biz.service.order.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;

import lombok.AllArgsConstructor;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,发票API
 **/
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-发票API", description = "店铺端-发票API")
@RequestMapping("/order/seller/receipt")
public class ReceiptController {

	private final ReceiptService receiptService;

	private final OrderService orderService;

	@Operation(summary = "分页获取", description = "分页获取", method = CommonConstant.GET)
	@RequestLogger("分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderReceiptDTO>> getByPage(PageVO page,
		ReceiptSearchParams receiptSearchParams) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		receiptSearchParams.setStoreId(storeId);
		return Result.success(receiptService.getReceiptData(receiptSearchParams, page));
	}

	@Operation(summary = "通过id获取", description = "通过id获取", method = CommonConstant.GET)
	@RequestLogger("通过id获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<Receipt> get(@PathVariable String id) {
		return Result.success(OperationalJudgment.judgment(receiptService.getById(id)));
	}

	@Operation(summary = "开发票", description = "开发票", method = CommonConstant.POST)
	@RequestLogger("开发票")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{id}/invoicing")
	public Result<Receipt> invoicing(@PathVariable Long id) {
		OperationalJudgment.judgment(receiptService.getById(id));
		return Result.success(receiptService.invoicing(id));
	}

	@Operation(summary = "通过订单编号获取", description = "通过订单编号获取", method = CommonConstant.GET)
	@RequestLogger("通过订单编号获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/orderSn/{orderSn}")
	public Result<Receipt> getByOrderSn(@PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(receiptService.getByOrderSn(orderSn));
	}

}
