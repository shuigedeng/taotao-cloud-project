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

import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.service.AliPayCallbackService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.service.WeChatPayCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @date 2021/2/27
 */
@Slf4j
@Tag(name = "支付回调")
@RestController
@RequestMapping("/pay/callback")
@AllArgsConstructor
public class PayCallbackController {
    private final AliPayCallbackService aliPayCallbackService;
    private final WeChatPayCallbackService weChatPayCallbackService;

    @SneakyThrows
    @Operation(summary = "支付宝回调")
    @PostMapping("/aliPay")
    public String aliPay(HttpServletRequest request) {
        Map<String, String> stringStringMap = AliPayApi.toMap(request);
        return aliPayCallbackService.payCallback(stringStringMap);
    }

    @SneakyThrows
    @Operation(summary = "微信支付回调")
    @PostMapping("/wechat")
    public String wechat(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        return weChatPayCallbackService.payCallback(params);
    }
}
