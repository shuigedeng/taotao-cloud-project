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

package com.taotao.cloud.payment.biz.pay.handle.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayOpenAuthTokenAppModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.jpay.alipay.AliPayApi;
import com.jpay.alipay.AliPayApiConfigKit;
import com.xhuicloud.common.core.constant.CommonConstants;
import com.xhuicloud.common.data.ttl.XHuiCommonThreadLocalHolder;
import com.xhuicloud.pay.config.PayConfigInit;
import com.xhuicloud.pay.dto.PayOrderDto;
import com.xhuicloud.pay.entity.PayOrderAll;
import com.xhuicloud.pay.handle.PayService;
import com.xhuicloud.pay.properties.PayProperties;
import com.xhuicloud.pay.service.PayOrderAllService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @program: XHuiCloud
 * @description: AliPaySerciceImpl
 * @author: Sinda
 * @create: 2020-06-05 10:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class AliPayServiceImpl implements PayService {

    private final HttpServletResponse response;

    private final PayOrderAllService payOrderAllService;

    private final PayProperties payProperties;

    @Override
    public Object pay(PayOrderDto payOrderDto) {
        // 创建本地预交易订单 并唤起支付宝
        return openAliPay(payOrderDto, payOrderAllService.create(payOrderDto));
    }

    /**
     * 唤起支付宝
     *
     * @param payOrderDto
     * @param payOrderAll
     */
    private Object openAliPay(PayOrderDto payOrderDto, PayOrderAll payOrderAll) {
        try {
            // 根据租户 选择支付商户号
            Integer tenantId = XHuiCommonThreadLocalHolder.getTenant();
            AliPayApiConfigKit.setThreadLocalAppId(PayConfigInit.tenantIdAliPayAppIdMaps.get(tenantId));
            // wap 支付
            alipayWapPay(payOrderDto, payOrderAll, tenantId);
            // tradeCreate 支付
            // return alipayTradeCreate(payOrderDto, payOrderAll, tenantId);
        } catch (Exception e) {
            log.error("支付宝手机支付失败", e);
        }
        return null;
    }

    @SneakyThrows
    private Object alipayTradeCreate(PayOrderDto payOrderDto, PayOrderAll payOrderAll, Integer tenantId) {
        AlipayOpenAuthTokenAppModel authTokenAppModel = new AlipayOpenAuthTokenAppModel();
        authTokenAppModel.setGrantType("authorization_code");
        authTokenAppModel.setCode(payOrderDto.getCode());

        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(payOrderDto.getOrderNo());
        model.setTotalAmount(String.valueOf(payOrderDto.getAmount()));
        model.setSubject(payOrderAll.getGoodsTitle());
        // 买家支付宝账号，和buyer_id不能同时为空
        model.setBuyerId(AliPayApi.openAuthTokenAppToResponse(authTokenAppModel).getUserId());
        AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(
                model, payProperties.getDomain() + payProperties.getAlipay().getNotifyUrl());
        return response.getTradeNo();
    }

    private void alipayWapPay(PayOrderDto payOrderDto, PayOrderAll payOrderAll, Integer tenantId)
            throws AlipayApiException, IOException {
        // 唤起支付宝
        AlipayTradeWapPayModel aliPayModel = new AlipayTradeWapPayModel();
        aliPayModel.setSubject(payOrderAll.getGoodsTitle());
        aliPayModel.setOutTradeNo(payOrderAll.getOrderNo());
        aliPayModel.setTimeoutExpress(
                StringUtils.isEmpty(payProperties.getAlipay().getExpireTime())
                        ? CommonConstants.TIMEOUT_EXPRESS
                        : payProperties.getAlipay().getExpireTime());
        aliPayModel.setTotalAmount(String.valueOf(payOrderDto.getAmount()));
        aliPayModel.setProductCode("QUICK_WAP_WAY"); // 手机网站支付 为固定值
        aliPayModel.setPassbackParams(CommonConstants.TENANT_ID + "=" + tenantId); // 回传参数
        aliPayModel.setQuitUrl(payOrderDto.getQuitUrl()); // 用户付款中途退出返回商户网站的地址
        AliPayApi.wapPay(
                response,
                aliPayModel,
                payProperties.getDomain(),
                payProperties.getDomain() + payProperties.getAlipay().getNotifyUrl());
    }
}
