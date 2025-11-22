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

package com.taotao.cloud.customer.biz.api.controller.buyer;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.goods.api.feign.StoreGoodsLabelApi;
import com.taotao.cloud.goods.api.model.vo.StoreGoodsLabelVO;
import com.taotao.cloud.store.api.feign.IFeignStoreApi;
import com.taotao.cloud.store.api.feign.IFeignStoreDetailApi;
import com.taotao.cloud.store.api.model.dto.StoreBankDTO;
import com.taotao.cloud.store.api.model.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.model.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.model.query.StorePageQuery;
import com.taotao.cloud.store.api.model.vo.StoreBasicInfoVO;
import com.taotao.cloud.store.api.model.vo.StoreDetailVO;
import com.taotao.cloud.store.api.model.vo.StoreOtherVO;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 买家端,店铺接口 */
@Validated
@RestController
@Tag(name = "买家端-店铺接口", description = "买家端-店铺接口")
@RequestMapping("/buyer/store/store")
public class StoreBuyerController {

    /** 店铺 */
    @Autowired
    private IFeignStoreApi storeService;
    /** 店铺商品分类 */
    @Autowired
    private StoreGoodsLabelApi storeGoodsLabelApi;
    /** 店铺详情 */
    @Autowired
    private IFeignStoreDetailApi storeDetailApi;

    @Operation(summary = "获取店铺列表分页", description = "获取店铺列表分页")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<StoreVO>> getByPage(StorePageQuery storePageQuery) {
        return Result.success(storeService.findByConditionPage(storePageQuery));
    }

    @Operation(summary = "通过id获取店铺信息", description = "通过id获取店铺信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/detail/{id}")
    public Result<StoreBasicInfoVO> detail(@NotNull @PathVariable String id) {
        return Result.success(storeDetailApi.getStoreBasicInfoDTO(id));
    }

    @Operation(summary = "通过id获取店铺详细信息-营业执照", description = "通过id获取店铺详细信息-营业执照")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/licence-photo/{id}")
    public Result<StoreOtherVO> licencePhoto(@Parameter(description = "店铺ID") @NotNull @PathVariable String id) {
        return Result.success(storeDetailApi.getStoreOtherVO(id));
    }

    @Operation(summary = "通过id获取店铺商品分类", description = "通过id获取店铺商品分类")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/label/{id}")
    public Result<List<StoreGoodsLabelVO>> storeGoodsLabel(
            @Parameter(description = "店铺ID") @NotNull @PathVariable String id) {
        return Result.success(storeGoodsLabelApi.listByStoreId(id));
    }

    @Operation(summary = "申请店铺第一步-填写企业信息", description = "申请店铺第一步-填写企业信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/apply/first")
    public Result<Boolean> applyFirstStep(@RequestBody StoreCompanyDTO storeCompanyDTO) {
        return Result.success(storeService.applyFirstStep(storeCompanyDTO));
    }

    @Operation(summary = "申请店铺第二步-填写银行信息", description = "申请店铺第二步-填写银行信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/apply/second")
    public Result<Boolean> applyFirstStep(@RequestBody StoreBankDTO storeBankDTO) {
        return Result.success(storeService.applySecondStep(storeBankDTO));
    }

    @Operation(summary = "申请店铺第三步-填写其他信息", description = "申请店铺第三步-填写其他信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/apply/third")
    public Result<Boolean> applyFirstStep(@RequestBody StoreOtherInfoDTO storeOtherInfoDTO) {
        return Result.success(storeService.applyThirdStep(storeOtherInfoDTO));
    }

    @Operation(summary = "获取当前登录会员的店铺信息-入驻店铺", description = "获取当前登录会员的店铺信息-入驻店铺")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/apply")
    public Result<StoreDetailVO> apply() {
        return Result.success(storeDetailApi.getStoreDetailByMemberId(SecurityUtils.getUserId()));
    }
}
