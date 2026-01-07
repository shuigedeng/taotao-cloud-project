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

package com.taotao.cloud.distribution.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.distribution.api.model.query.DistributionGoodsPageQuery;
import com.taotao.cloud.distribution.api.model.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.service.DistributionGoodsService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 管理端,分销商品管理接口 */
@Validated
@RestController
@Tag(name = "管理端-分销商品管理接口", description = "管理端-分销商品管理接口")
@RequestMapping("/manager/distribution/goods")
public class DistributionGoodsManagerController {

    @Autowired
    private DistributionGoodsService distributionGoodsService;

    @Operation(summary = "分页获取", description = "分页获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/getByPage")
    public Result<IPage<DistributionGoodsVO>> getByPage(DistributionGoodsPageQuery distributionGoodsPageQuery) {
        return Result.success(distributionGoodsService.goodsPage(distributionGoodsPageQuery));
    }

    @Operation(summary = "批量删除", description = "批量删除")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping(value = "/delByIds/{ids}")
    public Result<Object> delAllByIds(@PathVariable List ids) {
        distributionGoodsService.removeByIds(ids);
        return Result.success();
    }
}
