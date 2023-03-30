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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.service;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.MicroPayModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayWayEnum;
import com.taotao.cloud.payment.biz.bootx.code.paymodel.WeChatPayWay;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.entity.WeChatPayConfig;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.wechat.WeChatPayParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信支付
 *
 * @author xxm
 * @date 2021/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayService {

    /** 校验 */
    public void validation(PayModeParam payModeParam, WeChatPayConfig weChatPayConfig) {
        List<String> payWays =
                Optional.ofNullable(weChatPayConfig.getPayWays())
                        .filter(StrUtil::isNotBlank)
                        .map(s -> StrUtil.split(s, ','))
                        .orElse(new ArrayList<>(1));

        PayWayEnum payWayEnum =
                Optional.ofNullable(WeChatPayWay.findByNo(payModeParam.getPayWay()))
                        .orElseThrow(() -> new BizException("非法的微信支付类型"));
        if (!payWays.contains(payWayEnum.getCode())) {
            throw new BizException("该微信支付方式不可用");
        }
    }

    /** 支付 */
    public void pay(
            BigDecimal amount,
            Payment payment,
            WeChatPayParam weChatPayParam,
            PayModeParam payModeParam,
            WeChatPayConfig weChatPayConfig) {

        String payBody = null;

        // wap支付
        if (payModeParam.getPayWay() == PayWayCode.WAP) {
            payBody = this.wapPay(amount, payment, weChatPayConfig);
        }
        // 程序支付
        else if (payModeParam.getPayWay() == PayWayCode.APP) {
            payBody = this.appPay(amount, payment, weChatPayConfig);
        }
        // 微信公众号支付或者小程序支付
        else if (payModeParam.getPayWay() == PayWayCode.JSAPI) {
            payBody = this.jsPay(amount, payment, weChatPayParam.getOpenId(), weChatPayConfig);
        }
        // 二维码支付
        else if (payModeParam.getPayWay() == PayWayCode.QRCODE) {
            payBody = this.qrCodePay(amount, payment, weChatPayConfig);
        }
        // 付款码支付
        else if (payModeParam.getPayWay() == PayWayCode.BARCODE) {
            this.barCode(
                    amount, payment, weChatPayParam.getAuthCode(), weChatPayParam, weChatPayConfig);
        }
        // payBody到线程存储
        if (StrUtil.isNotBlank(payBody)) {
            AsyncPayInfo asyncPayInfo = new AsyncPayInfo().setPayBody(payBody);
            AsyncPayInfoLocal.set(asyncPayInfo);
        }
    }

    /** wap支付 */
    private String wapPay(BigDecimal amount, Payment payment, WeChatPayConfig weChatPayConfig) {
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params =
                this.buildParams(amount, payment, weChatPayConfig, TradeType.MWEB.getTradeType())
                        .build()
                        .createSign(wxPayApiConfig.getApiKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.MWEB_URL);
    }

    /** 程序支付 */
    private String appPay(BigDecimal amount, Payment payment, WeChatPayConfig weChatPayConfig) {
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params =
                this.buildParams(amount, payment, weChatPayConfig, TradeType.APP.getTradeType())
                        .build()
                        .createSign(wxPayApiConfig.getApiKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.PREPAY_ID);
    }

    /** 微信公众号支付或者小程序支付 */
    private String jsPay(
            BigDecimal amount, Payment payment, String openId, WeChatPayConfig weChatPayConfig) {
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params =
                this.buildParams(amount, payment, weChatPayConfig, TradeType.JSAPI.getTradeType())
                        .openid(openId)
                        .build()
                        .createSign(wxPayApiConfig.getApiKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.PREPAY_ID);
    }

    /** 二维码支付 */
    private String qrCodePay(BigDecimal amount, Payment payment, WeChatPayConfig weChatPayConfig) {

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params =
                this.buildParams(amount, payment, weChatPayConfig, TradeType.NATIVE.getTradeType())
                        .build()
                        .createSign(wxPayApiConfig.getApiKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.CODE_URL);
    }

    /** 条形码支付 */
    private void barCode(
            BigDecimal amount,
            Payment payment,
            String authCode,
            WeChatPayParam weChatPayParam,
            WeChatPayConfig weChatPayConfig) {
        String totalFee = String.valueOf(amount.multiply(new BigDecimal(100)).longValue());
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params =
                MicroPayModel.builder()
                        .appid(wxPayApiConfig.getAppId())
                        .mch_id(wxPayApiConfig.getMchId())
                        .nonce_str(WxPayKit.generateStr())
                        .body(payment.getTitle())
                        .auth_code(authCode)
                        .out_trade_no(String.valueOf(payment.getId()))
                        .total_fee(totalFee)
                        .spbill_create_ip(NetUtil.getLocalhostStr())
                        .build()
                        .createSign(wxPayApiConfig.getApiKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        String tradeType = result.get(WeChatPayCode.TRADE_TYPE);
        // 支付成功处理
        if (Objects.equals(result.get(WeChatPayCode.TRADE_STATE), WeChatPayCode.TRADE_SUCCESS)) {
            payment.setPayStatus(PayStatusCode.TRADE_SUCCESS).setPayTime(LocalDateTime.now());
            return;
        }
    }

    /** 构建参数 */
    private UnifiedOrderModel.UnifiedOrderModelBuilder buildParams(
            BigDecimal amount, Payment payment, WeChatPayConfig weChatPayConfig, String tradeType) {
        String totalFee = String.valueOf(amount.multiply(new BigDecimal(100)).longValue());
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        return UnifiedOrderModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body(payment.getTitle())
                .out_trade_no(String.valueOf(payment.getId()))
                .total_fee(totalFee)
                .spbill_create_ip(NetUtil.getLocalhostStr())
                .notify_url(weChatPayConfig.getNotifyUrl())
                .trade_type(tradeType);
    }

    /** 验证错误信息 */
    private void verifyErrorMsg(Map<String, String> result) {
        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        if (!WxPayKit.codeIsOk(returnCode) || !WxPayKit.codeIsOk(resultCode)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(WeChatPayCode.RETURN_MSG);
            }
            log.error("支付失败 {}", errorMsg);
            throw new BizException(errorMsg);
        }
    }
}
