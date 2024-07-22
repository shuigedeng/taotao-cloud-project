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

package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.sys.api.feign.SettingApi;
import com.taotao.cloud.sys.api.feign.response.setting.AlipayPaymentSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.BaseSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.ExperienceSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.GoodsSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.OrderSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.PointSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.QQConnectSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.SeckillSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.SettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.WechatConnectSettingApiResponse;
import com.taotao.cloud.sys.api.feign.response.setting.WechatPaymentSettingApiResponse;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignSettingFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class SettingApiFallback implements FallbackFactory<SettingApi> {

    @Override
    public SettingApi create(Throwable throwable) {
        return new SettingApi() {
            @Override
            public SettingApiResponse get(String key) {
                return null;
            }

            @Override
            public BaseSettingApiResponse getBaseSetting(String name) {
                return null;
            }

            @Override
            public GoodsSettingApiResponse getGoodsSetting(String name) {
                return null;
            }

            @Override
            public OrderSettingApiResponse getOrderSetting(String name) {
                return null;
            }

            @Override
            public ExperienceSettingApiResponse getExperienceSetting(String name) {
                return null;
            }

            @Override
            public PointSettingApiResponse getPointSetting(String name) {
                return null;
            }

            @Override
            public QQConnectSettingApiResponse getQQConnectSetting(String name) {
                return null;
            }

            @Override
            public WechatConnectSettingApiResponse getWechatConnectSetting(String name) {
                return null;
            }

            @Override
            public SeckillSettingApiResponse getSeckillSetting(String name) {
                return null;
            }

            @Override
            public AlipayPaymentSettingApiResponse getAlipayPaymentSetting(String name) {
                return null;
            }

            @Override
            public WechatPaymentSettingApiResponse getWechatPaymentSetting(String name) {
                return null;
            }
        };
    }
}
