package com.taotao.cloud.payment.biz.daxpay.single.service.controller.constant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import com.taotao.cloud.payment.biz.daxpay.service.param.constant.MerchantNotifyConstQuery;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.MerchantNotifyConstResult;
import com.taotao.cloud.payment.biz.daxpay.service.service.constant.MerchantNotifyConstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户订阅通知类型
 * @author xxm
 * @since 2024/8/5
 */
@RequestGroup(groupCode = "PayConst", moduleCode = "PayConfig")
@Tag(name = "商户订阅通知类型")
@RestController
@RequestMapping("/const/merchant/notify")
@RequiredArgsConstructor
public class MerchantNotifyConstController {

    private final MerchantNotifyConstService merchantNotifyConstService;

    @RequestPath("商户订阅通知类型分页")
    @Operation(summary = "商户订阅通知类型分页")
    @GetMapping("/page")
    public Result<PageResult<MerchantNotifyConstResult>> page(PageParam pageParam, MerchantNotifyConstQuery query) {
        return Res.ok(merchantNotifyConstService.page(pageParam, query));
    }
}
