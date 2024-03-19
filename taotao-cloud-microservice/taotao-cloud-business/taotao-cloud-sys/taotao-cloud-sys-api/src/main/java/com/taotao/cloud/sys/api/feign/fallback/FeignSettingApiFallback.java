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

import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.feign.response.setting.FeignAlipayPaymentSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignBaseSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignExperienceSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignGoodsSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignOrderSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignPointSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignQQConnectSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignSeckillSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignWechatConnectSettingResponse;
import com.taotao.cloud.sys.api.feign.response.setting.FeignWechatPaymentSettingResponse;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignSettingFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignSettingApiFallback implements FallbackFactory<IFeignSettingApi> {

    @Override
    public IFeignSettingApi create(Throwable throwable) {
        return new IFeignSettingApi() {
            @Override
            public FeignSettingResponse get(String key) {
                return null;
            }

            @Override
            public FeignBaseSettingResponse getBaseSetting(String name) {
                return null;
            }

            @Override
            public FeignGoodsSettingResponse getGoodsSetting(String name) {
                return null;
            }

            @Override
            public FeignOrderSettingResponse getOrderSetting(String name) {
                return null;
            }

            @Override
            public FeignExperienceSettingResponse getExperienceSetting(String name) {
                return null;
            }

            @Override
            public FeignPointSettingResponse getPointSetting(String name) {
                return null;
            }

            @Override
            public FeignQQConnectSettingResponse getQQConnectSetting(String name) {
                return null;
            }

            @Override
            public FeignWechatConnectSettingResponse getWechatConnectSetting(String name) {
                return null;
            }

            @Override
            public FeignSeckillSettingResponse getSeckillSetting(String name) {
                return null;
            }

            @Override
            public FeignAlipayPaymentSettingResponse getAlipayPaymentSetting(String name) {
                return null;
            }

            @Override
            public FeignWechatPaymentSettingResponse getWechatPaymentSetting(String name) {
                return null;
            }
        };
    }
}
