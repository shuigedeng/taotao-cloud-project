package com.taotao.cloud.payment.biz.daxpay.single.admin.controller.channel;

import com.alipay.api.domain.LabelValue;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.cash.service.CashPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.cash.CashPayConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.wechat.WalletConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 现金支付配置
 * @author xxm
 * @since 2024/2/17
 */
@Tag(name = "现金支付配置")
@RestController
@RequestMapping("/cash/config")
@RequiredArgsConstructor
public class CashConfigController {
    private final CashPayConfigService service;


    @Operation(summary = "获取配置")
    @GetMapping("/getConfig")
    public ResResult<CashPayConfigDto> getConfig() {
        return Res.ok(service.getConfig().toDto());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WalletConfigParam param) {
        service.update(param);
        return Res.ok();
    }

    @Operation(summary = "支付宝支持支付方式")
    @GetMapping("/findPayWays")
    public ResResult<List<LabelValue>> findPayWays() {
        return Res.ok(service.findPayWays());
    }
}
