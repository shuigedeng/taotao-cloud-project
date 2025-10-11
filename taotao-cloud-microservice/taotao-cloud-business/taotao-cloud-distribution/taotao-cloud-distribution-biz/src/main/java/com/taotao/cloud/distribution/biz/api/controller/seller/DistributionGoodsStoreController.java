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

package com.taotao.cloud.distribution.biz.api.controller.seller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.cloud.distribution.api.model.query.DistributionGoodsPageQuery;
import com.taotao.cloud.distribution.api.model.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.model.entity.DistributionGoods;
import com.taotao.cloud.distribution.biz.model.entity.DistributionSelectedGoods;
import com.taotao.cloud.distribution.biz.service.IDistributionGoodsService;
import com.taotao.cloud.distribution.biz.service.IDistributionSelectedGoodsService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 店铺端,分销商品接口 */
@Validated
@RestController
@Tag(name = "店铺端,分销商品接口", description = "店铺端,分销商品接口")
@RequestMapping("/store/distribution/goods")
public class DistributionGoodsStoreController {

    /** 分销商品 */
    @Autowired
    private IDistributionGoodsService distributionGoodsService;

    /** 已选择分销商品 */
    @Autowired
    private IDistributionSelectedGoodsService distributionSelectedGoodsService;

    @Operation(summary = "获取分销商商品列表", description = "获取分销商商品列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<IPage<DistributionGoodsVO>> distributionGoods(DistributionGoodsPageQuery distributionGoodsPageQuery) {
        return Result.success(distributionGoodsService.goodsPage(distributionGoodsPageQuery));
    }

    @Operation(summary = "选择商品参与分销", description = "选择商品参与分销")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/tree")
    @PutMapping(value = "/checked/{skuId}")
    public Result<DistributionGoods> distributionCheckGoods(
            @NotNull(message = "规格ID不能为空") @PathVariable String skuId,
            @NotNull(message = "佣金金额不能为空") @RequestParam BigDecimal commission) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        return Result.success(distributionGoodsService.checked(skuId, commission, storeId));
    }

    @Operation(summary = "取消分销商品", description = "取消分销商品")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/tree")
    @DeleteMapping(value = "/cancel/{id}")
    public Result<Object> cancel(@NotNull @PathVariable String id) {
        OperationalJudgment.judgment(distributionGoodsService.getById(id));
        // 清除分销商已选择分销商品
        distributionSelectedGoodsService.remove(
                new QueryWrapper<DistributionSelectedGoods>().eq("distribution_goods_id", id));
        // 清除分销商品
        distributionGoodsService.removeById(id);
        return Result.success();
    }
}
