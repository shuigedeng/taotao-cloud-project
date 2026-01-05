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

package com.taotao.cloud.order.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.order.sys.model.page.aftersale.AfterSalePageQuery;
import com.taotao.cloud.order.sys.model.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.biz.model.convert.AfterSaleConvert;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.service.business.aftersale.AfterSaleService;
import com.taotao.cloud.store.api.model.vo.StoreAfterSaleAddressVO;
import com.taotao.cloud.sys.api.model.vo.logistics.TracesVO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,售后API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:09
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-售后管理API", description = "管理端-售后管理API")
@RequestMapping("/order/manager/aftersale")
public class AfterSaleController {

    /** 售后 */
    private final AfterSaleService afterSaleService;

    @Operation(summary = "分页获取售后服务", description = "分页获取售后服务")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/page")
    public Result<PageResult<AfterSaleVO>> pageQuery(AfterSalePageQuery searchParams) {
        IPage<AfterSale> page = afterSaleService.pageQuery(searchParams);
        return Result.success(MpUtils.convertMybatisPage(page, AfterSaleVO.class));
    }

    @Operation(summary = "获取导出售后服务列表列表", description = "获取导出售后服务列表列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/exportAfterSaleOrder")
    public Result<List<AfterSaleVO>> exportAfterSaleOrder(AfterSalePageQuery afterSalePageQuery) {
        List<AfterSale> afterSales = afterSaleService.exportAfterSaleOrder(afterSalePageQuery);
        return Result.success(AfterSaleConvert.INSTANCE.convert(afterSales));
    }

    @Operation(summary = "查看售后服务详情", description = "查看售后服务详情")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{sn}")
    public Result<AfterSaleVO> get(@NotNull(message = "售后单号") @PathVariable("sn") String sn) {
        AfterSale afterSale = afterSaleService.getAfterSaleBySn(sn);
        return Result.success(AfterSaleConvert.INSTANCE.convert(afterSale));
    }

    @Operation(summary = "查看买家退货物流踪迹", description = "查看买家退货物流踪迹")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/delivery/traces/{sn}")
    public Result<TracesVO> getDeliveryTraces(@PathVariable String sn) {
        return Result.success(afterSaleService.deliveryTraces(sn));
    }

    @Operation(summary = "售后线下退款", description = "售后线下退款")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/refund/{afterSaleSn}")
    public Result<Boolean> refund(
            @NotNull(message = "请选择售后单") @PathVariable String afterSaleSn, @RequestParam String remark) {
        return Result.success(afterSaleService.refund(afterSaleSn, remark));
    }

    @Operation(summary = "审核售后申请", description = "审核售后申请")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/review/{afterSaleSn}")
    public Result<Boolean> review(
            @NotNull(message = "请选择售后单") @PathVariable String afterSaleSn,
            @NotNull(message = "请审核") String serviceStatus,
            String remark,
            BigDecimal actualRefundPrice) {
        return Result.success(afterSaleService.review(afterSaleSn, serviceStatus, remark, actualRefundPrice));
    }

    @Operation(summary = "获取商家售后收件地址", description = "获取商家售后收件地址")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/getStoreAfterSaleAddress/{sn}")
    public Result<StoreAfterSaleAddressVO> getStoreAfterSaleAddress(
            @NotNull(message = "售后单号不能为空") @PathVariable("sn") String sn) {
        return Result.success(afterSaleService.getStoreAfterSaleAddressVO(sn));
    }
}
