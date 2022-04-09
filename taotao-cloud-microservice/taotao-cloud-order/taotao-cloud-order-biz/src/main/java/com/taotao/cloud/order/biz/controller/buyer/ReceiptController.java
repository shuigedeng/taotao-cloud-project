package com.taotao.cloud.order.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.dto.order.ReceiptSearchParams;
import com.taotao.cloud.order.biz.entity.order.Receipt;
import com.taotao.cloud.order.biz.service.order.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,发票API
 **/
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-发票API", description = "买家端-发票API")
@RequestMapping("/order/buyer/receipt")
public class ReceiptController {

	private final ReceiptService receiptService;

	@Operation(summary = "获取发票详情", description = "获取发票详情", method = CommonConstant.GET)
	@RequestLogger("获取发票详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/{id}")
	public Result<Receipt> getDetail(@PathVariable String id) {
		return Result.success(this.receiptService.getDetail(id));
	}

	@Operation(summary = "获取发票分页信息", description = "获取发票分页信息", method = CommonConstant.GET)
	@RequestLogger("获取发票分页信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<IPage<OrderReceiptDTO>> getPage(ReceiptSearchParams searchParams,
		PageVO pageVO) {
		return Result.success(this.receiptService.getReceiptData(searchParams, pageVO));
	}

	@Operation(summary = "保存发票信息", description = "保存发票信息", method = CommonConstant.POST)
	@RequestLogger("保存发票信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Receipt> save(@Valid Receipt receipt) {
		return Result.success(receiptService.saveReceipt(receipt));
	}

}
