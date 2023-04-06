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

package com.taotao.cloud.message.biz.austin.handler.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.taotao.cloud.message.biz.austin.common.dto.account.AlipayMiniProgramAccount;
import java.util.HashMap;
import java.util.Map;

/**
 * 初始化支付宝小程序 单例
 *
 * @author 丁新东
 * @date 2022-12-07
 */
public class AlipayClientSingleton {

    private static volatile DefaultAlipayClient alipayClientSingleton;

    private static Map<String, DefaultAlipayClient> alipayClientMap = new HashMap<>();

    private AlipayClientSingleton() {}

    public static DefaultAlipayClient getSingleton(AlipayMiniProgramAccount alipayMiniProgramAccount)
            throws AlipayApiException {
        if (!alipayClientMap.containsKey(alipayMiniProgramAccount.getAppId())) {
            synchronized (DefaultAlipayClient.class) {
                if (!alipayClientMap.containsKey(alipayMiniProgramAccount.getAppId())) {
                    AlipayConfig alipayConfig = new AlipayConfig();
                    alipayConfig.setServerUrl("https://openapi.alipaydev.com/gateway.do");
                    alipayConfig.setAppId(alipayMiniProgramAccount.getAppId());
                    alipayConfig.setPrivateKey(alipayMiniProgramAccount.getPrivateKey());
                    alipayConfig.setFormat("json");
                    alipayConfig.setAlipayPublicKey(alipayMiniProgramAccount.getAlipayPublicKey());
                    alipayConfig.setCharset("utf-8");
                    alipayConfig.setSignType("RSA2");
                    alipayClientSingleton = new DefaultAlipayClient(alipayConfig);
                    alipayClientMap.put(alipayMiniProgramAccount.getAppId(), alipayClientSingleton);
                    return alipayClientSingleton;
                }
            }
        }
        return alipayClientMap.get(alipayMiniProgramAccount.getAppId());
    }
}
