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

package com.taotao.cloud.goods.facade.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageQuery;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端-商品计量单位接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:05:11
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-商品计量单位API", description = "店铺端-商品计量单位API")
@RequestMapping("/goods/seller/goods/unit")
public class GoodsUnitSellerController {

    /** 商品计量单位服务 */
    private final IGoodsUnitService goodsUnitService;

    @Operation(summary = "分页获取商品计量单位", description = "分页获取商品计量单位")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<GoodsUnitCO>> getByPage(@Validated PageQuery pageQuery) {
        IPage<GoodsUnit> page = goodsUnitService.page(pageQuery.buildMpPage());
        return Result.success(MpUtils.convertMybatisPage(page, GoodsUnitConvert.INSTANCE::convert));
    }
}
