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

package com.taotao.cloud.payment.biz.kit.plugin.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.kit.CashierSupport;
import com.taotao.cloud.payment.biz.kit.Payment;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.payment.biz.properties.ApiProperties;
import com.taotao.cloud.payment.biz.service.PaymentService;
import com.taotao.cloud.payment.biz.service.RefundLogService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.SettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.payment.AlipayPaymentSetting;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 支付宝支付 */
@Component
public class AliPayPlugin implements Payment {

    /** 支付日志 */
    @Autowired
    private PaymentService paymentService;
    /** 退款日志 */
    @Autowired
    private RefundLogService refundLogService;
    /** 收银台 */
    @Autowired
    private CashierSupport cashierSupport;
    /** 设置 */
    @Autowired
    private SettingApi settingApi;
    /** API域名 */
    @Autowired
    private ApiProperties apiProperties;
    /** 域名配置 */
    @Autowired
    private DomainProperties domainProperties;

    @Override
    public Result<Object> h5pay(HttpServletRequest request, HttpServletResponse response, PayParam payParam) {

        CashierParam cashierParam = cashierSupport.cashierParam(payParam);
        // 请求订单编号
        String outTradeNo = SnowFlake.getIdStr();
        // 准备支付参数
        AlipayTradeWapPayModel payModel = new AlipayTradeWapPayModel();
        payModel.setBody(cashierParam.getTitle());
        payModel.setSubject(cashierParam.getDetail());
        payModel.setTotalAmount(cashierParam.getPrice() + "");
        // 回传数据
        payModel.setPassbackParams(
                URLEncoder.createAll().encode(BeanUtils.formatKeyValuePair(payParam), StandardCharsets.UTF_8));
        // 3分钟超时
        payModel.setTimeoutExpress("3m");
        payModel.setOutTradeNo(outTradeNo);
        payModel.setProductCode("QUICK_WAP_PAY");
        try {
            LogUtils.info("支付宝H5支付：{}", JSONUtil.toJsonStr(payModel));
            AliPayRequest.wapPay(
                    response,
                    payModel,
                    callbackUrl(apiProperties.getBuyer(), PaymentMethodEnum.ALIPAY),
                    notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.ALIPAY));
        } catch (Exception e) {
            LogUtils.error("H5支付异常", e);
            throw new BusinessException(ResultEnum.ALIPAY_EXCEPTION);
        }
        return null;
    }

    @Override
    public Result<Object> jsApiPay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_NOT_SUPPORT);
    }

    @Override
    public Result<Object> appPay(HttpServletRequest request, PayParam payParam) {
        try {

            CashierParam cashierParam = cashierSupport.cashierParam(payParam);
            // 请求订单编号
            String outTradeNo = SnowFlake.getIdStr();

            AlipayTradeAppPayModel payModel = new AlipayTradeAppPayModel();

            payModel.setBody(cashierParam.getTitle());
            payModel.setSubject(cashierParam.getDetail());
            payModel.setTotalAmount(cashierParam.getPrice() + "");

            // 3分钟超时
            payModel.setTimeoutExpress("3m");
            // 回传数据
            payModel.setPassbackParams(
                    URLEncoder.createAll().encode(BeanUtils.formatKeyValuePair(payParam), StandardCharsets.UTF_8));
            payModel.setOutTradeNo(outTradeNo);
            payModel.setProductCode("QUICK_MSECURITY_PAY");

            LogUtils.info("支付宝APP支付：{}", payModel);
            String orderInfo = AliPayRequest.appPayToResponse(
                            payModel, notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.ALIPAY))
                    .getBody();
            LogUtils.info("支付宝APP支付返回内容：{}", orderInfo);
            return Result.success(orderInfo);
        } catch (AlipayApiException e) {
            LogUtils.error("支付宝支付异常：", e);
            throw new BusinessException(ResultEnum.ALIPAY_EXCEPTION);
        } catch (Exception e) {
            LogUtils.error("支付业务异常：", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public Result<Object> nativePay(HttpServletRequest request, PayParam payParam) {

        try {
            CashierParam cashierParam = cashierSupport.cashierParam(payParam);

            AlipayTradePrecreateModel payModel = new AlipayTradePrecreateModel();

            // 请求订单编号
            String outTradeNo = SnowFlake.getIdStr();

            payModel.setBody(cashierParam.getTitle());
            payModel.setSubject(cashierParam.getDetail());
            payModel.setTotalAmount(cashierParam.getPrice() + "");

            // 回传数据
            payModel.setPassbackParams(
                    URLEncoder.createAll().encode(BeanUtils.formatKeyValuePair(payParam), StandardCharsets.UTF_8));
            payModel.setTimeoutExpress("3m");
            payModel.setOutTradeNo(outTradeNo);
            LogUtils.info("支付宝扫码：{}", payModel);
            String resultStr = AliPayRequest.tradePrecreatePayToResponse(
                            payModel, notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.ALIPAY))
                    .getBody();

            LogUtils.info("支付宝扫码交互返回：{}", resultStr);
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            return Result.success(
                    jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code"));
        } catch (Exception e) {
            LogUtils.error("支付业务异常：", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public void refund(RefundLog refundLog) {
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        // 这里取支付回调时返回的流水
        if (StringUtils.isNotEmpty(refundLog.getPaymentReceivableNo())) {
            model.setTradeNo(refundLog.getPaymentReceivableNo());
        } else {
            throw new BusinessException(ResultEnum.ALIPAY_PARAMS_EXCEPTION);
        }
        model.setRefundAmount(refundLog.getTotalAmount() + "");
        model.setRefundReason(refundLog.getRefundReason());
        model.setOutRequestNo(refundLog.getOutOrderNo());
        // 交互退款
        try {
            AlipayTradeRefundResponse alipayTradeRefundResponse = AliPayApi.tradeRefundToResponse(model);
            LogUtils.error(
                    "支付宝退款，参数：{},支付宝响应：{}", JSONUtil.toJsonStr(model), JSONUtil.toJsonStr(alipayTradeRefundResponse));
            if (alipayTradeRefundResponse.isSuccess()) {
                refundLog.setIsRefund(true);
                refundLog.setReceivableNo(refundLog.getOutOrderNo());
            } else {
                refundLog.setErrorMessage(String.format(
                        "错误码：%s,错误原因：%s",
                        alipayTradeRefundResponse.getSubCode(), alipayTradeRefundResponse.getSubMsg()));
            }
            refundLogService.save(refundLog);
        } catch (Exception e) {
            LogUtils.error("支付退款异常：", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public void cancel(RefundLog refundLog) {
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        // 这里取支付回调时返回的流水
        if (StringUtils.isNotEmpty(refundLog.getPaymentReceivableNo())) {
            model.setTradeNo(refundLog.getPaymentReceivableNo());
        } else {
            LogUtils.error("退款时，支付参数为空导致异常：{}", refundLog);
            throw new BusinessException(ResultEnum.ALIPAY_PARAMS_EXCEPTION);
        }
        try {
            // 与阿里进行交互
            AlipayTradeCancelResponse alipayTradeCancelResponse = AliPayApi.tradeCancelToResponse(model);
            if (alipayTradeCancelResponse.isSuccess()) {
                refundLog.setIsRefund(true);
                refundLog.setReceivableNo(refundLog.getOutOrderNo());
            } else {
                refundLog.setErrorMessage(String.format(
                        "错误码：%s,错误原因：%s",
                        alipayTradeCancelResponse.getSubCode(), alipayTradeCancelResponse.getSubMsg()));
            }
            refundLogService.save(refundLog);
        } catch (Exception e) {
            LogUtils.error("支付宝退款异常", e);
        }
    }

    @Override
    public void refundNotify(HttpServletRequest request) {
        // 不需要实现
    }

    @Override
    public void callBack(HttpServletRequest request) {
        LogUtils.info("支付同步回调：");
        callback(request);
    }

    @Override
    public void notify(HttpServletRequest request) {
        verifyNotify(request);
        LogUtils.info("支付异步通知：");
    }

    /** 验证支付结果 */
    private void callback(HttpServletRequest request) {
        try {
            AlipayPaymentSetting alipayPaymentSetting = alipayPaymentSetting();
            // 获取支付宝反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            LogUtils.info("同步回调：{}", JSONUtil.toJsonStr(map));
            boolean verifyResult = AlipaySignature.rsaCertCheckV1(
                    map, alipayPaymentSetting.getAlipayPublicCertPath(), "UTF-8", "RSA2");
            if (verifyResult) {
                LogUtils.info("支付回调通知：支付成功-参数：{}", map);
            } else {
                LogUtils.info("支付回调通知：支付失败-参数：{}", map);
            }

            ThreadContextHolder.getHttpResponse()
                    .sendRedirect(domainProperties.getWap() + "/pages/order/myOrder?status=0");
        } catch (Exception e) {
            LogUtils.error("支付回调同步通知异常", e);
        }
    }

    /**
     * 验证支付结果
     *
     * @param request
     */
    private void verifyNotify(HttpServletRequest request) {
        try {
            AlipayPaymentSetting alipayPaymentSetting = alipayPaymentSetting();
            // 获取支付宝反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            LogUtils.info("支付回调响应：{}", JSONUtil.toJsonStr(map));
            boolean verifyResult = AlipaySignature.rsaCertCheckV1(
                    map, alipayPaymentSetting.getAlipayPublicCertPath(), "UTF-8", "RSA2");
            // 支付完成判定
            if (!"TRADE_FINISHED".equals(map.get("trade_status")) && !"TRADE_SUCCESS".equals(map.get("trade_status"))) {
                return;
            }
            String payParamStr = map.get("passback_params");
            String payParamJson = URLDecoder.decode(payParamStr, StandardCharsets.UTF_8);
            PayParam payParam = BeanUtils.formatKeyValuePair(payParamJson, new PayParam());

            if (verifyResult) {
                String tradeNo = map.get("trade_no");
                BigDecimal totalAmount = BigDecimal.parseBigDecimal(map.get("total_amount"));
                PaymentSuccessParams paymentSuccessParams =
                        new PaymentSuccessParams(PaymentMethodEnum.ALIPAY.name(), tradeNo, totalAmount, payParam);

                paymentService.success(paymentSuccessParams);
                LogUtils.info("支付回调通知：支付成功-参数：{},回调参数:{}", map, payParam);
            } else {
                LogUtils.info("支付回调通知：支付失败-参数：{}", map);
            }
        } catch (AlipayApiException e) {
            LogUtils.error("支付回调通知异常", e);
        }
    }

    /** 获取微信支付配置 */
    private AlipayPaymentSetting alipayPaymentSetting() {
        AlipayPaymentSetting setting = settingApi.getAlipayPaymentSetting(SettingCategoryEnum.ALIPAY_PAYMENT.name());
        if (setting != null) {
            return setting;
        }
        throw new BusinessException(ResultEnum.ALIPAY_NOT_SETTING);
    }
}
