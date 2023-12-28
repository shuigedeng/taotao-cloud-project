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

package com.taotao.cloud.payment.biz.demo.test;

import com.yungouos.pay.entity.PayOrder;
import com.yungouos.pay.order.SystemOrder;

/**
 * 订单接口调用演示
 *
 * @author YunGouOS技术部-029
 */
public class OrderTest {

    public static void main(String[] args) {

        // 商户号可以是支付宝也可以是微信
        String mch_id = "商户号";
        // 商户密钥
        String key = "支付密钥";

        /** 查询订单 */
        try {
            PayOrder payOrder = SystemOrder.getOrderInfoByOutTradeNo("Y194506551713811", mch_id, key);
            LogUtils.info("查询系统订单返回结果：" + payOrder);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            LogUtils.error(e);
        }
    }
}
