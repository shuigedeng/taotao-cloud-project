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

import com.taotao.boot.common.model.Result;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.store.api.feign.IFeignStoreDetailApi;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品分类接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:17:12
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-商品分类API", description = "店铺端-商品分类API")
@RequestMapping("/goods/seller/category/store")
public class CategorySellerController {

    /** 分类服务 */
    private final ICategoryService categoryService;
    /** 分类品牌服务 */
    private final ICategoryBrandService categoryBrandService;
    /** 店铺详情服务 */
    private final IFeignStoreDetailApi storeDetailApi;

    @Operation(summary = "获取店铺经营的分类", description = "获取店铺经营的分类")
    @RequestLogger("获取店铺经营的分类")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/all")
    public Result<List<CategoryTreeCO>> getListAll() {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        // 获取店铺经营范围
        String goodsManagementCategory = storeDetailApi.getStoreDetailVO(storeId).getGoodsManagementCategory();
        return Result.success(this.categoryService.getStoreCategory(goodsManagementCategory.split(",")));
    }

    @Operation(summary = "获取所选分类关联的品牌信息", description = "获取所选分类关联的品牌信息")
    @RequestLogger("获取所选分类关联的品牌信息")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{categoryId}/brands")
    public Result<List<CategoryBrandCO>> queryBrands(@PathVariable Long categoryId) {
        return Result.success(this.categoryBrandService.getCategoryBrandList(categoryId));
    }
}
