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

package com.taotao.cloud.order.facade.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderDetailCO;
import com.taotao.cloud.order.application.command.order.dto.OrderPageQry;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderSimpleCO;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.web.request.annotation.RequestLogger;
import com.taotao.boot.web.utils.OperationalJudgment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zipkin2.storage.Traces;

/**
 * 买家端,订单API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:00
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-订单API", description = "买家端-订单API")
@RequestMapping("/order/buyer/order")
public class OrderController {

    private final IOrderService orderService;

    @Operation(summary = "查询会员订单列表", description = "查询会员订单列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<OrderSimpleCO>> queryMineOrder(OrderPageQry orderPageQry) {
        SecurityUser currentUser = SecurityUtils.getCurrentUser();
        orderPageQry.setMemberId(currentUser.getUserId());
        IPage<OrderSimpleCO> page = orderService.pageQuery(orderPageQry);
        return Result.success(MpUtils.convertMybatisPage(page, OrderSimpleCO.class));
    }

    @Operation(summary = "订单明细", description = "订单明细")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{orderSn}")
    public Result<OrderDetailCO> detail(@NotNull(message = "订单编号不能为空") @PathVariable("orderSn") String orderSn) {
        OrderDetailCO orderDetailCO = orderService.queryDetail(orderSn);
        OperationalJudgment.judgment(orderDetailCO.order());
        return Result.success(orderDetailCO);
    }

    @Operation(summary = "确认收货", description = "确认收货")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/{orderSn}/receiving")
    public Result<Boolean> receiving(@NotNull(message = "订单编号不能为空") @PathVariable("orderSn") String orderSn) {
        Order order = orderService.getBySn(orderSn);
        if (order == null) {
            throw new BusinessException(ResultEnum.ORDER_NOT_EXIST);
        }
        // 判定是否是待收货状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.DELIVERED.name())) {
            throw new BusinessException(ResultEnum.ORDER_DELIVERED_ERROR);
        }
        orderService.complete(orderSn);
        return Result.success(true);
    }

    @Operation(summary = "取消订单", description = "取消订单")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/{orderSn}/cancel")
    public Result<Boolean> cancel(@PathVariable String orderSn, @RequestParam String reason) {
        orderService.cancel(orderSn, reason);
        return Result.success(true);
    }

    @Operation(summary = "删除订单", description = "删除订单")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping(value = "/{orderSn}")
    public Result<Boolean> deleteOrder(@PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        orderService.deleteOrder(orderSn);
        return Result.success(true);
    }

    @Operation(summary = "查询物流踪迹", description = "查询物流踪迹")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/traces/{orderSn}")
    public Result<Traces> getTraces(@NotBlank(message = "订单编号不能为空") @PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return Result.success(orderService.getTraces(orderSn));
    }

    @Operation(summary = "开票", description = "开票")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/receipt/{orderSn}")
    public Result<Boolean> invoice(@NotBlank(message = "订单编号不能为空") @PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return Result.success(orderService.invoice(orderSn));
    }
}
