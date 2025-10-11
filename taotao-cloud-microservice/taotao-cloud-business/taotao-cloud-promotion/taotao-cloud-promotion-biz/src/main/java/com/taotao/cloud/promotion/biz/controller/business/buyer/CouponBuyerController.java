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

package com.taotao.cloud.promotion.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.CouponPageQuery;
import com.taotao.cloud.promotion.api.model.vo.CouponVO;
import com.taotao.cloud.promotion.api.model.vo.MemberCouponVO;
import com.taotao.cloud.promotion.biz.model.convert.MemberCouponConvert;
import com.taotao.cloud.promotion.biz.model.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.business.ICouponService;
import com.taotao.cloud.promotion.biz.service.business.IMemberCouponService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 买家端,买家优惠券接口
 */
@AllArgsConstructor
@RestController
@Tag(name = "买家端,买家优惠券接口")
@RequestMapping("/buyer/promotion/coupon")
public class CouponBuyerController {

	/**
	 * 优惠券
	 */
	private final ICouponService couponService;

	/**
	 * 会员优惠券
	 */
	private final IMemberCouponService memberCouponService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/page")
	@Operation(summary = "获取可领取优惠券列表")
	public Result<PageResult<CouponVO>> getCouponList(CouponPageQuery queryParam) {
		queryParam.setPromotionStatus(PromotionsStatusEnum.START.name());
		queryParam.setGetType(CouponGetEnum.FREE.name());

		IPage<CouponVO> couponVOIPage = couponService.pageVOFindAll(queryParam, queryParam.getPageParm());
		return Result.success(MpUtils.convertMybatisPage(couponVOIPage, CouponVO.class));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前会员的优惠券列表")
	@GetMapping("/getCoupons")
	public Result<PageResult<MemberCouponVO>> getCoupons(CouponPageQuery param) {
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		// param.setMemberId(currentUser.getId());
		IPage<MemberCoupon> memberCoupons = memberCouponService.getMemberCoupons(param, param.getPageParm());
		return Result.success(MpUtils.convertMybatisPage(memberCoupons, MemberCouponVO.class));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前会员的对于当前商品可使用的优惠券列表")
	@GetMapping("/canUse")
	public Result<PageResult<MemberCouponVO>> getCouponsByCanUse(CouponPageQuery param, BigDecimal totalPrice) {
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		// param.setMemberId(currentUser.getId());
		IPage<MemberCoupon> memberCoupons = memberCouponService.getMemberCouponsByCanUse(param, totalPrice, param.getPageParm());
		return Result.success(MpUtils.convertMybatisPage(memberCoupons, MemberCouponVO.class));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前会员可使用的优惠券数量")
	@GetMapping("/getCouponsNum")
	public Result<Long> getMemberCouponsNum() {
		return Result.success(memberCouponService.getMemberCouponsNum());
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "会员领取优惠券")
	@GetMapping("/receive/{couponId}")
	public Result<Boolean> receiveCoupon(@NotNull(message = "优惠券ID不能为空") @PathVariable("couponId") String couponId) {
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		memberCouponService.receiveBuyerCoupon(couponId, currentUser.getId(), currentUser.getNickName());
		return Result.success(true);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "通过id获取")
	@GetMapping(value = "/get/{couponId}")
	public Result<MemberCouponVO> get(@NotNull(message = "优惠券ID不能为空") @PathVariable("couponId") String couponId) {
		MemberCoupon memberCoupon = OperationalJudgment.judgment(memberCouponService.getById(couponId));
		return Result.success(MemberCouponConvert.INSTANCE.convert(memberCoupon));
	}
}
