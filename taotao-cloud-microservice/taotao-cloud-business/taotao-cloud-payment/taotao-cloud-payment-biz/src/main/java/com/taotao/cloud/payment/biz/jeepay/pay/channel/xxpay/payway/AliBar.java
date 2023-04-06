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

package com.taotao.cloud.payment.biz.jeepay.pay.channel.xxpay.payway;

import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;
import com.taotao.cloud.payment.biz.jeepay.core.model.params.xxpay.XxpayNormalMchParams;
import com.taotao.cloud.payment.biz.jeepay.pay.channel.xxpay.XxpayPaymentService;
import com.taotao.cloud.payment.biz.jeepay.pay.model.MchAppConfigContext;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.AbstractRS;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.msg.ChannelRetMsg;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.UnifiedOrderRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.payway.AliBarOrderRQ;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.payway.AliBarOrderRS;
import com.taotao.cloud.payment.biz.jeepay.pay.util.ApiResBuilder;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/*
 * 小新支付 支付宝条码支付
 *
 * @author jmdhappy
 * @site https://www.jeequan.com
 * @date 2021/9/20 10:09
 */
@Service("xxpayPaymentByAliBarService") // Service Name需保持全局唯一性
@Slf4j
public class AliBar extends XxpayPaymentService {

    @Override
    public String preCheck(UnifiedOrderRQ rq, PayOrder payOrder) {

        AliBarOrderRQ bizRQ = (AliBarOrderRQ) rq;
        if (StringUtils.isEmpty(bizRQ.getAuthCode())) {
            throw new BizException("用户支付条码[authCode]不可为空");
        }

        return null;
    }

    @Override
    public AbstractRS pay(UnifiedOrderRQ rq, PayOrder payOrder, MchAppConfigContext mchAppConfigContext) {
        AliBarOrderRQ bizRQ = (AliBarOrderRQ) rq;
        XxpayNormalMchParams params = (XxpayNormalMchParams) configContextQueryService.queryNormalMchParams(
                mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId(), getIfCode());
        // 构造支付请求参数
        Map<String, Object> paramMap = new TreeMap();
        paramMap.put("mchId", params.getMchId());
        paramMap.put("productId", "8021"); // 支付宝条码
        paramMap.put("mchOrderNo", payOrder.getPayOrderId());
        paramMap.put("amount", payOrder.getAmount() + "");
        paramMap.put("currency", "cny");
        paramMap.put("clientIp", payOrder.getClientIp());
        paramMap.put("device", "web");
        paramMap.put("returnUrl", getReturnUrl());
        paramMap.put("notifyUrl", getNotifyUrl(payOrder.getPayOrderId()));
        paramMap.put("subject", payOrder.getSubject());
        paramMap.put("body", payOrder.getBody());
        paramMap.put("extra", bizRQ.getAuthCode());
        // 构造函数响应数据
        AliBarOrderRS res = ApiResBuilder.buildSuccess(AliBarOrderRS.class);
        ChannelRetMsg channelRetMsg = new ChannelRetMsg();
        res.setChannelRetMsg(channelRetMsg);
        // 发起支付
        doPay(payOrder, params, paramMap, channelRetMsg);
        return res;
    }
}
