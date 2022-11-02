/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
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
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
        if (AlipaySignature.rsaCheckV1(map,
                AliPayApiConfigKit.getApiConfig(map.get("app_id")).getAlipayPublicKey()
                , "UTF-8",
                "RSA2")) {
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
        if (AlipaySignature.rsaCheckV1(map, AliPayApiConfigKit.getApiConfig(map.get("app_id")).getAlipayPublicKey(), "UTF-8", "RSA2")) {
            // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理

            // 校验是否有这个订单
            PayOrderAll payOrderAll = payOrderAllService.getOne(Wrappers.<PayOrderAll>lambdaQuery()
                    .eq(PayOrderAll::getOrderNo, map.get("out_trade_no")));

            OrderUtil.checkOrder(payOrderAll,map);


        }
    }

}
