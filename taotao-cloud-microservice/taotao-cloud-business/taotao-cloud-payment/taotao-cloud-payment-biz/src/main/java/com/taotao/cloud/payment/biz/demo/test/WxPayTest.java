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

import com.alibaba.fastjson2.JSONObject;
import com.yungouos.pay.entity.*;
import com.yungouos.pay.entity.qqpay.QqPayBiz;
import com.yungouos.pay.wxpay.WxPay;

/**
 * 微信支付调用演示
 *
 * @author YunGouOS技术部-029
 */
public class WxPayTest {

    public static void main(String[] args) {

        String result;
        String mchId = "微信支付商户号";
        String key = "微信支付支付密钥";
        // 收银台支付结束后返回地址
        String returnUrl = "http://www.baidu.com";
        try {

            /** 付款码支付（被扫） */
            CodePayBiz codePayBiz = WxPay.codePay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "测试",
                    "134681285892396042",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("付款码支付结果：" + codePayBiz.toString());

            /** 扫码支付 返回二维码连接 */
            result = WxPay.nativePay(
                    System.currentTimeMillis() + "",
                    "1",
                    mchId,
                    "测试",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("扫码支付 结果：" + result);

            /** 公众号支付 返回JSSDK需要的jspackage */
            String jspackage = WxPay.jsapiPay(
                    System.currentTimeMillis() + "",
                    "1",
                    mchId,
                    "测试",
                    "o-_-itxeWVTRnl-iGT_JJ-t3kpxU",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("公众号支付结果：" + jspackage);

            /** 收银台支付 返回收银台支付地址，跳转到该地址即可 */
            String cashierPayUrl = WxPay.cashierPay(
                    System.currentTimeMillis() + "",
                    "1",
                    mchId,
                    "测试收银台支付",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("收银台支付结果：" + cashierPayUrl);

            /** 小程序支付 不是真正的下单，组装参数。拿到参数后使用小程序的前端将参数传递给支付收银小程序 */
            JSONObject minAppPay = WxPay.minAppPay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "小程序支付演示",
                    "海底捞",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("小程序支付结果：" + minAppPay.toJSONString());

            /** 小程序支付，真正的下单，返回小程序支付所需参数 */
            minAppPay = WxPay.minAppPaySend(
                    "o-_-itxeWVTRnl-iGT_JJ-t3kpxU",
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "小程序支付演示",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("小程序支付结果：" + minAppPay.toJSONString());

            /** 微信刷脸支付 */
            FacePayBiz facePayBiz = WxPay.facePay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "人脸支付测试",
                    "o-_-itxeWVTRnl-iGT_JJ-t3kpxU",
                    "人脸特征码",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("刷脸支付结果：" + facePayBiz);

            /** 微信刷脸支付SDK模式凭证 */
            FacePayAuthInfoBiz facePayAuthInfo =
                    WxPay.getFacePayAuthInfo(mchId, "门店ID", "门店名称", "刷脸支付信息", "设备ID", null, null, key);
            LogUtils.info("刷脸支付凭证：" + facePayAuthInfo);

            /** 微信h5支付 */
            String h5payResult = WxPay.H5Pay(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "H5支付测试，仅限企业",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("微信H5支付结果：" + h5payResult);

            /** 微信APP支付 */
            JSONObject appPayParams = WxPay.appPay(
                    "wx465856913462378a",
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "APP支付测试，仅限企业",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("微信APP支付结果：" + appPayParams.toJSONString());

            /** QQ小程序支付，不是真正的下单，组装参数。拿到参数后使用小程序的前端将参数传递给支付收银小程序 */
            JSONObject qqPayParams = WxPay.qqPayParams(
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "QQ小程序支付测试",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("QQ小程序支付结果：" + qqPayParams.toJSONString());

            /** QQ小程序支付，真正的下单，返回小程序支付所需参数 */
            QqPayBiz qqPayBiz = WxPay.qqPay(
                    "appId",
                    "accessToken",
                    System.currentTimeMillis() + "",
                    "0.01",
                    mchId,
                    "QQ小程序支付测试",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    key);
            LogUtils.info("QQ小程序支付结果：" + qqPayBiz.toString());

            /** 查询刷卡支付结果 */
            CodePayBiz codePayBiz2 = WxPay.getCodePayResult("1556267522899", mchId, key);
            LogUtils.info("微信刷卡支付结果：" + codePayBiz2.toString());

            /** 订单退款 */
            RefundOrder refundOrder = WxPay.orderRefund("1556267522899", mchId, "0.1", null, "退款描述", null, key);
            LogUtils.info("订单退款结果：" + refundOrder.toString());

            /** 查询退款结果 */
            RefundSearch refundSearch = WxPay.getRefundResult("R17200911248111", mchId, key);
            LogUtils.info("查询退款结果：" + refundSearch.toString());

            /** 关闭订单 */
            String closeOrder = WxPay.closeOrder("R17200911248111", mchId, key);
            LogUtils.info("关闭订单结果：" + closeOrder);

            /** 撤销订单 */
            String reverseOrder = WxPay.reverseOrder("R17200911248111", mchId, key);
            LogUtils.info("撤销订单结果：" + closeOrder);

            /** 下载对账单 正常直接通过getUrl获取到excel地址到浏览器访问下载即可 也可以通过getList获取到对账单的数据流集成到业务系统中 */
            WxDownloadBillBiz downloadBillBiz = WxPay.downloadBill(mchId, "2020-01-29", null, null, key);
            LogUtils.info("对账单excel地址：" + downloadBillBiz.getUrl());
            LogUtils.info("对账单数据：" + downloadBillBiz.getList().toString());
            LogUtils.info("对账单统计数据：" + downloadBillBiz.getTotal().toString());

        } catch (Exception e) {
            LogUtils.error(e);
        }
    }
}
