package com.taotao.cloud.order.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.order.api.model.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.model.page.order.ReceiptPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.Receipt;
import com.taotao.cloud.order.biz.service.business.order.IReceiptService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,发票API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:02
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-发票API", description = "买家端-发票API")
@RequestMapping("/order/buyer/receipt")
public class ReceiptController {

	private final IReceiptService receiptService;

	@Operation(summary = "获取发票详情", description = "获取发票详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/{id}")
	public Result<Receipt> getDetail(@PathVariable String id) {
		return Result.success(this.receiptService.getDetail(id));
	}

	@Operation(summary = "获取发票分页信息", description = "获取发票分页信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageResult<OrderReceiptDTO>> getPage(ReceiptPageQuery searchParams) {
		IPage<OrderReceiptDTO> page = this.receiptService.pageQuery(searchParams);
		return Result.success(PageResult.convertMybatisPage(page, OrderReceiptDTO.class));
	}

	@Operation(summary = "保存发票信息", description = "保存发票信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Valid Receipt receipt) {
		return Result.success(receiptService.saveReceipt(receipt));
	}

}
