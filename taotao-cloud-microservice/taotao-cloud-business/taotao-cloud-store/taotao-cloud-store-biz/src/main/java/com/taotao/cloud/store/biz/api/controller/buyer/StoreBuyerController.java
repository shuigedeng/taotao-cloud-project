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

package com.taotao.cloud.store.biz.api.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.goods.api.feign.StoreGoodsLabelApi;
import com.taotao.cloud.goods.api.model.vo.StoreGoodsLabelVO;
import com.taotao.cloud.store.api.model.dto.StoreBankDTO;
import com.taotao.cloud.store.api.model.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.model.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.model.query.StorePageQuery;
import com.taotao.cloud.store.api.model.vo.StoreBasicInfoVO;
import com.taotao.cloud.store.api.model.vo.StoreDetailVO;
import com.taotao.cloud.store.api.model.vo.StoreOtherVO;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import com.taotao.cloud.store.biz.service.IStoreDetailService;
import com.taotao.cloud.store.biz.service.IStoreService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 买家端,店铺接口 */
@Validated
@RestController
@Tag(name = "买家端-店铺接口", description = "买家端-店铺接口")
@RequestMapping("/buyer/store/store")
public class StoreBuyerController {

    /** 店铺 */
    @Autowired
    private IStoreService storeService;
    /** 店铺商品分类 */
    @Autowired
    private StoreGoodsLabelApi storeGoodsLabelApi;
    /** 店铺详情 */
    @Autowired
    private IStoreDetailService storeDetailService;

    @Operation(summary = "获取店铺列表分页", description = "获取店铺列表分页")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<PageResult<StoreVO>> getByPage(StorePageQuery storePageQuery) {
        IPage<StoreVO> storeVOIPage = storeService.findByConditionPage(storePageQuery);
        return Result.success(MpUtils.convertMybatisPage(storeVOIPage, StoreVO.class));
    }

    @Operation(summary = "通过id获取店铺信息", description = "通过id获取店铺信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/get/detail/{id}")
    public Result<StoreBasicInfoVO> detail(@NotNull @PathVariable String id) {
        return Result.success(storeDetailService.getStoreBasicInfoDTO(id));
    }

    @Operation(summary = "通过id获取店铺详细信息-营业执照", description = "通过id获取店铺详细信息-营业执照")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/get/licencePhoto/{id}")
    public Result<StoreOtherVO> licencePhoto(@Parameter(description = "店铺ID") @NotNull @PathVariable String id) {
        return Result.success(storeDetailService.getStoreOtherVO(id));
    }

    @Operation(summary = "通过id获取店铺商品分类", description = "通过id获取店铺商品分类")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/label/get/{id}")
    public Result<List<StoreGoodsLabelVO>> storeGoodsLabel(
            @Parameter(description = "店铺ID") @NotNull @PathVariable String id) {
        return Result.success(storeGoodsLabelApi.listByStoreId(id));
    }

    @Operation(summary = "申请店铺第一步-填写企业信息", description = "申请店铺第一步-填写企业信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/apply/first")
    public Result<Boolean> applyFirstStep(StoreCompanyDTO storeCompanyDTO) {
        return Result.success(storeService.applyFirstStep(storeCompanyDTO));
    }

    @Operation(summary = "申请店铺第二步-填写银行信息", description = "申请店铺第二步-填写银行信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/apply/second")
    public Result<Boolean> applyFirstStep(StoreBankDTO storeBankDTO) {
        return Result.success(storeService.applySecondStep(storeBankDTO));
    }

    @Operation(summary = "申请店铺第三步-填写其他信息", description = "申请店铺第三步-填写其他信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/apply/third")
    public Result<Boolean> applyFirstStep(StoreOtherInfoDTO storeOtherInfoDTO) {
        return Result.success(storeService.applyThirdStep(storeOtherInfoDTO));
    }

    @Operation(summary = "获取当前登录会员的店铺信息-入驻店铺", description = "获取当前登录会员的店铺信息-入驻店铺")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/apply")
    public Result<StoreDetailVO> apply() {
        return Result.success(storeDetailService.getStoreDetailVOByMemberId(SecurityUtils.getUserId()));
    }
}
