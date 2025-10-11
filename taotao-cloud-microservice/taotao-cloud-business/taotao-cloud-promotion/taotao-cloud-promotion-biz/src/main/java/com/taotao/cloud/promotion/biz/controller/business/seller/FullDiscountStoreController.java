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

package com.taotao.cloud.promotion.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.cloud.order.api.model.vo.cart.FullDiscountVO;
import com.taotao.cloud.promotion.api.model.page.FullDiscountPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.service.business.IFullDiscountService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.Objects;
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
 * 店铺端,满额活动接口
 *
 * @since 2020/8/19
 */
@RestController
@Tag(name = "店铺端,满额活动接口")
@RequestMapping("/store/promotion/fullDiscount")
public class FullDiscountStoreController {

    @Autowired
    private IFullDiscountService fullDiscountService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "新增满优惠活动")
    @PostMapping
    public Result<FullDiscount> addFullDiscount(@RequestBody FullDiscountVO fullDiscountVO) {
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
        fullDiscountVO.setStoreId(currentUser.getStoreId());
        fullDiscountVO.setStoreName(currentUser.getStoreName());
        if (!fullDiscountService.savePromotions(fullDiscountVO)) {
            return Result.error(ResultEnum.PINTUAN_ADD_ERROR);
        }
        return Result.success(fullDiscountVO);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "通过id获取")
    @GetMapping("/{id}")
    public Result<FullDiscountVO> get(@PathVariable String id) {
        FullDiscountVO fullDiscount = OperationalJudgment.judgment(fullDiscountService.getFullDiscount(id));
        return Result.success(fullDiscount);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "根据条件分页查询满优惠活动")
    @GetMapping
    public Result<IPage<FullDiscount>> getFullDiscountByPage(FullDiscountPageQuery searchParams, PageVO page) {
        String storeId = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getStoreId();
        searchParams.setStoreId(storeId);
        IPage<FullDiscount> fullDiscountByPage = fullDiscountService.pageFindAll(searchParams, page);
        return Result.success(fullDiscountByPage);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "修改满优惠活动")
    @PutMapping
    public Result<String> editFullDiscount(@RequestBody FullDiscountVO fullDiscountVO) {
        OperationalJudgment.judgment(fullDiscountService.getFullDiscount(fullDiscountVO.getId()));
		SecurityUser currentUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
        fullDiscountVO.setStoreId(currentUser.getStoreId());
        fullDiscountVO.setStoreName(currentUser.getStoreName());
        if (!fullDiscountService.updatePromotions(fullDiscountVO)) {
            return Result.error(ResultEnum.PINTUAN_EDIT_ERROR);
        }
        return Result.success(ResultEnum.FULL_DISCOUNT_EDIT_SUCCESS);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "删除满优惠活动")
    @DeleteMapping("/{id}")
    public Result<String> deleteFullDiscount(@PathVariable String id) {
        OperationalJudgment.judgment(fullDiscountService.getById(id));
        fullDiscountService.removePromotions(Collections.singletonList(id));
        return Result.success(ResultEnum.FULL_DISCOUNT_EDIT_DELETE);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "修改满额活动状态")
    // @ApiImplicitParams({
    //	@ApiImplicitParam(name = "id", value = "满额活动ID", required = true, paramType = "path"),
    //	@ApiImplicitParam(name = "promotionStatus", value = "满额活动状态", required = true, paramType =
    // "path")
    // })
    @PutMapping("/status/{id}")
    public Result<Object> updateCouponStatus(
            @Parameter(description = "满额活动ID") @PathVariable String id, Long startTime, Long endTime) {
        OperationalJudgment.judgment(fullDiscountService.getFullDiscount(id));
        if (fullDiscountService.updateStatus(Collections.singletonList(id), startTime, endTime)) {
            return Result.success(ResultEnum.SUCCESS);
        }
        return Result.error(ResultEnum.ERROR);
    }
}
