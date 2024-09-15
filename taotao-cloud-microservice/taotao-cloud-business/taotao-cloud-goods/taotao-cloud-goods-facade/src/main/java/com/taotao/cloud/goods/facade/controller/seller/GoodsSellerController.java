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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.store.api.feign.IFeignStoreDetailApi;
import com.taotao.cloud.store.api.model.vo.StoreDetailVO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:09:23
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-商品API", description = "店铺端-商品API")
@RequestMapping("/goods/seller/goods")
public class GoodsSellerController {

    /** 商品 */
    private final IGoodsService goodsService;
    /** 商品sku */
    private final IGoodsSkuService goodsSkuService;
    /** 店铺详情 */
    private final IFeignStoreDetailApi storeDetailApi;

    @Operation(summary = "分页获取商品列表", description = "分页获取商品列表")
    @RequestLogger("分页获取商品列表")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<GoodsCO>> getByPage(@Validated GoodsPageQuery goodsPageQuery) {
        // 当前登录商家账号
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        goodsPageQuery.setStoreId(storeId);
        IPage<Goods> goodsPage = goodsService.goodsQueryPage(goodsPageQuery);
        return Result.success(MpUtils.convertMybatisPage(goodsPage, GoodsCO.class));
    }

    @Operation(summary = "分页获取商品Sku列表", description = "分页获取商品Sku列表")
    @RequestLogger("分页获取商品Sku列表")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/sku/page")
    public Result<PageResult<GoodsSkuCO>> getSkuByPage(@Validated GoodsPageQuery goodsPageQuery) {
        // 当前登录商家账号
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        goodsPageQuery.setStoreId(storeId);
        IPage<GoodsSku> goodsSkuPage = goodsSkuService.goodsSkuQueryPage(goodsPageQuery);
        return Result.success(MpUtils.convertMybatisPage(goodsSkuPage, GoodsSkuCO.class));
    }

    @Operation(summary = "分页获取库存告警商品列表", description = "分页获取库存告警商品列表")
    @RequestLogger("分页获取库存告警商品列表")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/stock/warning")
    public Result<StockWarningCO> getWarningStockByPage(@Validated GoodsPageQuery goodsPageQuery) {
        // 当前登录商家账号
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        StoreDetailCO storeDetail = storeDetailApi.getStoreDetailVO(storeId);
        // 库存预警数量
        Integer stockWarnNum = storeDetail.getStockWarning();
        goodsPageQuery.setStoreId(storeId);
        goodsPageQuery.setLeQuantity(stockWarnNum);
        goodsPageQuery.setMarketEnable(GoodsStatusEnum.UPPER.name());
        // 商品SKU列表
        IPage<GoodsSku> goodsSkuPage = goodsSkuService.goodsSkuQueryPage(goodsPageQuery);
        StockWarningCO stockWarning =
                new StockWarningCO(stockWarnNum, MpUtils.convertMybatisPage(goodsSkuPage, GoodsSkuCO.class));
        return Result.success(stockWarning);
    }

    @Operation(summary = "通过id获取", description = "通过id获取")
    @Parameters({
            @Parameter(name = "goodsId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("通过id获取")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{goodsId}")
    public Result<GoodsSkuParamsCO> get(@PathVariable Long goodsId) {
        return Result.success(goodsService.getGoodsVO(goodsId));
    }

    @Operation(summary = "新增商品", description = "新增商品")
    @RequestLogger("新增商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Boolean> save(@Validated @RequestBody GoodsOperationDTO goodsOperationDTO) {
        return Result.success(goodsService.addGoods(goodsOperationDTO));
    }

    @Operation(summary = "修改商品", description = "修改商品")
    @Parameters({
            @Parameter(name = "goodsId", required = true, description = "商品ID", in = ParameterIn.PATH),
    })
    @RequestLogger("修改商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/{goodsId}")
    public Result<Boolean> update(@Validated @RequestBody GoodsOperationDTO goodsOperationDTO, @PathVariable Long goodsId) {
        return Result.success(goodsService.editGoods(goodsOperationDTO, goodsId));
    }

    @Operation(summary = "下架商品", description = "下架商品")
    @RequestLogger("下架商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/under")
    public Result<Boolean> underGoods(@Validated @NotEmpty(message = "商品id不能为空") @RequestBody List<Long> goodsId) {
        return Result.success(goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.DOWN, "商家下架"));
    }

    @Operation(summary = "上架商品", description = "上架商品")

    @RequestLogger("上架商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/up")
    public Result<Boolean> unpGoods(@RequestParam List<Long> goodsId) {
        return Result.success(goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, ""));
    }

    @Operation(summary = "删除商品", description = "删除商品")
    @RequestLogger("删除商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping
    public Result<Boolean> deleteGoods(@Validated @NotEmpty(message = "商品信息不能为空") @RequestBody List<Long> goodsIds) {
        return Result.success(goodsService.deleteGoods(goodsIds));
    }

    @Operation(summary = "设置商品运费模板", description = "设置商品运费模板")
    @Parameters({
            @Parameter(name = "templateId", required = true, description = "模板id", in = ParameterIn.PATH),
    })
    @RequestLogger("设置商品运费模板")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/freight/{templateId}")
    public Result<Boolean> freight(@Validated @NotEmpty(message = "商品信息不能为空") @RequestBody List<Long> goodsId,
                                   @PathVariable Long templateId) {
        return Result.success(goodsService.freight(goodsId, templateId));
    }

    @Operation(summary = "根据goodsId分页获取商品规格列表", description = "根据goodsId分页获取商品规格列表")
    @Parameters({
            @Parameter(name = "goodsId", required = true, description = "商品id", in = ParameterIn.PATH),
    })
    @RequestLogger("根据goodsId分页获取商品规格列表")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/sku/{goodsId}/page")
    public Result<List<GoodsSkuSpecGalleryCO>> getSkuByList(@PathVariable Long goodsId) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        return Result.success(goodsSkuService.getGoodsSkuVOList(goodsSkuService.list(new LambdaQueryWrapper<GoodsSku>()
                .eq(GoodsSku::getGoodsId, goodsId)
                .eq(GoodsSku::getStoreId, storeId))));
    }

    @Operation(summary = "修改商品库存", description = "修改商品库存")
    @RequestLogger("修改商品库存")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/stocks")
    public Result<Boolean> updateStocks(@Validated @RequestBody List<GoodsSkuStockDTO> updateStockList) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        // 获取商品skuId集合
        List<Long> goodsSkuIds =
                updateStockList.stream().map(GoodsSkuStockDTO::getSkuId).toList();
        // 根据skuId集合查询商品信息
        List<GoodsSku> goodsSkuList = goodsSkuService.list(new LambdaQueryWrapper<GoodsSku>()
                .in(GoodsSku::getId, goodsSkuIds)
                .eq(GoodsSku::getStoreId, storeId));
        // 过滤不符合当前店铺的商品
        List<Long> filterGoodsSkuIds =
                goodsSkuList.stream().map(GoodsSku::getId).toList();
        List<GoodsSkuStockDTO> collect = updateStockList.stream()
                .filter(i -> filterGoodsSkuIds.contains(i.getSkuId()))
                .toList();
        return Result.success(goodsSkuService.updateStocks(collect));
    }
}
