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
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,促销接口
 *
 * @since 2021/2/2
 */
@RestController
@Tag(name = "管理端,促销接口")
@RequestMapping("/manager/promotion")
public class PromotionManagerController {

    @Autowired
    private IPromotionService promotionService;

    @Autowired
    private IPromotionGoodsService promotionGoodsService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/current")
    @Operation(summary = "获取当前进行中的促销活动")
    public Result<Map<String, Object>> getCurrentPromotion() {
        Map<String, Object> currentPromotion = promotionService.getCurrentPromotion();
        return Result.success(currentPromotion);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/{promotionId}/goods")
    @Operation(summary = "获取当前进行中的促销活动商品")
    public Result<IPage<PromotionGoods>> getPromotionGoods(
            @PathVariable String promotionId, String promotionType, PageQuery pageQuery) {
        PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
        // searchParams.setPromotionId(promotionId);
        searchParams.setPromotionType(promotionType);
        searchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        IPage<PromotionGoods> promotionGoods = promotionGoodsService.pageFindAll(searchParams, pageQuery);
        return Result.success(promotionGoods);
    }
}
