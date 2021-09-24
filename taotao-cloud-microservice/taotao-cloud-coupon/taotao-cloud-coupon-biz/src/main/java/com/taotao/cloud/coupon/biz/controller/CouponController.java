/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.coupon.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.coupon.api.vo.CouponVO;
import com.taotao.cloud.coupon.biz.entity.Coupon;
import com.taotao.cloud.coupon.biz.mapper.CouponMapper;
import com.taotao.cloud.coupon.biz.service.ICouponService;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提现申请管理API
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/13 09:58
 */
@Validated
@RestController
@RequestMapping("/withdraw")
@Tag(name = "提现申请管理API", description = "提现申请管理API")
public class CouponController {

	private final ICouponService withdrawService;

	public CouponController(ICouponService withdrawService) {
		this.withdrawService = withdrawService;
	}

	@Operation(summary = "根据id查询提现申请信息", description = "根据id查询提现申请信息", method = CommonConstant.GET)
	@RequestOperateLog(description = "根据id查询提现申请信息")
	@PreAuthorize("hasAuthority('withdraw:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<CouponVO> findWithdrawById(@PathVariable(value = "id") Long id) {
		Coupon coupon = withdrawService.findWithdrawById(id);
		CouponVO vo = CouponMapper.INSTANCE.withdrawToWithdrawVO(coupon);
		return Result.success(vo);
	}

}
