package com.taotao.cloud.promotion.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.FullDiscountSearchParams;
import com.taotao.cloud.promotion.biz.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.service.FullDiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 管理端,满额活动接口
 *
 * 
 * @since 2021/1/12
 **/
@RestController
@Api(tags = "管理端,满额活动接口")
@RequestMapping("/manager/promotion/fullDiscount")
public class FullDiscountManagerController {

    @Autowired
    private FullDiscountService fullDiscountService;

    @ApiOperation(value = "获取满优惠列表")
    @GetMapping
    public Result<IPage<FullDiscount>> getCouponList(FullDiscountSearchParams searchParams, PageVO page) {
        page.setNotConvert(true);
        return Result.success(fullDiscountService.pageFindAll(searchParams, page));
    }

    @ApiOperation(value = "获取满优惠详情")
    @GetMapping("/{id}")
    public Result<FullDiscountVO> getCouponDetail(@PathVariable String id) {
        return Result.success(fullDiscountService.getFullDiscount(id));
    }

    @ApiOperation(value = "修改满额活动状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "满额活动ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "promotionStatus", value = "满额活动状态", required = true, paramType = "path")
    })
    @PutMapping("/status/{id}")
    public Result<Object> updateCouponStatus(@PathVariable String id, Long startTime, Long endTime) {
        if (fullDiscountService.updateStatus(Collections.singletonList(id), startTime, endTime)) {
            return ResultUtil.success(ResultEnum.SUCCESS);
        }
        return ResultUtil.error(ResultEnum.ERROR);
    }
}
