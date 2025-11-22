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
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.vo.PintuanMemberVO;
import com.taotao.cloud.promotion.api.model.vo.PintuanShareVO;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.IPintuanService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,拼团接口
 *
 */
@Tag(name = "买家端,拼团接口")
@RestController
@RequestMapping("/buyer/promotion/pintuan")
public class PintuanBuyerController {

    @Autowired
    private IPromotionGoodsService promotionGoodsService;

    @Autowired
    private IPintuanService pintuanService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取拼团商品")
    @GetMapping
    public Result<IPage<PromotionGoods>> getPintuanCategory(String goodsName, String categoryPath, PageVO pageVo) {
        PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
        searchParams.setGoodsName(goodsName);
        searchParams.setPromotionType(PromotionTypeEnum.PINTUAN.name());
        searchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        searchParams.setCategoryPath(categoryPath);
        return Result.success(promotionGoodsService.pageFindAll(searchParams, pageVo));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取当前拼团活动的未成团的会员")
    @GetMapping("/{pintuanId}/members")
    public Result<List<PintuanMemberVO>> getPintuanMember(@PathVariable String pintuanId) {
        return Result.success(pintuanService.getPintuanMember(pintuanId));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取当前拼团订单的拼团分享信息")
    @GetMapping("/share")
    public Result<PintuanShareVO> share(String parentOrderSn, String skuId) {
        return Result.success(pintuanService.getPintuanShareInfo(parentOrderSn, skuId));
    }
}
