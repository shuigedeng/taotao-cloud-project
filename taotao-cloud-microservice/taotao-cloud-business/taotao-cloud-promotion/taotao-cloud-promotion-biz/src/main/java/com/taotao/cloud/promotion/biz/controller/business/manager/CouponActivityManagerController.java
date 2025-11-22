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

package com.taotao.cloud.promotion.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.promotion.api.model.dto.CouponActivityDTO;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityVO;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivity;
import com.taotao.cloud.promotion.biz.service.business.ICouponActivityService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 优惠券活动
 *
 * @since 2021/5/21 7:11 下午
 */
@RestController
@Tag(name = "管理端,优惠券活动接口")
@RequestMapping("/manager/promotion/couponActivity")
public class CouponActivityManagerController {

    @Autowired
    private ICouponActivityService couponActivityService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取优惠券活动分页")
    @GetMapping
    public Result<IPage<CouponActivity>> getCouponActivityPage(PageQuery page) {
        return Result.success(couponActivityService.page(PageUtil.initPage(page)));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取优惠券活动")
    @ApiImplicitParam(name = "couponActivityId", value = "优惠券活动ID", required = true, paramType = "path")
    @GetMapping("/{couponActivityId}")
    public Result<CouponActivityVO> getCouponActivity(@PathVariable String couponActivityId) {
        return Result.success(couponActivityService.getCouponActivityVO(couponActivityId));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "添加优惠券活动")
    @PostMapping
    @PutMapping
    public Result<CouponActivity> addCouponActivity(
            @RequestBody(required = false) CouponActivityDTO couponActivityDTO) {
        if (couponActivityService.savePromotions(couponActivityDTO)) {
            return Result.success(couponActivityDTO);
        }
        return Result.error(ResultEnum.COUPON_ACTIVITY_SAVE_ERROR);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "关闭优惠券活动")
    @DeleteMapping("/{id}")
    public Result<CouponActivity> updateStatus(@PathVariable String id) {
        if (couponActivityService.updateStatus(Collections.singletonList(id), null, null)) {
            return Result.success(ResultEnum.SUCCESS);
        }
        throw new BusinessException(ResultEnum.ERROR);
    }
}
