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

package com.taotao.cloud.payment.biz.bootx.controller;

import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service.WalletLogService;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.wallet.WalletLogDto;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.wallet.WalletLogQueryParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钱包日志相关接口
 *
 * @author xxm
 * @date 2020/12/8
 */
@Tag(name = "钱包日志相关的接口")
@RestController
@RequestMapping("/wallet/log")
@AllArgsConstructor
public class WalletLogController {
    private final WalletLogService walletLogService;

    @Operation(summary = "个人钱包日志")
    @PostMapping("/pageByPersonal")
    public ResResult<PageResult<WalletLogDto>> pageByPersonal(
            @ParameterObject PageQuery PageQuery, @ParameterObject WalletLogQueryParam param) {
        return Res.ok(walletLogService.pageByPersonal(PageQuery, param));
    }

    @Operation(summary = "查询钱包日志(分页)")
    @GetMapping("/page")
    public ResResult<PageResult<WalletLogDto>> page(
            @ParameterObject PageQuery PageQuery, @ParameterObject WalletLogQueryParam param) {
        return Res.ok(walletLogService.page(PageQuery, param));
    }

    @Operation(summary = "根据钱包id查询钱包日志(分页)")
    @GetMapping("/pageByWalletId")
    public ResResult<PageResult<WalletLogDto>> pageByWalletId(
            @ParameterObject PageQuery PageQuery, @ParameterObject WalletLogQueryParam param) {
        return Res.ok(walletLogService.pageByWalletId(PageQuery, param));
    }
}
