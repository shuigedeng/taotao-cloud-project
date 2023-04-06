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

package com.taotao.cloud.payment.biz.pay.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jpay.alipay.AliPayApi;
import com.jpay.alipay.AliPayApiConfigKit;
import com.xhuicloud.common.authorization.resource.annotation.Anonymous;
import com.xhuicloud.pay.entity.PayOrderAll;
import com.xhuicloud.pay.service.PayOrderAllService;
import com.xhuicloud.pay.utils.OrderUtil;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: XHuiCloud
 * @description: PayNotifyRecordController
 * @author: Sinda
 * @create: 2020-06-05 11:00
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/notify")
@Api(value = "notify", tags = "异步回调管理")
public class PayNotifyRecordController {

    private final PayOrderAllService payOrderAllService;

    /**
     * 同步 用于支付完成后返回的页面
     *
     * @param request
     * @return
     */
    @Anonymous(false)
    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/alipay/return_url")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response) {
        // 获取支付宝反馈信息
        Map<String, String> map = AliPayApi.toMap(request);
        if (AlipaySignature.rsaCheckV1(
                map, AliPayApiConfigKit.getApiConfig(map.get("app_id")).getAlipayPublicKey(), "UTF-8", "RSA2")) {
            // TODO 支付成功
            response.getWriter().print("success");
        }
        return "fail";
    }

    /**
     * 异步
     *
     * @param request
     * @return
     */
    @Anonymous(false)
    @SneakyThrows
    @ResponseBody
    @RequestMapping(value = "/alipay/notify_url")
    public void notifyUrl(HttpServletRequest request) {
        // 获取支付宝POST过来反馈信息
        Map<String, String> map = AliPayApi.toMap(request);
        if (AlipaySignature.rsaCheckV1(
                map, AliPayApiConfigKit.getApiConfig(map.get("app_id")).getAlipayPublicKey(), "UTF-8", "RSA2")) {
            // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理

            // 校验是否有这个订单
            PayOrderAll payOrderAll = payOrderAllService.getOne(
                    Wrappers.<PayOrderAll>lambdaQuery().eq(PayOrderAll::getOrderNo, map.get("out_trade_no")));

            OrderUtil.checkOrder(payOrderAll, map);
        }
    }
}
