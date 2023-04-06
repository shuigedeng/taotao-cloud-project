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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.service;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.code.paymodel.AliPayCode;
import com.taotao.cloud.payment.biz.bootx.core.notify.dao.PayNotifyRecordManager;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.AbsPayCallbackStrategy;
import com.taotao.cloud.payment.biz.bootx.core.pay.service.PayCallbackService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.dao.AlipayConfigManager;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.entity.AlipayConfig;
import java.util.Map;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝回调处理
 *
 * @author xxm
 * @date 2021/2/28
 */
@Slf4j
@Service
public class AliPayCallbackService extends AbsPayCallbackStrategy {
    private final AlipayConfigManager alipayConfigManager;

    public AliPayCallbackService(
            RedisClient redisClient,
            PayNotifyRecordManager payNotifyRecordManager,
            PayCallbackService payCallbackService,
            AlipayConfigService alipayConfigService,
            AlipayConfigManager alipayConfigManager) {
        super(redisClient, payNotifyRecordManager, payCallbackService);
        this.alipayConfigManager = alipayConfigManager;
    }

    @Override
    public int getPayChannel() {
        return PayChannelCode.ALI;
    }

    @Override
    public int getTradeStatus() {
        Map<String, String> params = PARAMS.get();
        String tradeStatus = params.get(AliPayCode.TRADE_STATUS);
        if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_SUCCESS)) {
            return PayStatusCode.NOTIFY_TRADE_SUCCESS;
        }
        return PayStatusCode.NOTIFY_TRADE_FAIL;
    }

    @SneakyThrows
    @Override
    public boolean verifyNotify() {
        Map<String, String> params = PARAMS.get();
        String callReq = JSONUtil.toJsonStr(params);
        String appId = params.get("app_id");
        if (StrUtil.isBlank(appId)) {
            log.warn("支付宝回调报文 appId 为空 {}", callReq);
            return false;
        }
        AlipayConfig alipayConfig = alipayConfigManager.findActivity().orElseThrow(DataNotExistException::new);
        if (alipayConfig == null) {
            log.warn("支付宝回调报文 appId 不合法 {}", callReq);
            return false;
        }

        try {
            if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_KEY)) {
                return AlipaySignature.rsaCheckV1(
                        params, alipayConfig.getAlipayPublicKey(), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            } else {
                return AlipaySignature.verifyV1(
                        params,
                        CertUtil.getCertByContent(alipayConfig.getAlipayCert()),
                        CharsetUtil.UTF_8,
                        AlipayConstants.SIGN_TYPE_RSA2);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验签失败", e);
            return false;
        }
    }

    @Override
    public Long getPaymentId() {
        Map<String, String> params = PARAMS.get();
        return Long.valueOf(params.get(AliPayCode.OUT_TRADE_NO));
    }

    @Override
    public String getReturnMsg() {
        return "success";
    }
}
