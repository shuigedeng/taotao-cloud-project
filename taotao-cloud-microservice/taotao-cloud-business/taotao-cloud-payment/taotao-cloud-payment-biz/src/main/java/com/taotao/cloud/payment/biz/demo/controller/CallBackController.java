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

package com.taotao.cloud.payment.biz.demo.controller;


import com.yungouos.pay.util.PaySignUtil;
import com.yungouos.springboot.demo.config.AliPayConfig;
import com.yungouos.springboot.demo.config.WxPayConfig;
import com.yungouos.springboot.demo.service.order.OrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/callback")
public class CallBackController {

    @Resource
    private OrderService orderService;

    @RequestMapping("/notify")
    public String notify(
            @RequestParam Map<String, String> data, HttpServletRequest request, HttpServletResponse response) {
        try {

            LogUtils.info("接受到支付结果回调");
            LogUtils.info(data.toString());

            String payChannel = data.get("payChannel");

            String attach = data.get("attach");

            if (StrUtil.isBlank(payChannel)) {
                return "payChannel is not null";
            }

            String key = null;
            if ("wxpay".equals(payChannel)) {
                key = WxPayConfig.key;
            }
            if ("alipay".equals(payChannel)) {
                key = AliPayConfig.key;
            }

            boolean sign = PaySignUtil.checkNotifySign(request, key);

            LogUtils.info("签名验证：" + sign);

            if (!sign) {
                return "sign fail";
            }

            String outTradeNo = data.get("outTradeNo");
            String payNo = data.get("payNo");
            String time = data.get("time");
            String code = data.get("code");

            if (Integer.valueOf(code).intValue() != 1) {
                return "pay fail";
            }

            boolean success = orderService.paySuccess(outTradeNo, payNo, time);
            if (!success) {
                return "fail";
            }
            PrintWriter out = response.getWriter();
            out.print("SUCCESS");
            return null;
        } catch (Exception e) {
            LogUtils.error(e);
        }

        return null;
    }
}
