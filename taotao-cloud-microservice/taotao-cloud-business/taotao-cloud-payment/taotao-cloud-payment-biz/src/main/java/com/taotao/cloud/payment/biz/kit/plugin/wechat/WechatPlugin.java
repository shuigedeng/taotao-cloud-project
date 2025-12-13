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

package com.taotao.cloud.payment.biz.kit.plugin.wechat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taotao.boot.cache.redis.repository.RedisRepository;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.order.api.feign.OrderApi;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.kit.CashierSupport;
import com.taotao.cloud.payment.biz.kit.Payment;
import com.taotao.cloud.payment.biz.kit.core.PaymentHttpResponse;
import com.taotao.cloud.payment.biz.kit.core.enums.RequestMethodEnums;
import com.taotao.cloud.payment.biz.kit.core.enums.SignType;
import com.taotao.cloud.payment.biz.kit.core.kit.AesUtil;
import com.taotao.cloud.payment.biz.kit.core.kit.HttpKit;
import com.taotao.cloud.payment.biz.kit.core.kit.IpKit;
import com.taotao.cloud.payment.biz.kit.core.kit.PayKit;
import com.taotao.cloud.payment.biz.kit.core.kit.WxPayKit;
import com.taotao.cloud.payment.biz.kit.core.utils.DateTimeZoneUtil;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.enums.WechatApiEnum;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.enums.WechatDomain;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.model.Amount;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.model.H5Info;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.model.Payer;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.model.RefundModel;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.model.SceneInfo;
import com.taotao.cloud.payment.biz.kit.plugin.wechat.model.UnifiedOrderModel;
import com.taotao.cloud.payment.biz.properties.ApiProperties;
import com.taotao.cloud.payment.biz.service.PaymentService;
import com.taotao.cloud.payment.biz.service.RefundLogService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.SettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.payment.WechatPaymentSetting;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 微信支付 */
@Component
public class WechatPlugin implements Payment {

    /** 收银台 */
    @Autowired
    private CashierSupport cashierSupport;
    /** 支付日志 */
    @Autowired
    private PaymentService paymentService;
    /** 缓存 */
    @Autowired
    private RedisRepository redisRepository;
    /** 退款日志 */
    @Autowired
    private RefundLogService refundLogService;
    /** API域名 */
    @Autowired
    private ApiProperties apiProperties;
    /** 配置 */
    @Autowired
    private SettingApi settingApi;
    /** 联合登陆 */
    @Autowired
    private IFeignConnectService connectService;
    /** 联合登陆 */
    @Autowired
    private OrderApi orderApi;

