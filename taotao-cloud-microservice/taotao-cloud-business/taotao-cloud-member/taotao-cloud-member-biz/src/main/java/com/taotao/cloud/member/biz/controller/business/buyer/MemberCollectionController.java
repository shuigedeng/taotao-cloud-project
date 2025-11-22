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

package com.taotao.cloud.member.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.member.api.constant.MemberCollectionQueryConstants;
import com.taotao.cloud.member.sys.model.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.service.business.IMemberGoodsCollectionService;
import com.taotao.cloud.store.api.feign.IFeignStoreCollectionApi;
import com.taotao.cloud.store.api.model.vo.StoreCollectionVO;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,会员收藏API
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员收藏API", description = "买家端-会员收藏API")
@RequestMapping("/member/buyer/member/collection")
public class MemberCollectionController {

    /**
     * 会员商品收藏
     */
    private final IMemberGoodsCollectionService memberGoodsCollectionService;
    /**
     * 会员店铺
     */
    private final IFeignStoreCollectionApi feignStoreCollectionApi;

    @Operation(summary = "查询会员收藏列表", description = "查询会员收藏列表")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping("/{type}")
    public Result<PageResult<StoreCollectionVO>> goodsListPage(@Parameter(description = "类型", required = true)
                                                               @PathVariable String type,
                                                               @Validated PageQuery page) {
        if (MemberCollectionQueryConstants.GOODS.equals(type)) {
            IPage<GoodsCollectionVO> goodsCollectionPage = memberGoodsCollectionService.goodsCollection(page);
            return Result.success(MpUtils.convertMybatisPage(goodsCollectionPage, StoreCollectionVO.class));
        }

        return Result.success(feignStoreCollectionApi.storeCollection(page));
    }

    @Operation(summary = "添加会员收藏", description = "添加会员收藏")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping("/{type}/{id}")
    public Result<Boolean> addGoodsCollection(@Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺")
                                              @PathVariable String type,
                                              @Parameter(description = "id", required = true)
                                              @NotNull(message = "值不能为空")
                                              @PathVariable Long id) {
        if (MemberCollectionQueryConstants.GOODS.equals(type)) {
            return Result.success(memberGoodsCollectionService.addGoodsCollection(id));
        }
        return Result.success(feignStoreCollectionApi.addStoreCollection(id));
    }

    @Operation(summary = "删除会员收藏", description = "删除会员收藏")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @DeleteMapping(value = "/{type}/{id}")
    public Result<Object> deleteGoodsCollection(
            @Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
            @Parameter(description = "id", required = true) @NotNull(message = "值不能为空") @PathVariable Long id) {
        if (MemberCollectionQueryConstants.GOODS.equals(type)) {
            return Result.success(memberGoodsCollectionService.deleteGoodsCollection(id));
        }
        return Result.success(feignStoreCollectionApi.deleteStoreCollection(id));
    }

    @Operation(summary = "查询会员是否收藏", description = "查询会员是否收藏")
    @RequestLogger("查询会员是否收藏")
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/{type}/{id}/collection")
    public Result<Boolean> isCollection(
            @Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
            @Parameter(description = "id", required = true) @NotNull(message = "值不能为空") @PathVariable Long id) {
        if (MemberCollectionQueryConstants.GOODS.equals(type)) {
            return Result.success(memberGoodsCollectionService.isCollection(id));
        }
        return Result.success(this.feignStoreCollectionApi.isCollection(id));
    }
}
