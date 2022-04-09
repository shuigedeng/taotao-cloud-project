package com.taotao.cloud.order.biz.controller.buyer;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,预存款充值记录API
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-预存款充值记录API", description = "买家端-预存款充值记录API")
@RequestMapping("/order/buyer/recharge")
@Transactional(rollbackFor = Exception.class)
public class RechargeTradeController {

	private final RechargeService rechargeService;

	@Operation(summary = "创建余额充值订单", description = "创建余额充值订单", method = CommonConstant.POST)
	@RequestLogger("创建余额充值订单")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Recharge> create(
		@Max(value = 10000, message = "充值金额单次最多允许充值10000元") @Min(value = 1, message = "充值金额单次最少充值金额为1元") BigDecimal price) {
		Recharge recharge = this.rechargeService.recharge(price);
		return Result.success(recharge);
	}

}
