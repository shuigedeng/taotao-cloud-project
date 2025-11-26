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

package com.taotao.cloud.workflow.biz.flowable.api.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.servlet.RequestUtils;
import com.taotao.cloud.order.api.feign.IFeignStoreFlowApi;
import com.taotao.cloud.order.api.web.vo.order.StoreFlowVO;
import com.taotao.cloud.store.api.model.query.BillPageQuery;
import com.taotao.cloud.store.api.model.vo.BillListVO;
import com.taotao.cloud.store.biz.model.entity.Bill;
import com.taotao.cloud.store.biz.service.BillService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,结算单接口
 *
 * @since 2020/11/17 4:29 下午
 */
@Validated
@RestController
@Tag(name = "店铺端-结算单接口", description = "店铺端-结算单接口")
@RequestMapping("/store/bill")
public class BillStoreController {

    @Autowired
    private BillService billService;

    @Autowired
    private IFeignStoreFlowApi storeFlowApi;

    @Operation(summary = "获取结算单分页", description = "获取结算单分页")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/getByPage")
    public Result<PageResult<BillListVO>> getByPage(BillPageQuery billPageQuery) {
        billPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
        IPage<BillListVO> billListVOIPage = billService.billPage(billPageQuery);
        return Result.success(MpUtils.convertMybatisPage(billListVOIPage, BillListVO.class));
    }

    @Operation(summary = "通过id获取结算单", description = "通过id获取结算单")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/get/{id}")
    public Result<Bill> get(@PathVariable String id) {
        return Result.success(OperationalJudgment.judgment(billService.getById(id)));
    }

    @Operation(summary = "获取商家结算单流水分页", description = "获取商家结算单流水分页")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}/getStoreFlow")
    public Result<PageResult<StoreFlowVO>> getStoreFlow(
            @PathVariable String id, @Parameter(description = "流水类型:PAY、REFUND") String flowType, PageQuery PageQuery) {
        OperationalJudgment.judgment(billService.getById(id));
        IPage<StoreFlowVO> storeFlow = storeFlowApi.getStoreFlow(id, flowType, PageQuery);
        return Result.success(MpUtils.convertMybatisPage(storeFlow, StoreFlowVO.class));
    }

    @Operation(summary = "获取商家分销订单流水分页", description = "获取商家分销订单流水分页")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}/getDistributionFlow")
    public Result<PageResult<StoreFlowVO>> getDistributionFlow(@PathVariable String id, PageQuery PageQuery) {
        OperationalJudgment.judgment(billService.getById(id));
        IPage<StoreFlowVO> distributionFlow = storeFlowApi.getDistributionFlow(id, PageQuery);
        return Result.success(MpUtils.convertMybatisPage(distributionFlow, StoreFlowVO.class));
    }

    @Operation(summary = "核对结算单", description = "核对结算单")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/check/{id}")
    public Result<Boolean> examine(@PathVariable String id) {
        OperationalJudgment.judgment(billService.getById(id));
        billService.check(id);
        return Result.success(true);
    }

    @Operation(summary = "下载结算单", description = "下载结算单")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/downLoad/{id}")
    public void downLoadDeliverExcel(@PathVariable String id) {
        OperationalJudgment.judgment(billService.getById(id));
        HttpServletResponse response = RequestUtils.getResponse();
        billService.download(response, id);
    }
}
