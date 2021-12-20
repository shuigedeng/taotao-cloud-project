package com.taotao.cloud.promotion.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.CouponSearchParams;
import com.taotao.cloud.promotion.api.vo.CouponVO;
import com.taotao.cloud.promotion.biz.entity.Coupon;
import com.taotao.cloud.promotion.biz.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.CouponService;
import com.taotao.cloud.promotion.biz.service.MemberCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理端,优惠券接口
 *
 * @author paulG
 * @since 2020/10/9
 **/
@RestController
@Api(tags = "管理端,优惠券接口")
@RequestMapping("/manager/promotion/coupon")
public class CouponManagerController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private MemberCouponService memberCouponService;

    @ApiOperation(value = "获取优惠券列表")
    @GetMapping
    public ResultMessage<IPage<CouponVO>> getCouponList(CouponSearchParams queryParam, PageVO page) {
        queryParam.setStoreId("platform");
        return ResultUtil.data(couponService.pageVOFindAll(queryParam, page));
    }

    @ApiOperation(value = "获取优惠券详情")
    @GetMapping("/{couponId}")
    public ResultMessage<CouponVO> getCoupon(@PathVariable String couponId) {
        CouponVO coupon = couponService.getDetail(couponId);
        return ResultUtil.data(coupon);
    }

    @ApiOperation(value = "添加优惠券")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResultMessage<CouponVO> addCoupon(@RequestBody CouponVO couponVO) {
        this.setStoreInfo(couponVO);
        couponService.savePromotions(couponVO);
        return ResultUtil.data(couponVO);
    }

    @ApiOperation(value = "修改优惠券")
    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResultMessage<Coupon> updateCoupon(@RequestBody CouponVO couponVO) {
        this.setStoreInfo(couponVO);
        Coupon coupon = couponService.getById(couponVO.getId());
        couponService.updatePromotions(couponVO);
        return ResultUtil.data(coupon);
    }

    @ApiOperation(value = "修改优惠券状态")
    @PutMapping("/status")
    public ResultMessage<Object> updateCouponStatus(String couponIds, Long startTime, Long endTime) {
        String[] split = couponIds.split(",");
        if (couponService.updateStatus(Arrays.asList(split), startTime, endTime)) {
            return ResultUtil.success(ResultCode.COUPON_EDIT_STATUS_SUCCESS);
        }
        throw new ServiceException(ResultCode.COUPON_EDIT_STATUS_ERROR);
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/{ids}")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        couponService.removePromotions(ids);
        return ResultUtil.success();
    }

    @ApiOperation(value = "会员优惠券作废")
    @PutMapping(value = "/member/cancellation/{id}")
    public ResultMessage<Object> cancellation(@PathVariable String id) {
        memberCouponService.cancellation(id);
        return ResultUtil.success(ResultCode.COUPON_CANCELLATION_SUCCESS);
    }

    @ApiOperation(value = "根据优惠券id券分页获取会员领详情")
    @GetMapping(value = "/member/{id}")
    public ResultMessage<IPage<MemberCoupon>> getByPage(@PathVariable String id,
                                                        PageVO page) {
        QueryWrapper<MemberCoupon> queryWrapper = new QueryWrapper<>();
        IPage<MemberCoupon> data = memberCouponService.page(PageUtil.initPage(page),
                queryWrapper.eq("coupon_id", id)
        );
        return ResultUtil.data(data);

    }

    private void setStoreInfo(CouponVO couponVO) {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        couponVO.setStoreId("platform");
        couponVO.setStoreName("platform");
    }

}
