/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.facade.controller.buyer;

import com.taotao.boot.common.model.Result;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class RechargeTradeController {

	private final IFeignMemberRechargeApi memberRechargeApi;

	@Operation(summary = "创建余额充值订单", description = "创建余额充值订单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<MemberRechargeVO> create(@Max(value = 10000, message = "充值金额单次最多允许充值10000元")
										   @Min(value = 1, message = "充值金额单次最少充值金额为1元")
										   BigDecimal price) {
		MemberRechargeVO recharge = this.memberRechargeApi.recharge(price);
		return Result.success(recharge);
	}
}
