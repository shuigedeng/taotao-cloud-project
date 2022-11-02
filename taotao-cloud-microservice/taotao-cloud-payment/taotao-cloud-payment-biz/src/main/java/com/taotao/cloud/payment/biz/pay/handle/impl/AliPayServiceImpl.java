/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.payment.biz.pay.handle.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayOpenAuthTokenAppModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.jpay.alipay.AliPayApi;
import com.jpay.alipay.AliPayApiConfigKit;
import com.xhuicloud.common.data.ttl.XHuiCommonThreadLocalHolder;
import com.xhuicloud.pay.config.PayConfigInit;
import com.xhuicloud.common.core.constant.CommonConstants;
import com.xhuicloud.pay.dto.PayOrderDto;
import com.xhuicloud.pay.entity.PayOrderAll;
import com.xhuicloud.pay.handle.PayService;
import com.xhuicloud.pay.properties.PayProperties;
import com.xhuicloud.pay.service.PayOrderAllService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        //买家支付宝账号，和buyer_id不能同时为空
        model.setBuyerId(AliPayApi.openAuthTokenAppToResponse(authTokenAppModel).getUserId());
        AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(model,
                payProperties.getDomain() + payProperties.getAlipay().getNotifyUrl());
        return response.getTradeNo();
    }

    private void alipayWapPay(PayOrderDto payOrderDto, PayOrderAll payOrderAll, Integer tenantId) throws AlipayApiException, IOException {
        // 唤起支付宝
        AlipayTradeWapPayModel aliPayModel = new AlipayTradeWapPayModel();
        aliPayModel.setSubject(payOrderAll.getGoodsTitle());
        aliPayModel.setOutTradeNo(payOrderAll.getOrderNo());
        aliPayModel.setTimeoutExpress(StringUtils.isEmpty(payProperties.getAlipay().getExpireTime())
                ? CommonConstants.TIMEOUT_EXPRESS : payProperties.getAlipay().getExpireTime());
        aliPayModel.setTotalAmount(String.valueOf(payOrderDto.getAmount()));
        aliPayModel.setProductCode("QUICK_WAP_WAY"); //手机网站支付 为固定值
        aliPayModel.setPassbackParams(CommonConstants.TENANT_ID + "=" + tenantId);// 回传参数
        aliPayModel.setQuitUrl(payOrderDto.getQuitUrl());//用户付款中途退出返回商户网站的地址
        AliPayApi.wapPay(response, aliPayModel,
                payProperties.getDomain(),
                payProperties.getDomain() + payProperties.getAlipay().getNotifyUrl());
    }
}
