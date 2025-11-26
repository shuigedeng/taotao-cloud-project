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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.service.RefundLogService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 管理端,退款日志接口 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-退款日志接口", description = "管理端-退款日志接口I")
@RequestMapping("/manager/order/refundLog")
public class RefundLogManagerController {
    @Autowired
    private RefundLogService refundLogService;

    @Operation(summary = "查看退款日志详情", description = "查看退款日志详情")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/{id}")
    public Result<RefundLog> get(@PathVariable String id) {
        return Result.success(refundLogService.getById(id));
    }

    @Operation(summary = "分页获取退款日志", description = "分页获取退款日志")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<IPage<RefundLog>> getByPage(RefundLog entity, SearchVO searchVo, PageVO page) {
        return Result.success(refundLogService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo)));
    }
}