    @Override
    public Result<Object> h5pay(HttpServletRequest request, HttpServletResponse response1, PayParam payParam) {

        try {
            CashierParam cashierParam = cashierSupport.cashierParam(payParam);

            // 支付参数准备
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setPayer_client_ip(IpKit.getRealIp(request));
            H5Info h5Info = new H5Info();
            h5Info.setType("WAP");
            sceneInfo.setH5_info(h5Info);

            // 支付金额
            Integer fen = CurrencyUtils.fen(cashierParam.getPrice());
            // 第三方付款订单
            String outOrderNo = IdGeneratorUtils.getIdStr();
            // 过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);

            // 回传数据
            String attach = URLEncoder.createDefault().encode(JSONUtil.toJsonStr(payParam), StandardCharsets.UTF_8);

            WechatPaymentSetting setting = wechatPaymentSetting();
            String appid = setting.getServiceAppId();
            if (appid == null) {
                throw new BusinessException(ResultEnum.WECHAT_PAYMENT_NOT_SETTING);
            }
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(cashierParam.getDetail())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    .setAttach(attach)
                    .setNotify_url(notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.WECHAT))
                    .setAmount(new Amount().setTotal(fen))
                    .setScene_info(sceneInfo);

            LogUtils.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.H5_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel));

            return Result.success(JSONUtil.toJsonStr(response.getBody()));
        } catch (Exception e) {
            LogUtils.error("微信H5支付错误", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public Result<Object> jsApiPay(HttpServletRequest request, PayParam payParam) {

        try {
            Connect connect = connectService.queryConnect(ConnectQueryDTO.builder()
                    .userId(UserContext.getCurrentUser().getId())
                    .unionType(ConnectEnum.WECHAT.name())
                    .build());
            if (connect == null) {
                return null;
            }

            Payer payer = new Payer();
            payer.setOpenid(connect.getUnionId());

            CashierParam cashierParam = cashierSupport.cashierParam(payParam);

            // 支付金额
            Integer fen = CurrencyUtils.fen(cashierParam.getPrice());
            // 第三方付款订单
            String outOrderNo = IdGeneratorUtils.getIdStr();
            // 过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);

            String attach = URLEncoder.createDefault().encode(JSONUtil.toJsonStr(payParam), StandardCharsets.UTF_8);

            WechatPaymentSetting setting = wechatPaymentSetting();
            String appid = setting.getServiceAppId();
            if (appid == null) {
                throw new BusinessException(ResultEnum.WECHAT_PAYMENT_NOT_SETTING);
            }
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(cashierParam.getDetail())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    .setAttach(attach)
                    .setNotify_url(notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.WECHAT))
                    .setAmount(new Amount().setTotal(fen))
                    .setPayer(payer);

            LogUtils.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.JS_API_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel));
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, getPlatformCert());
            LogUtils.info("verifySignature: {}", verifySignature);
            LogUtils.info("统一下单响应 {}", response);

            if (verifySignature) {
                String body = response.getBody();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                String prepayId = jsonObject.getStr("prepay_id");
                Map<String, String> map = WxPayKit.jsApiCreateSign(appid, prepayId, setting.getApiclient_key());
                LogUtils.info("唤起支付参数:{}", map);

                return Result.success(map);
            }
            LogUtils.error("微信支付参数验证错误，请及时处理");
            throw new BusinessException(ResultEnum.PAY_ERROR);
        } catch (Exception e) {
            LogUtils.error("支付异常", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public Result<Object> appPay(HttpServletRequest request, PayParam payParam) {

        try {

            CashierParam cashierParam = cashierSupport.cashierParam(payParam);

            // 支付金额
            Integer fen = CurrencyUtils.fen(cashierParam.getPrice());
            // 第三方付款订单
            String outOrderNo = IdGeneratorUtils.getIdStr();
            // 过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);

            String attach = URLEncoder.createDefault().encode(JSONUtil.toJsonStr(payParam), StandardCharsets.UTF_8);

            WechatPaymentSetting setting = wechatPaymentSetting();
            String appid = setting.getAppId();
            if (appid == null) {
                throw new BusinessException(ResultEnum.WECHAT_PAYMENT_NOT_SETTING);
            }
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(cashierParam.getDetail())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    .setAttach(attach)
                    .setNotify_url(notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.WECHAT))
                    .setAmount(new Amount().setTotal(fen));

            LogUtils.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.APP_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel));
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, getPlatformCert());
            LogUtils.info("verifySignature: {}", verifySignature);
            LogUtils.info("统一下单响应 {}", response);

            if (verifySignature) {
                JSONObject jsonObject = JSONUtil.parseObj(response.getBody());
                String prepayId = jsonObject.getStr("prepay_id");
                Map<String, String> map = WxPayKit.appPrepayIdCreateSign(
                        appid, setting.getMchId(), prepayId, setting.getApiclient_key(), SignType.HMACSHA256);
                LogUtils.info("唤起支付参数:{}", map);

                return Result.success(map);
            }
            LogUtils.error("微信支付参数验证错误，请及时处理");
            throw new BusinessException(ResultEnum.PAY_ERROR);
        } catch (Exception e) {
            LogUtils.error("支付异常", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public Result<Object> nativePay(HttpServletRequest request, PayParam payParam) {

        try {

            CashierParam cashierParam = cashierSupport.cashierParam(payParam);

            // 支付金额
            Integer fen = CurrencyUtils.fen(cashierParam.getPrice());
            // 第三方付款订单
            String outOrderNo = IdGeneratorUtils.getIdStr();
            // 过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);

            String attach = URLEncoder.createDefault().encode(JSONUtil.toJsonStr(payParam), StandardCharsets.UTF_8);

            WechatPaymentSetting setting = wechatPaymentSetting();

            String appid = setting.getServiceAppId();
            if (appid == null) {
                throw new BusinessException(ResultEnum.WECHAT_PAYMENT_NOT_SETTING);
            }
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(cashierParam.getDetail())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    // 回传参数
                    .setAttach(attach)
                    .setNotify_url(notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.WECHAT))
                    .setAmount(new Amount().setTotal(fen));

            LogUtils.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.NATIVE_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel));
            LogUtils.info("统一下单响应 {}", response);
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, getPlatformCert());
            LogUtils.info("verifySignature: {}", verifySignature);

            if (verifySignature) {
                return Result.success(new JSONObject(response.getBody()).getStr("code_url"));
            } else {
                LogUtils.error("微信支付参数验证错误，请及时处理");
                throw new BusinessException(ResultEnum.PAY_ERROR);
            }
        } catch (ServiceException e) {
            LogUtils.error("支付异常", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        } catch (Exception e) {
            LogUtils.error("支付异常", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public Result<Object> mpPay(HttpServletRequest request, PayParam payParam) {

        try {
            Connect connect = connectService.queryConnect(ConnectQueryDTO.builder()
                    .userId(UserContext.getCurrentUser().getId())
                    .unionType(ConnectEnum.WECHAT_MP_OPEN_ID.name())
                    .build());
            if (connect == null) {
                return null;
            }

            Payer payer = new Payer();
            payer.setOpenid(connect.getUnionId());

            CashierParam cashierParam = cashierSupport.cashierParam(payParam);

            // 支付金额
            Integer fen = CurrencyUtils.fen(cashierParam.getPrice());
            // 第三方付款订单
            String outOrderNo = IdGeneratorUtils.getIdStr();
            // 过期时间
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);

            // 微信小程序，appid 需要单独获取，这里读取了联合登陆配置的appid ，实际场景小程序自动登录，所以这个appid是最为保险的做法
            // 如果有2开需求，这里需要调整，修改这个appid的获取途径即可
            String appid = wechatPaymentSetting().getMpAppId();
            if (StringUtils.isEmpty(appid)) {
                throw new BusinessException(ResultEnum.WECHAT_PAYMENT_NOT_SETTING);
            }
            String attach = URLEncoder.createDefault().encode(JSONUtil.toJsonStr(payParam), StandardCharsets.UTF_8);

            WechatPaymentSetting setting = wechatPaymentSetting();
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(appid)
                    .setMchid(setting.getMchId())
                    .setDescription(cashierParam.getDetail())
                    .setOut_trade_no(outOrderNo)
                    .setTime_expire(timeExpire)
                    .setAttach(attach)
                    .setNotify_url(notifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.WECHAT))
                    .setAmount(new Amount().setTotal(fen))
                    .setPayer(payer);

            LogUtils.info("统一下单参数 {}", JSONUtil.toJsonStr(unifiedOrderModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.JS_API_PAY.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(unifiedOrderModel));
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, getPlatformCert());
            LogUtils.info("verifySignature: {}", verifySignature);
            LogUtils.info("统一下单响应 {}", response);

            if (verifySignature) {
                String body = response.getBody();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                String prepayId = jsonObject.getStr("prepay_id");
                Map<String, String> map = WxPayKit.jsApiCreateSign(appid, prepayId, setting.getApiclient_key());
                LogUtils.info("唤起支付参数:{}", map);

                return Result.success(map);
            }
            LogUtils.error("微信支付参数验证错误，请及时处理");
            throw new BusinessException(ResultEnum.PAY_ERROR);
        } catch (Exception e) {
            LogUtils.error("支付异常", e);
            throw new BusinessException(ResultEnum.PAY_ERROR);
        }
    }

    @Override
    public void callBack(HttpServletRequest request) {
        try {
            verifyNotify(request);
        } catch (Exception e) {
            LogUtils.error("支付异常", e);
        }
    }

    @Override
    public void notify(HttpServletRequest request) {
        try {
            verifyNotify(request);
        } catch (Exception e) {
            LogUtils.error("支付异常", e);
        }
    }

    /**
     * 验证结果，执行支付回调
     *
     * @param request
     * @throws Exception
     */
    private void verifyNotify(HttpServletRequest request) throws Exception {

        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serialNo = request.getHeader("Wechatpay-Serial");
        String signature = request.getHeader("Wechatpay-Signature");

        LogUtils.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
        String result = HttpKit.readData(request);
        LogUtils.info("微信支付通知密文 {}", result);

        WechatPaymentSetting setting = wechatPaymentSetting();
        // 校验服务器端响应¬
        String plainText = WxPayKit.verifyNotify(
                serialNo,
                result,
                signature,
                nonce,
                timestamp,
                setting.getApiKey3(),
                Objects.requireNonNull(getPlatformCert()));

        LogUtils.info("微信支付通知明文 {}", plainText);

        JSONObject jsonObject = JSONUtil.parseObj(plainText);

        String payParamStr = jsonObject.getStr("attach");
        String payParamJson = URLDecoder.decode(payParamStr, StandardCharsets.UTF_8);
        PayParam payParam = JSONUtil.toBean(payParamJson, PayParam.class);

        String tradeNo = jsonObject.getStr("transaction_id");
        BigDecimal totalAmount =
                CurrencyUtils.reversalFen(jsonObject.getJSONObject("amount").getBigDecimal("total"));

        PaymentSuccessParams paymentSuccessParams =
                new PaymentSuccessParams(PaymentMethodEnum.WECHAT.name(), tradeNo, totalAmount, payParam);

        paymentService.success(paymentSuccessParams);
        LogUtils.info("微信支付回调：支付成功{}", plainText);
    }

    @Override
    public void refund(RefundLog refundLog) {

        try {

            Amount amount = new Amount()
                    .setRefund(CurrencyUtils.fen(refundLog.getTotalAmount()))
                    .setTotal(CurrencyUtils.fen(orderApi.getPaymentTotal(refundLog.getOrderSn())));

            // 退款参数准备
            RefundModel refundModel = new RefundModel()
                    .setTransaction_id(refundLog.getPaymentReceivableNo())
                    .setOut_refund_no(refundLog.getOutOrderNo())
                    .setReason(refundLog.getRefundReason())
                    .setAmount(amount)
                    .setNotify_url(refundNotifyUrl(apiProperties.getBuyer(), PaymentMethodEnum.WECHAT));

            WechatPaymentSetting setting = wechatPaymentSetting();

            LogUtils.info("微信退款参数 {}", JSONUtil.toJsonStr(refundModel));
            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.POST,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.DOMESTIC_REFUNDS.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    JSONUtil.toJsonStr(refundModel));
            LogUtils.info("微信退款响应 {}", response);
            // 退款申请成功
            if (response.getStatus() == 200) {
                refundLogService.save(refundLog);
            } else {
                // 退款申请失败
                refundLog.setErrorMessage(response.getBody());
                refundLogService.save(refundLog);
            }
        } catch (Exception e) {
            LogUtils.error("微信退款申请失败", e);
        }
    }

    @Override
    public void cancel(RefundLog refundLog) {
        this.refund(refundLog);
    }

    @Override
    public void refundNotify(HttpServletRequest request) {
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serialNo = request.getHeader("Wechatpay-Serial");
        String signature = request.getHeader("Wechatpay-Signature");

        LogUtils.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
        String result = HttpKit.readData(request);
        LogUtils.info("微信退款通知密文 {}", result);
        JSONObject ciphertext = JSONUtil.parseObj(result);

        try { // 校验服务器端响应¬
            String plainText = WxPayKit.verifyNotify(
                    serialNo,
                    result,
                    signature,
                    nonce,
                    timestamp,
                    wechatPaymentSetting().getApiKey3(),
                    Objects.requireNonNull(getPlatformCert()));
            LogUtils.info("微信退款通知明文 {}", plainText);

            if (("REFUND.SUCCESS").equals(ciphertext.getStr("event_type"))) {
                LogUtils.info("退款成功 {}", plainText);
                // 校验服务器端响应
                JSONObject jsonObject = JSONUtil.parseObj(plainText);
                String transactionId = jsonObject.getStr("transaction_id");
                String refundId = jsonObject.getStr("refund_id");

                RefundLog refundLog = refundLogService.getOne(
                        new LambdaQueryWrapper<RefundLog>().eq(RefundLog::getPaymentReceivableNo, transactionId));
                if (refundLog != null) {
                    refundLog.setIsRefund(true);
                    refundLog.setReceivableNo(refundId);
                    refundLogService.saveOrUpdate(refundLog);
                }

            } else {
                LogUtils.info("退款失败 {}", plainText);
                JSONObject jsonObject = JSONUtil.parseObj(plainText);
                String transactionId = jsonObject.getStr("transaction_id");
                String refundId = jsonObject.getStr("refund_id");

                RefundLog refundLog = refundLogService.getOne(
                        new LambdaQueryWrapper<RefundLog>().eq(RefundLog::getPaymentReceivableNo, transactionId));
                if (refundLog != null) {
                    refundLog.setReceivableNo(refundId);
                    refundLog.setErrorMessage(ciphertext.getStr("summary"));
                    refundLogService.saveOrUpdate(refundLog);
                }
            }
        } catch (Exception e) {
            LogUtils.error("微信退款失败", e);
        }
    }

    /**
     * 获取微信支付配置
     *
     * @return
     */
    private WechatPaymentSetting wechatPaymentSetting() {
        try {
            WechatPaymentSetting wechatPaymentSetting =
                    settingApi.getWechatPaymentSetting(SettingCategoryEnum.WECHAT_PAYMENT.name());
            return wechatPaymentSetting;
        } catch (Exception e) {
            LogUtils.error("微信支付暂不支持", e);
            throw new BusinessException(ResultEnum.PAY_NOT_SUPPORT);
        }
    }

    /**
     * 获取平台公钥
     *
     * @return 平台公钥
     */
    private X509Certificate getPlatformCert() {
        // 获取缓存中的平台公钥，如果有则直接返回，否则去微信请求
        String publicCert = (String) redisRepository.get(CachePrefix.WECHAT_PLAT_FORM_CERT.getPrefix());
        if (!StringUtils.isEmpty(publicCert)) {
            return PayKit.getCertificate(publicCert);
        }
        // 获取平台证书列表
        try {

            WechatPaymentSetting setting = wechatPaymentSetting();

            PaymentHttpResponse response = WechatApi.v3(
                    RequestMethodEnums.GET,
                    WechatDomain.CHINA.toString(),
                    WechatApiEnum.GET_CERTIFICATES.toString(),
                    setting.getMchId(),
                    setting.getSerialNumber(),
                    null,
                    setting.getApiclient_key(),
                    "");
            String body = response.getBody();
            LogUtils.info("获取微信平台证书body: {}", body);
            if (response.getStatus() == 200) {
                JSONObject jsonObject = JSONUtil.parseObj(body);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                // 默认认为只有一个平台证书
                JSONObject encryptObject = dataArray.getJSONObject(0);
                JSONObject encryptCertificate = encryptObject.getJSONObject("encrypt_certificate");
                String associatedData = encryptCertificate.getStr("associated_data");
                String cipherText = encryptCertificate.getStr("ciphertext");
                String nonce = encryptCertificate.getStr("nonce");
                publicCert = getPlatformCertStr(associatedData, nonce, cipherText);
                long second =
                        (PayKit.getCertificate(publicCert).getNotAfter().getTime() - System.currentTimeMillis()) / 1000;
                redisRepository.set(CachePrefix.WECHAT_PLAT_FORM_CERT.getPrefix(), publicCert, second);
            } else {
                LogUtils.error("证书获取失败：{}" + body);
                throw new BusinessException(ResultEnum.WECHAT_CERT_ERROR);
            }
            return PayKit.getCertificate(publicCert);
        } catch (Exception e) {
            LogUtils.error("证书获取失败", e);
        }
        return null;
    }

    /**
     * 获取平台证书缓存的字符串 下列各个密钥参数
     *
     * @param associatedData 密钥参数
     * @param nonce 密钥参数
     * @param cipherText 密钥参数
     * @return platform key
     * @throws GeneralSecurityException 密钥获取异常
     */
    private String getPlatformCertStr(String associatedData, String nonce, String cipherText)
            throws GeneralSecurityException {

        AesUtil aesUtil = new AesUtil(wechatPaymentSetting().getApiKey3().getBytes(StandardCharsets.UTF_8));
        // 平台证书密文解密
        // encrypt_certificate 中的  associated_data nonce  ciphertext
        return aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), cipherText);
    }
}
