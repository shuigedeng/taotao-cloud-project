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

package com.taotao.cloud.payment.biz.controller.manger;

import org.dromara.hutoolcore.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.order.api.feign.OrderApi;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 管理端,收款日志接口 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-收款日志接口", description = "管理端-收款日志接口")
@RequestMapping("/manager/order/paymentLog")
public class PaymentLogManagerController {

    @Autowired
    private OrderApi orderApi;

    @Operation(summary = "分页获取支付日志", description = "分页获取支付日志")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<IPage<PaymentLog>> getByPage(OrderVO order, SearchVO searchVo, PageVO page) {
        return Result.success(
                orderApi.queryPaymentLogs(PageUtil.initPage(page), PageUtil.initWrapper(order, searchVo)));
    }
}
