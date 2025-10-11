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

package com.taotao.cloud.order.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.order.sys.model.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.sys.model.page.order.ReceiptPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.Receipt;
import com.taotao.cloud.order.biz.service.business.order.IOrderService;
import com.taotao.cloud.order.biz.service.business.order.IReceiptService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,发票API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:45
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-发票API", description = "店铺端-发票API")
@RequestMapping("/order/seller/receipt")
public class ReceiptController {

    private final IReceiptService receiptService;

    private final IOrderService orderService;

    @Operation(summary = "分页获取", description = "分页获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<OrderReceiptDTO>> getByPage(ReceiptPageQuery receiptPageQuery) {
        receiptPageQuery.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
        IPage<OrderReceiptDTO> page = receiptService.pageQuery(receiptPageQuery);
        return Result.success(MpUtils.convertMybatisPage(page, OrderReceiptDTO.class));
    }

    @Operation(summary = "通过id获取", description = "通过id获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}")
    public Result<Receipt> get(@PathVariable String id) {
        return Result.success(OperationalJudgment.judgment(receiptService.getById(id)));
    }

    @Operation(summary = "开发票", description = "开发票")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/{id}/invoicing")
    public Result<Receipt> invoicing(@PathVariable Long id) {
        OperationalJudgment.judgment(receiptService.getById(id));
        return Result.success(receiptService.invoicing(id));
    }

    @Operation(summary = "通过订单编号获取", description = "通过订单编号获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/orderSn/{orderSn}")
    public Result<Receipt> getByOrderSn(@PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return Result.success(receiptService.getByOrderSn(orderSn));
    }
}
