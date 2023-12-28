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

package com.taotao.cloud.payment.biz.demo.service.wxpay;

import com.yungouos.pay.common.PayException;
import com.yungouos.pay.wxpay.WxPay;
import com.yungouos.springboot.demo.config.WxPayConfig;
import com.yungouos.springboot.demo.entity.Order;
import com.yungouos.springboot.demo.service.order.OrderService;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private OrderService orderService;

    @Override
    public Map<String, Object> nativePay(String body, String money) {
        Map<String, Object> map = null;
        try {
            Order order = orderService.add(body, money);
            if (order == null) {
                throw new Exception("订单保存失败");
            }
            map = new HashMap<String, Object>();
            String notify_url = "http://yungouos.wicp.net/api/callback/notify";
            String result = WxPay.nativePay(
                    order.getOrderNo(),
                    order.getMoney(),
                    WxPayConfig.mchId,
                    order.getBody(),
                    "2",
                    null,
                    notify_url,
                    null,
                    null,
                    null,
                    null,
                    WxPayConfig.key);
            map.put("url", result);
            map.put("orderNo", order.getOrderNo());
        } catch (PayException e) {
            LogUtils.error(e);
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return map;
    }
}
