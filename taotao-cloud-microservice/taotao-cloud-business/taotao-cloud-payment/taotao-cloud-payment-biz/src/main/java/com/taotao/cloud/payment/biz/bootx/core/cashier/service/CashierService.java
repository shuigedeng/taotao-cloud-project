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

package com.taotao.cloud.payment.biz.bootx.core.cashier.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayModelExtraCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayWayCode;
import com.taotao.cloud.payment.biz.bootx.core.aggregate.entity.AggregatePayInfo;
import com.taotao.cloud.payment.biz.bootx.core.aggregate.service.AggregateService;
import com.taotao.cloud.payment.biz.bootx.core.pay.PayModelUtil;
import com.taotao.cloud.payment.biz.bootx.core.pay.service.PayService;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierCombinationPayParam;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierSinglePayParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import java.math.BigDecimal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.RedisClient;
import org.springframework.stereotype.Service;

/**
 * 结算台
 *
 * @author xxm
 * @date 2022/2/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierService {
    private final PayService payService;
    private final AggregateService aggregateService;
    private final RedisClient redisClient;
    private final String PREFIX_KEY = "cashier:pay:aggregate:";

    /** 发起支付(单渠道支付) */
    public PayResult singlePay(CashierSinglePayParam param) {

        // 如果是聚合支付, 特殊处理
        if (Objects.equals(PayChannelCode.AGGREGATION, param.getPayChannel())) {
            int payChannel = aggregateService.getPayChannel(param.getAuthCode());
            param.setPayChannel(payChannel);
        }

        // 构建支付方式参数
        PayModeParam payModeParam = new PayModeParam()
                .setPayChannel(param.getPayChannel())
                .setPayWay(param.getPayWay())
                .setAmount(param.getAmount());

        // 处理附加参数
        HashMap<String, String> map = new HashMap<>(1);
        map.put(PayModelExtraCode.AUTH_CODE, param.getAuthCode());
        map.put(PayModelExtraCode.VOUCHER_NO, param.getVoucherNo());
        String extraParamsJson = PayModelUtil.buildExtraParamsJson(param.getPayChannel(), map);
        payModeParam.setExtraParamsJson(extraParamsJson);

        PayParam payParam = new PayParam()
                .setTitle(param.getTitle())
                .setBusinessId(param.getBusinessId())
                .setUserId(SecurityUtils.getUserIdOrDefaultId())
                .setPayModeList(Collections.singletonList(payModeParam));
        PayResult payResult = payService.pay(payParam);

        if (PayStatusCode.TRADE_REFUNDED == payResult.getPayStatus()) {
            throw new PayFailureException("已经退款");
        }
        return payResult;
    }

    /** 扫码发起自动支付 */
    public String aggregatePay(String key, String ua) {
        CashierSinglePayParam cashierSinglePayParam = new CashierSinglePayParam().setPayWay(PayWayCode.QRCODE);
        // 判断是哪种支付方式
        if (ua.contains(PayChannelCode.UA_ALI_PAY)) {
            cashierSinglePayParam.setPayChannel(PayChannelCode.ALI);
        } else if (ua.contains(PayChannelCode.UA_WECHAT_PAY)) {
            cashierSinglePayParam.setPayChannel(PayChannelCode.WECHAT);
        } else {
            throw new PayFailureException("不支持的支付方式");
        }
        String jsonStr = Optional.ofNullable(redisClient.get(PREFIX_KEY + key))
                .orElseThrow(() -> new PayFailureException("支付超时"));
        AggregatePayInfo aggregatePayInfo = JSONUtil.toBean(jsonStr, AggregatePayInfo.class);
        cashierSinglePayParam
                .setTitle(aggregatePayInfo.getTitle())
                .setAmount(aggregatePayInfo.getAmount())
                .setBusinessId(aggregatePayInfo.getBusinessId());
        PayResult payResult = this.singlePay(cashierSinglePayParam);
        return payResult.getAsyncPayInfo().getPayBody();
    }

    /** 组合支付 */
    public PayResult combinationPay(CashierCombinationPayParam param) {
        // 处理支付参数
        List<PayModeParam> payModeList = param.getPayModeList();
        // 删除小于等于零的
        payModeList.removeIf(payModeParam -> BigDecimalUtil.compareTo(payModeParam.getAmount(), BigDecimal.ZERO) < 1);
        if (CollUtil.isEmpty(payModeList)) {
            throw new PayFailureException("支付参数有误");
        }
        // 发起支付
        PayParam payParam = new PayParam()
                .setTitle(param.getTitle())
                .setBusinessId(param.getBusinessId())
                .setUserId(SecurityUtils.getUserIdOrDefaultId())
                .setPayModeList(param.getPayModeList());
        PayResult payResult = payService.pay(payParam);

        if (PayStatusCode.TRADE_REFUNDED == payResult.getPayStatus()) {
            throw new PayFailureException("已经退款");
        }
        return payResult;
    }
}
