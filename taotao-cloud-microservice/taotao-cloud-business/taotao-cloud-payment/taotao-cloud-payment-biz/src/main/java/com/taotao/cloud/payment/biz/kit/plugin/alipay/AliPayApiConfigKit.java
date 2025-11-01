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

import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.model.vo.setting.payment.AlipayPaymentSetting;
import java.util.Date;

/** AliPayApiConfigKit */
public class AliPayApiConfigKit {

    /** 支付配置 */
    static DefaultAlipayClient defaultAlipayClient;

    /** 下次刷新时间 */
    static Date nextRebuildDate;

    /** 间隔时间 */
    static Long refreshInterval = 1000 * 60 * 3L;

    /**
     * 获取支付宝支付参数
     *
     * @return
     * @throws AlipayApiException
     */
    public static synchronized DefaultAlipayClient getAliPayApiConfig() throws AlipayApiException {
        Date date = new Date();
        // 如果过期，则重新构建
        if (nextRebuildDate == null || date.after(nextRebuildDate)) {
            return rebuild();
        }
        return defaultAlipayClient;
    }

    static DefaultAlipayClient rebuild() throws AlipayApiException {
        AlipayPaymentSetting setting;
        try {
            SettingService settingService = (SettingService) SpringContextUtil.getBean("settingServiceImpl");
            Setting systemSetting = settingService.get(SettingCategoryEnum.ALIPAY_PAYMENT.name());
            setting = JSONUtil.toBean(systemSetting.getSettingValue(), AlipayPaymentSetting.class);
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.PAY_NOT_SUPPORT);
        }

        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
        certAlipayRequest.setFormat("json");
        certAlipayRequest.setCharset("utf-8");
        certAlipayRequest.setSignType("RSA2");
        certAlipayRequest.setAppId(setting.getAppId());
        certAlipayRequest.setPrivateKey(setting.getPrivateKey());
        certAlipayRequest.setCertPath(setting.getCertPath());
        certAlipayRequest.setAlipayPublicCertPath(setting.getAlipayPublicCertPath());
        certAlipayRequest.setRootCertPath(setting.getRootCertPath());
        defaultAlipayClient = new DefaultAlipayClient(certAlipayRequest);
        nextRebuildDate = DateUtil.date(System.currentTimeMillis() + refreshInterval);
        return defaultAlipayClient;
    }
}
