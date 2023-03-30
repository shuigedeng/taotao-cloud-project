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

import cn.hutool.db.PageResult;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service.WalletQueryService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service.WalletService;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.wallet.WalletDto;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.wallet.WalletInfoDto;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.wallet.WalletPayParam;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.wallet.WalletRechargeParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 钱包
 *
 * @author xxm
 * @date 2021/2/24
 */
@Tag(name = "钱包相关的接口")
@RestController
@RequestMapping("wallet")
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletQueryService walletQueryService;

    @Operation(summary = "开通用户钱包操作")
    @PostMapping("createWallet")
    public ResResult<Void> createWallet(Long userId) {
        walletService.createWallet(userId);
        return Res.ok();
    }

    @Operation(summary = "批量开通用户钱包操作")
    @PostMapping("createWalletBatch")
    public ResResult<Void> createWalletBatch(@RequestBody List<Long> userIds) {
        walletService.createWalletBatch(userIds);
        return Res.ok();
    }

    @Operation(summary = "解锁钱包")
    @OperateLog(title = "解锁钱包", businessType = BusinessType.UPDATE, saveParam = true)
    @PostMapping("/unlock")
    public ResResult<Void> unlock(Long walletId) {
        walletService.unlock(walletId);
        return Res.ok();
    }

    @Operation(summary = "锁定钱包")
    @OperateLog(title = "锁定钱包", businessType = BusinessType.UPDATE, saveParam = true)
    @PostMapping("/lock")
    public ResResult<Void> lock(Long walletId) {
        walletService.lock(walletId);
        return Res.ok();
    }

    @Operation(summary = "充值操作(增减余额)")
    @PostMapping("/changerBalance")
    public ResResult<Void> changerBalance(@RequestBody WalletRechargeParam param) {
        walletService.changerBalance(param);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WalletDto>> page(PageQuery PageQuery, WalletPayParam param) {
        return Res.ok(walletQueryService.page(PageQuery, param));
    }

    @Operation(summary = "分页")
    @GetMapping("/pageByNotWallet")
    public ResResult<PageResult<UserInfoDto>> pageByNotWallet(
            PageQuery PageQuery, UserInfoParam param) {
        return Res.ok(walletQueryService.pageByNotWallet(PageQuery, param));
    }

    @Operation(summary = "根据用户查询钱包")
    @GetMapping("/findByUser")
    public ResResult<WalletDto> findByUser() {
        return Res.ok(walletQueryService.findByUser());
    }

    @Operation(summary = "根据钱包ID查询钱包")
    @GetMapping("/findById")
    public ResResult<WalletDto> findById(Long walletId) {
        return Res.ok(walletQueryService.findById(walletId));
    }

    @Operation(summary = "获取钱包综合信息")
    @GetMapping("/getWalletInfo")
    public ResResult<WalletInfoDto> getWalletInfo(Long walletId) {
        return Res.ok(walletQueryService.getWalletInfo(walletId));
    }
}
