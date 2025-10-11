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
import com.taotao.cloud.order.sys.model.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.sys.model.page.order.ReceiptPageQuery;
import com.taotao.cloud.order.biz.service.business.order.IReceiptService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,发票记录API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:24
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-发票记录管理API", description = "管理端-发票记录管理API")
@RequestMapping("/order/manager/receipt")
public class ReceiptController {

    private final IReceiptService receiptService;

    @Operation(summary = "获取发票分页信息", description = "获取发票分页信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/tree")
    public Result<PageResult<OrderReceiptDTO>> getPage(ReceiptPageQuery receiptPageQuery) {
        IPage<OrderReceiptDTO> page = this.receiptService.pageQuery(receiptPageQuery);
        return Result.success(MpUtils.convertMybatisPage(page, OrderReceiptDTO.class));
    }
}
