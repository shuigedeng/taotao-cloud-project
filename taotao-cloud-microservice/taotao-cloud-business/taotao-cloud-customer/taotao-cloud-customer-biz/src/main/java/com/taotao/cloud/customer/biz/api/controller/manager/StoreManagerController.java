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

package com.taotao.cloud.customer.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.store.api.model.dto.AdminStoreApplyDTO;
import com.taotao.cloud.store.api.model.dto.StoreEditDTO;
import com.taotao.cloud.store.api.model.query.StorePageQuery;
import com.taotao.cloud.store.api.model.vo.StoreDetailVO;
import com.taotao.cloud.store.api.model.vo.StoreManagementCategoryVO;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import com.taotao.cloud.store.biz.model.entity.Store;
import com.taotao.cloud.store.biz.service.StoreDetailService;
import com.taotao.cloud.store.biz.service.StoreService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端,店铺管理接口
 *
 * @since 2020/12/6 16:09
 */
@Validated
@RestController
@Tag(name = "管理端-店铺管理接口", description = "管理端-店铺管理接口")
@RequestMapping("/manager/store")
public class StoreManagerController {

    /** 店铺 */
    @Autowired
    private StoreService storeService;
    /** 店铺详情 */
    @Autowired
    private StoreDetailService storeDetailService;

    @Operation(summary = "获取店铺分页列表", description = "获取店铺分页列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/all")
    public Result<List<Store>> getAll() {
        return Result.success(storeService.list(new QueryWrapper<Store>().eq("store_disable", "OPEN")));
    }

    @Operation(summary = "获取店铺分页列表", description = "获取店铺分页列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<StoreVO>> getByPage(StorePageQuery storePageQuery) {
        IPage<StoreVO> storeVOIPage = storeService.findByConditionPage(storePageQuery);
        return Result.success(MpUtils.convertMybatisPage(storeVOIPage, StoreVO.class));
    }

    @Operation(summary = "获取店铺详情", description = "获取店铺详情")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/detail/{storeId}")
    public Result<StoreDetailVO> detail(@PathVariable String storeId) {
        return Result.success(storeDetailService.getStoreDetailVO(storeId));
    }

    @Operation(summary = "添加店铺", description = "添加店铺")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Store> add(@Valid @RequestBody AdminStoreApplyDTO adminStoreApplyDTO) {
        return Result.success(storeService.add(adminStoreApplyDTO));
    }

    @Operation(summary = "编辑店铺", description = "编辑店铺")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/{id}")
    public Result<Store> edit(@PathVariable String id, @Valid @RequestBody  StoreEditDTO storeEditDTO) {
        storeEditDTO.setStoreId(id);
        return Result.success(storeService.edit(storeEditDTO));
    }

    @Operation(summary = "审核店铺申请", description = "审核店铺申请")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/audit/{id}/{passed}")
    public Result<Boolean> audit(
            @Parameter(description = "是否通过审核 0 通过 1 拒绝 编辑操作则不需传递") @PathVariable String id,
            @Parameter(description = "店铺id") @PathVariable Integer passed) {
        return Result.success(storeService.audit(id, passed));
    }

    @Operation(summary = "关闭店铺", description = "关闭店铺")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/disable/{id}")
    public Result<Boolean> disable(@PathVariable String id) {
        return Result.success(storeService.disable(id));
    }

    @Operation(summary = "开启店铺", description = "开启店铺")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/enable/{id}")
    public Result<Boolean> enable(@PathVariable String id) {
        return Result.success(storeService.enable(id));
    }

    @Operation(summary = "查询一级分类列表", description = "查询一级分类列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/management-category/{storeId}")
    public Result<List<StoreManagementCategoryVO>> firstCategory(@PathVariable String storeId) {
        return Result.success(this.storeDetailService.goodsManagementCategory(storeId));
    }

    @Operation(summary = "根据会员id查询店铺信息", description = "根据会员id查询店铺信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/member/{memberId}")
    public Result<Store> getByMemberId(@Valid @PathVariable String memberId) {
        List<Store> list = storeService.list(new QueryWrapper<Store>().eq("member_id", memberId));
        if (list.size() > 0) {
            return Result.success(list.get(0));
        }
        return Result.success(null);
    }
}
