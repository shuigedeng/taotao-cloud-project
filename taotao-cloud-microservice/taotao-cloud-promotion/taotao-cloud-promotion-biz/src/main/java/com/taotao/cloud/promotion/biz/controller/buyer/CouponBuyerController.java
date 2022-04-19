package com.taotao.cloud.promotion.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.query.CouponSearchParams;
import com.taotao.cloud.promotion.api.vo.CouponVO;
import com.taotao.cloud.promotion.biz.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.CouponService;
import com.taotao.cloud.promotion.biz.service.MemberCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,买家优惠券接口
 *
 * @since 2020/11/17 3:35 下午
 */
@RestController
@Tag(name = "买家端,买家优惠券接口")
@RequestMapping("/buyer/promotion/coupon")
public class CouponBuyerController {

	/**
	 * 优惠券
	 */
	@Autowired
	private CouponService couponService;

	/**
	 * 会员优惠券
	 */
	@Autowired
	private MemberCouponService memberCouponService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "获取可领取优惠券列表")
	public Result<IPage<CouponVO>> getCouponList(CouponSearchParams queryParam, PageVO page) {
		queryParam.setPromotionStatus(PromotionsStatusEnum.START.name());
		queryParam.setGetType(CouponGetEnum.FREE.name());
		IPage<CouponVO> canUseCoupons = couponService.pageVOFindAll(queryParam, page);
		return Result.success(canUseCoupons);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前会员的优惠券列表")
	@GetMapping("/getCoupons")
	public Result<IPage<MemberCoupon>> getCoupons(CouponSearchParams param, PageVO pageVo) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		param.setMemberId(currentUser.getId());
		return Result.success(memberCouponService.getMemberCoupons(param, pageVo));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前会员的对于当前商品可使用的优惠券列表")
	@GetMapping("/canUse")
	public Result<IPage<MemberCoupon>> getCouponsByCanUse(CouponSearchParams param,
		BigDecimal totalPrice, PageVO pageVo) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		param.setMemberId(currentUser.getId());
		return Result.success(
			memberCouponService.getMemberCouponsByCanUse(param, totalPrice, pageVo));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前会员可使用的优惠券数量")
	@GetMapping("/getCouponsNum")
	public Result<Object> getMemberCouponsNum() {
		return Result.success(memberCouponService.getMemberCouponsNum());
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "会员领取优惠券")
	@GetMapping("/receive/{couponId}")
	public Result<Object> receiveCoupon(
		@NotNull(message = "优惠券ID不能为空") @PathVariable("couponId") String couponId) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		memberCouponService.receiveBuyerCoupon(couponId, currentUser.getId(),
			currentUser.getNickName());
		return Result.success();
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "通过id获取")
	@GetMapping(value = "/get/{couponId}")
	public Result<MemberCoupon> get(
		@NotNull(message = "优惠券ID不能为空") @PathVariable("couponId") String couponId) {
		MemberCoupon memberCoupon = OperationalJudgment.judgment(
			memberCouponService.getById(couponId));
		return Result.success(memberCoupon);
	}


}
