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

import com.yungouos.pay.alipay.AliPay;
import com.yungouos.pay.entity.*;

/**
 * 支付宝SDK调用演示
 *
 * @author YunGouOS技术部-029
 */
public class AliPayTest {

    public static void main(String[] args) {
        String result;
        // 支付宝商户号，登录www.yungouos.com-》支付宝-》商户管理 获取
        String mch_id = "签约后的商户号";
        // 商户密钥
        String key = "签约后的密钥";
        // 回调地址
        String notify = "http://www.baidu.com";

        // 同步回调地址
        String returnUrl = "http://www.baidu.com";

        try {

            // 花呗分期业务参数示例
            HbFqBiz hbFqBiz = new HbFqBiz();
            hbFqBiz.setNum(3); // 分3期
            hbFqBiz.setPercent(0); // 花呗手续费商户承担比例为0，也就是全部由消费者承担花呗分期手续费

            // 花呗分期需要支付金额超过100元，此处方便演示就将该对象置位null，正常使用时候根据自身业务情况设置该对象即可
            hbFqBiz = null;

            // 付款码
            String auth_code = "288717351683453412";
            // 支付宝付款码支付
            AliPayCodePayBiz aliPayCodePayBiz = AliPay.codePay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mch_id,
                    "测试",
                    auth_code,
                    null,
                    notify,
                    null,
                    null,
                    null,
                    hbFqBiz,
                    key);
            LogUtils.info(aliPayCodePayBiz.toString());

            // 支付宝扫码支付
            result = AliPay.nativePay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mch_id,
                    "测试订单",
                    "2",
                    null,
                    notify,
                    null,
                    null,
                    null,
                    hbFqBiz,
                    key);
            LogUtils.info("支付宝扫码支付返回结果：" + result);

            // 支付宝wap支付
            result = AliPay.wapPay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mch_id,
                    "支付测试",
                    null,
                    notify,
                    null,
                    null,
                    null,
                    hbFqBiz,
                    key);
            LogUtils.info("支付宝wap支付返回结果：" + result);

            String buyer_id = "支付宝买家唯一编号，通过支付宝授权接口获取";

            // 支付宝JS支付
            AliPayJsPayBiz aliPayJsPayBiz = AliPay.jsPay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mch_id,
                    buyer_id,
                    "支付测试",
                    null,
                    notify,
                    null,
                    null,
                    null,
                    hbFqBiz,
                    key);
            LogUtils.info("支付宝JS支付返回结果：" + aliPayJsPayBiz.toString());

            // 支付宝H5支付
            AliPayH5Biz aliPayH5Biz = AliPay.h5Pay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mch_id,
                    "接口测试",
                    null,
                    notify,
                    returnUrl,
                    null,
                    null,
                    null,
                    hbFqBiz,
                    key);
            // form表单需要自行输出跳转
            LogUtils.info("支付宝H5支付返回form表单：" + aliPayH5Biz.getForm());
            // url直接重定向访问即可
            LogUtils.info("支付宝H5支付返回url：" + aliPayH5Biz.getUrl());

            // 支付宝appPay支付
            String appPay = AliPay.appPay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mch_id,
                    "接口测试",
                    null,
                    notify,
                    null,
                    null,
                    null,
                    hbFqBiz,
                    key);
            LogUtils.info("支付宝APP支付返回结果：" + appPay);

            // 电脑网站支付
            AliPayWebPayBiz aliPayWebPayBiz = AliPay.webPay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mch_id,
                    "接口测试",
                    null,
                    notify,
                    null,
                    null,
                    null,
                    null,
                    hbFqBiz,
                    key);
            // form表单需要自行输出跳转
            LogUtils.info("支付宝电脑网站支付返回form表单：" + aliPayWebPayBiz.getForm());
            // url直接重定向访问即可
            LogUtils.info("支付宝电脑网站支付返回url：" + aliPayWebPayBiz.getUrl());

            // 发起退款
            RefundOrder orderRefund = AliPay.orderRefund("Y194506551713811", mch_id, "0.01", null, "测试退款", null, key);
            LogUtils.info("支付宝发起退款返回结果：" + orderRefund.toString());

            // 退款查询
            RefundSearch refundSearch = AliPay.getRefundResult("R09441868126739", mch_id, key);
            LogUtils.info("支付宝退款结果查询返回结果：" + refundSearch.toString());

            // 关闭订单
            String closeOrder = AliPay.closeOrder("Y194506551713811", mch_id, key);
            LogUtils.info("支付宝关闭订单结果：" + closeOrder);

            // 撤销订单
            AliPayReverseOrderBiz aliPayReverseOrderBiz = AliPay.reverseOrder("Y194506551713811", mch_id, key);
            LogUtils.info("支付宝撤销订单结果：" + aliPayReverseOrderBiz.toString());

        } catch (Exception e) {
            LogUtils.error(e);
        }
    }
}
