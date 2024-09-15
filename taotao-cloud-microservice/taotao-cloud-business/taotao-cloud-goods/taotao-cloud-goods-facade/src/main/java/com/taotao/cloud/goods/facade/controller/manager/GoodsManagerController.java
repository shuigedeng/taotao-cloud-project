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

package com.taotao.cloud.goods.facade.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,商品管理接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-商品管理API", description = "管理端-商品管理API")
@RequestMapping("/goods/manager/goods")
public class GoodsManagerController {

    /** 商品服务 */
    private final IGoodsService goodsService;
    /** 规格商品服务 */
    private final IGoodsSkuService goodsSkuService;

    @Operation(summary = "分页获取", description = "分页获取")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("分页获取")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/page")
    public Result<PageResult<GoodsCO>> getByPage(@Validated GoodsPageQuery goodsPageQuery) {
        IPage<Goods> goodsPage = goodsService.goodsQueryPage(goodsPageQuery);
        return Result.success(MpUtils.convertMybatisPage(goodsPage, GoodsCO.class));
    }

    @Operation(summary = "分页获取商品列表", description = "分页获取商品列表")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("分页获取商品列表")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/sku/page")
    public Result<PageResult<GoodsSkuCO>> getSkuByPage(@Validated GoodsPageQuery goodsPageQuery) {
        IPage<GoodsSku> goodsSkuPage = goodsSkuService.goodsSkuQueryPage(goodsPageQuery);
        return Result.success(MpUtils.convertMybatisPage(goodsSkuPage, GoodsSkuConvert.INSTANCE::convert));
    }

    @Operation(summary = "分页获取待审核商品", description = "分页获取待审核商品")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("分页获取待审核商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/auth/page")
    public Result<PageResult<GoodsCO>> getAuthPage(@Validated GoodsPageQuery goodsPageQuery) {
        goodsPageQuery.setAuthFlag(GoodsAuthEnum.TOBEAUDITED.name());
        IPage<Goods> goodsPage = goodsService.goodsQueryPage(goodsPageQuery);
        return Result.success(MpUtils.convertMybatisPage(goodsPage, GoodsCO.class));
    }

    @Operation(summary = "管理员下架商品", description = "管理员下架商品")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("管理员下架商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/{goodsId}/under")
    public Result<Boolean> underGoods(
            @PathVariable Long goodsId, @NotEmpty(message = "下架原因不能为空") @RequestParam String reason) {
        List<Long> goodsIds = List.of(goodsId);
        return Result.success(goodsService.managerUpdateGoodsMarketAble(goodsIds, GoodsStatusEnum.DOWN, reason));
    }

    @Operation(summary = "管理员审核商品", description = "管理员审核商品")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("管理员审核商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "{goodsIds}/auth")
    public Result<Boolean> auth(@PathVariable List<Long> goodsIds, @RequestParam String authFlag) {
        // 校验商品是否存在
        return Result.success(goodsService.auditGoods(goodsIds, GoodsAuthEnum.valueOf(authFlag)));
    }

    @Operation(summary = "管理员上架商品", description = "管理员上架商品")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("管理员上架商品")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/{goodsId}/up")
    public Result<Boolean> unpGoods(@PathVariable List<Long> goodsId) {
        return Result.success(goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, ""));
    }

    @Operation(summary = "通过id获取商品详情", description = "通过id获取商品详情")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("通过id获取商品详情")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}")
    public Result<GoodsSkuParamsCO> get(@PathVariable Long id) {
        return Result.success(goodsService.getGoodsVO(id));
    }
}
