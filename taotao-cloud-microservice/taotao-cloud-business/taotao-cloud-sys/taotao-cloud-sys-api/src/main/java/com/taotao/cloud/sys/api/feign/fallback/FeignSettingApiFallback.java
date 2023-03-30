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
import com.taotao.cloud.sys.api.model.vo.setting.BaseSetting;
import com.taotao.cloud.sys.api.model.vo.setting.ExperienceSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.GoodsSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.OrderSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.PointSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.QQConnectSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.SeckillSetting;
import com.taotao.cloud.sys.api.model.vo.setting.SettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.WechatConnectSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.payment.AlipayPaymentSetting;
import com.taotao.cloud.sys.api.model.vo.setting.payment.WechatPaymentSetting;
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
            public SettingVO get(String key) {
                return null;
            }

            @Override
            public BaseSetting getBaseSetting(String name) {
                return null;
            }

            @Override
            public GoodsSettingVO getGoodsSetting(String name) {
                return null;
            }

            @Override
            public OrderSettingVO getOrderSetting(String name) {
                return null;
            }

            @Override
            public ExperienceSettingVO getExperienceSetting(String name) {
                return null;
            }

            @Override
            public PointSettingVO getPointSetting(String name) {
                return null;
            }

            @Override
            public QQConnectSettingVO getQQConnectSetting(String name) {
                return null;
            }

            @Override
            public WechatConnectSettingVO getWechatConnectSetting(String name) {
                return null;
            }

            @Override
            public SeckillSetting getSeckillSetting(String name) {
                return null;
            }

            @Override
            public AlipayPaymentSetting getAlipayPaymentSetting(String name) {
                return null;
            }

            @Override
            public WechatPaymentSetting getWechatPaymentSetting(String name) {
                return null;
            }
        };
    }
}
