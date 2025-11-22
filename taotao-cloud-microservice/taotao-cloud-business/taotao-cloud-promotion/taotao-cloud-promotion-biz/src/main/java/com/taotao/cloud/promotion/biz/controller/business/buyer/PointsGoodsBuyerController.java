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
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.promotion.api.model.page.PointsGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoodsCategory;
import com.taotao.cloud.promotion.biz.service.business.IPointsGoodsCategoryService;
import com.taotao.cloud.promotion.biz.service.business.IPointsGoodsService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,积分商品接口
 *
 * @since 2021/1/19
 */
@RestController
@Tag(name = "买家端,积分商品接口")
@RequestMapping("/buyer/promotion/pointsGoods")
public class PointsGoodsBuyerController {

    @Autowired
    private IPointsGoodsService pointsGoodsService;

    @Autowired
    private IPointsGoodsCategoryService pointsGoodsCategoryService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping
    @Operation(summary = "分页获取积分商品")
    public Result<IPage<PointsGoods>> getPointsGoodsPage(PointsGoodsPageQuery searchParams ){
        IPage<PointsGoods> pointsGoodsByPage = pointsGoodsService.pageFindAll(searchParams, searchParams.getPageParm());
        return Result.success(pointsGoodsByPage);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/category")
    @Operation(summary = "获取积分商品分类分页")
    public Result<IPage<PointsGoodsCategory>> page(String name, PageQuery page) {
        return Result.success(pointsGoodsCategoryService.getCategoryByPage(name, page));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/{id}")
    @Operation(summary = "获取积分活动商品")
    public Result<PointsGoodsVO> getPointsGoodsPage(@Parameter(name = "积分商品ID") @PathVariable String id) {
        return Result.success(pointsGoodsService.getPointsGoodsDetail(id));
    }
}
