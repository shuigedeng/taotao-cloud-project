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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.cloud.promotion.api.model.page.CouponPageQuery;
import com.taotao.cloud.promotion.api.model.vo.CouponVO;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;
import com.taotao.cloud.promotion.biz.model.entity.MemberCoupon;
import com.taotao.cloud.promotion.biz.service.business.ICouponService;
import com.taotao.cloud.promotion.biz.service.business.IMemberCouponService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.SecurityUtils;
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
 * 管理端,优惠券接口
 *
 * @since 2020/10/9
 */
@RestController
@Tag(name = "管理端,优惠券接口")
@RequestMapping("/manager/promotion/coupon")
public class CouponManagerController {

    @Autowired
    private ICouponService couponService;

    @Autowired
    private IMemberCouponService memberCouponService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取优惠券列表")
    @GetMapping
    public Result<IPage<CouponVO>> getCouponList(CouponPageQuery queryParam) {
        queryParam.setStoreId("platform");
        return Result.success(couponService.pageVOFindAll(queryParam, page));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取优惠券详情")
    @GetMapping("/{couponId}")
    public Result<CouponVO> getCoupon(@PathVariable String couponId) {
        CouponVO coupon = couponService.getDetail(couponId);
        return Result.success(coupon);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "添加优惠券")
    @PostMapping
    public Result<CouponVO> addCoupon(@RequestBody CouponVO couponVO) {
        this.setStoreInfo(couponVO);
        couponService.savePromotions(couponVO);
        return Result.success(couponVO);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "修改优惠券")
    @PutMapping
    public Result<Coupon> updateCoupon(@RequestBody CouponVO couponVO) {
        this.setStoreInfo(couponVO);
        Coupon coupon = couponService.getById(couponVO.getId());
        couponService.updatePromotions(couponVO);
        return Result.success(coupon);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "修改优惠券状态")
    @PutMapping("/status")
    public Result<Object> updateCouponStatus(String couponIds, Long startTime, Long endTime) {
        String[] split = couponIds.split(",");
        if (couponService.updateStatus(Arrays.asList(split), startTime, endTime)) {
            return Result.success(ResultEnum.COUPON_EDIT_STATUS_SUCCESS);
        }
        throw new BusinessException(ResultEnum.COUPON_EDIT_STATUS_ERROR);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "批量删除")
    @DeleteMapping(value = "/{ids}")
    public Result<Object> delAllByIds(@PathVariable List<String> ids) {
        couponService.removePromotions(ids);
        return Result.success();
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "会员优惠券作废")
    @PutMapping(value = "/member/cancellation/{id}")
    public Result<Object> cancellation(@PathVariable String id) {
        memberCouponService.cancellation(id);
        return Result.success(ResultEnum.COUPON_CANCELLATION_SUCCESS);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "根据优惠券id券分页获取会员领详情")
    @GetMapping(value = "/member/{id}")
    public Result<IPage<MemberCoupon>> getByPage(@PathVariable String id, PageQuery page) {
        QueryWrapper<MemberCoupon> queryWrapper = new QueryWrapper<>();
        IPage<MemberCoupon> data = memberCouponService.page(PageUtil.initPage(page), queryWrapper.eq("coupon_id", id));
        return Result.success(data);
    }

    private void setStoreInfo(CouponVO couponVO) {
		SecurityUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(ResultEnum.USER_NOT_EXIST);
        }
        couponVO.setStoreId("platform");
        couponVO.setStoreName("platform");
    }
}
