package com.taotao.cloud.promotion.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.vo.CouponSearchParams;
import com.taotao.cloud.promotion.api.vo.CouponVO;
import com.taotao.cloud.promotion.biz.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.CouponService;
import com.taotao.cloud.promotion.biz.service.MemberCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 买家端,买家优惠券接口
 *
 * 
 * @since 2020/11/17 3:35 下午
 */
@RestController
@Api(tags = "买家端,买家优惠券接口")
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

    @GetMapping
    @ApiOperation(value = "获取可领取优惠券列表")
    public Result<IPage<CouponVO>> getCouponList(CouponSearchParams queryParam, PageVO page) {
        queryParam.setPromotionStatus(PromotionsStatusEnum.START.name());
        queryParam.setGetType(CouponGetEnum.FREE.name());
        IPage<CouponVO> canUseCoupons = couponService.pageVOFindAll(queryParam, page);
        return Result.success(canUseCoupons);
    }

    @ApiOperation(value = "获取当前会员的优惠券列表")
    @GetMapping("/getCoupons")
    public Result<IPage<MemberCoupon>> getCoupons(CouponSearchParams param, PageVO pageVo) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        param.setMemberId(currentUser.getId());
        return Result.success(memberCouponService.getMemberCoupons(param, pageVo));
    }

    @ApiOperation(value = "获取当前会员的对于当前商品可使用的优惠券列表")
    @GetMapping("/canUse")
    public Result<IPage<MemberCoupon>> getCouponsByCanUse(CouponSearchParams param, BigDecimal totalPrice, PageVO pageVo) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        param.setMemberId(currentUser.getId());
        return Result.success(memberCouponService.getMemberCouponsByCanUse(param, totalPrice, pageVo));
    }

    @ApiOperation(value = "获取当前会员可使用的优惠券数量")
    @GetMapping("/getCouponsNum")
    public Result<Object> getMemberCouponsNum() {
        return Result.success(memberCouponService.getMemberCouponsNum());
    }

    @ApiOperation(value = "会员领取优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponId", value = "优惠券ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/receive/{couponId}")
    public Result<Object> receiveCoupon(@NotNull(message = "优惠券ID不能为空") @PathVariable("couponId") String couponId) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        memberCouponService.receiveBuyerCoupon(couponId, currentUser.getId(), currentUser.getNickName());
        return ResultUtil.success();
    }

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "优惠券ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping(value = "/get/{id}")
    public Result<MemberCoupon> get(@NotNull(message = "优惠券ID不能为空") @PathVariable("id") String id) {
        MemberCoupon memberCoupon = OperationalJudgment.judgment(memberCouponService.getById(id));
        return Result.success(memberCoupon);
    }


}
