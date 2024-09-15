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
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,店铺分类接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:49:55
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-店铺分类API", description = "店铺端-店铺分类API")
@RequestMapping("/goods/seller/label")
public class GoodsLabelSellerController {

    /** 店铺分类服务 */
    private final IStoreGoodsLabelService storeGoodsLabelService;

    @Operation(summary = "获取当前店铺商品分类列表", description = "获取当前店铺商品分类列表")
    @RequestLogger("获取当前店铺商品分类列表")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<List<StoreGoodsLabelCO>> list() {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        return Result.success(storeGoodsLabelService.listByStoreId(storeId));
    }

    @Operation(summary = "获取店铺商品分类详情", description = "获取店铺商品分类详情")
    @RequestLogger("获取店铺商品分类详情")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/{id}")
    public Result<StoreGoodsLabelInfoCO> getStoreGoodsLabel(@PathVariable Long id) {
        StoreGoodsLabel storeGoodsLabel = storeGoodsLabelService.getById(id);
        return Result.success(GoodsLabelStoreConvert.INSTANCE.convert(storeGoodsLabel));
    }

    @Operation(summary = "添加店铺商品分类", description = "添加店铺商品分类")
    @RequestLogger("添加店铺商品分类")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Boolean> add(@Validated @RequestBody StoreGoodsLabelDTO storeGoodsLabelDTO) {
        StoreGoodsLabel storeGoodsLabel = GoodsLabelStoreConvert.INSTANCE.convert(storeGoodsLabelDTO);
        return Result.success(storeGoodsLabelService.addStoreGoodsLabel(storeGoodsLabel));
    }

    @Operation(summary = "修改店铺商品分类", description = "修改店铺商品分类")
    @RequestLogger("修改店铺商品分类")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}")
    public Result<Boolean> edit(@PathVariable Long id, @Validated @RequestBody StoreGoodsLabelDTO storeGoodsLabelDTO) {
        StoreGoodsLabel storeGoodsLabel = GoodsLabelStoreConvert.INSTANCE.convert(storeGoodsLabelDTO);
        storeGoodsLabel.setId(id);
        return Result.success(storeGoodsLabelService.editStoreGoodsLabel(storeGoodsLabel));
    }

    @Operation(summary = "删除店铺商品分类", description = "删除店铺商品分类")
    @RequestLogger("删除店铺商品分类")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(storeGoodsLabelService.removeStoreGoodsLabel(id));
    }
}
