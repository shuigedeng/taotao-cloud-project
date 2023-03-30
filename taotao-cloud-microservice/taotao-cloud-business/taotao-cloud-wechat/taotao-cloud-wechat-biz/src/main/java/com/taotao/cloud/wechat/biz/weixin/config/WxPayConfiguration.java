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

package com.taotao.cloud.wechat.biz.weixin.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付Configuration
 *
 * @author www.joolun.com
 */
@Slf4j
@Configuration
public class WxPayConfiguration {

    private static WxMaProperties wxMaProperties;

    @Autowired
    public WxPayConfiguration(WxMaProperties wxMaProperties) {
        this.wxMaProperties = wxMaProperties;
    }

    /**
     * 获取WxMpService
     *
     * @return
     */
    public static WxPayService getPayService() {
        WxPayService wxPayService = null;
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxMaProperties.getConfigs().get(0).getAppId());
        payConfig.setMchId(wxMaProperties.getConfigs().get(0).getMchId());
        payConfig.setMchKey(wxMaProperties.getConfigs().get(0).getMchKey());
        payConfig.setKeyPath(wxMaProperties.getConfigs().get(0).getKeyPath());
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
