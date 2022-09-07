package com.taotao.cloud.order.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.model.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.model.query.order.ReceiptPageQuery;
import com.taotao.cloud.order.biz.service.order.IReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,发票记录API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:24
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-发票记录管理API", description = "平台管理端-发票记录管理API")
@RequestMapping("/order/manager/receipt")
public class ReceiptController {

	private final IReceiptService receiptService;

	@Operation(summary = "获取发票分页信息", description = "获取发票分页信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	public Result<PageResult<OrderReceiptDTO>> getPage(ReceiptPageQuery searchParams) {
		IPage<OrderReceiptDTO> receiptData = this.receiptService.getReceiptData(searchParams);
		return Result.success(PageResult.convertMybatisPage(receiptData, OrderReceiptDTO.class));
	}

}
