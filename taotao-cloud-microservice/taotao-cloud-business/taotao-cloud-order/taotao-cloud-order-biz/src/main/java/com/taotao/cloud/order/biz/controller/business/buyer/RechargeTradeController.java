package com.taotao.cloud.order.biz.controller.business.buyer;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.member.api.feign.IFeignMemberRechargeApi;
import com.taotao.cloud.member.api.model.vo.MemberRechargeVO;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,预存款充值记录API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:06
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-预存款充值记录API", description = "买家端-预存款充值记录API")
@RequestMapping("/order/buyer/recharge")
@Transactional(rollbackFor = Exception.class)
public class RechargeTradeController {

	private final IFeignMemberRechargeApi memberRechargeApi;

	@Operation(summary = "创建余额充值订单", description = "创建余额充值订单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<MemberRechargeVO> create(
		@Max(value = 10000, message = "充值金额单次最多允许充值10000元") @Min(value = 1, message = "充值金额单次最少充值金额为1元") BigDecimal price) {
		MemberRechargeVO recharge = this.memberRechargeApi.recharge(price);
		return Result.success(recharge);
	}

}
