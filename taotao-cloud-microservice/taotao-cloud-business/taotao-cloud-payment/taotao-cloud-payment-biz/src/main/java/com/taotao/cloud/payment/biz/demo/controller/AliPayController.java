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


import com.alibaba.fastjson.JSONObject;
import com.yungouos.springboot.demo.common.ApiResponse;
import com.yungouos.springboot.demo.service.alipay.AliPayService;
import jakarta.annotation.Resource;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alipay")
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    @ResponseBody
    @RequestMapping("/nativePay")
    public JSONObject nativePay(@RequestParam Map<String, String> data) {
        JSONObject response = ApiResponse.init();
        try {
            String body = data.get("body");
            String money = data.get("money");
            if (StrUtil.isBlank(body)) {
                response = ApiResponse.fail("body is not null");
                return response;
            }
            if (StrUtil.isBlank(money)) {
                response = ApiResponse.fail("money is not null");
                return response;
            }
            Map<String, Object> map = aliPayService.nativePay(body, money);
            response = ApiResponse.success("支付宝下单成功", map);
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return response;
    }

    @ResponseBody
    @RequestMapping("/mobilePay")
    public JSONObject mobilePay(@RequestParam Map<String, String> data) {
        JSONObject response = ApiResponse.init();
        try {
            String body = data.get("body");
            String money = data.get("money");
            if (StrUtil.isBlank(body)) {
                response = ApiResponse.fail("body is not null");
                return response;
            }
            if (StrUtil.isBlank(money)) {
                response = ApiResponse.fail("money is not null");
                return response;
            }
            Map<String, Object> map = aliPayService.mobilePay(body, money);
            response = ApiResponse.success("支付宝下单成功", map);
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return response;
    }
}
