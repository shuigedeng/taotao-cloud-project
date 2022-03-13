package com.taotao.cloud.promotion.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.dto.CouponActivityDTO;
import com.taotao.cloud.promotion.api.vo.CouponActivityVO;
import com.taotao.cloud.promotion.biz.entity.CouponActivity;
import com.taotao.cloud.promotion.biz.service.CouponActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 优惠券活动
 *
 * 
 * @since 2021/5/21 7:11 下午
 */
@RestController
@Api(tags = "管理端,优惠券活动接口")
@RequestMapping("/manager/promotion/couponActivity")
public class CouponActivityManagerController {

    @Autowired
    private CouponActivityService couponActivityService;

    @ApiOperation(value = "获取优惠券活动分页")
    @GetMapping
    public Result<IPage<CouponActivity>> getCouponActivityPage(PageVO page) {
        return Result.success(couponActivityService.page(PageUtil.initPage(page)));
    }

    @ApiOperation(value = "获取优惠券活动")
    @ApiImplicitParam(name = "couponActivityId", value = "优惠券活动ID", required = true, paramType = "path")
    @GetMapping("/{couponActivityId}")
    public Result<CouponActivityVO> getCouponActivity(@PathVariable String couponActivityId) {
        return Result.success(couponActivityService.getCouponActivityVO(couponActivityId));
    }

    @ApiOperation(value = "添加优惠券活动")
    @PostMapping
    @PutMapping(consumes = "application/json", produces = "application/json")
    public Result<CouponActivity> addCouponActivity(@RequestBody(required = false) CouponActivityDTO couponActivityDTO) {
        if (couponActivityService.savePromotions(couponActivityDTO)) {
            return Result.success(couponActivityDTO);
        }
        return ResultUtil.error(ResultCode.COUPON_ACTIVITY_SAVE_ERROR);
    }

    @ApiOperation(value = "关闭优惠券活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "优惠券活动ID", required = true, dataType = "String", paramType = "path")
    })
    @DeleteMapping("/{id}")
    public Result<CouponActivity> updateStatus(@PathVariable String id) {
        if (couponActivityService.updateStatus(Collections.singletonList(id), null, null)) {
            return ResultUtil.success(ResultCode.SUCCESS);
        }
        throw new ServiceException(ResultCode.ERROR);
    }


}
