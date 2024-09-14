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

package com.taotao.cloud.sys.api.feign;

import com.taotao.boot.common.constant.ServiceName;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.SettingApiFallback;
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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台配置模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:48
 */
@FeignClient(
        name = ServiceName.TAOTAO_CLOUD_SYS,
        contextId = "IFeignSettingApi",
        fallbackFactory = SettingApiFallback.class)
public interface SettingApi {

    /**
     * 通过key获取配置
     *
     * @param key key
     * @return 配置信息
     * @since 2022-03-25 14:10:22
     */
    @GetMapping("/sys/feign/tools/setting")
	SettingApiResponse get(@RequestParam(value = "key") String key);

    @GetMapping("/sys/feign/tools/setting/base")
	BaseSettingApiResponse getBaseSetting(String name);

    /**
     * 获得商品设置
     *
     * @param name 名字
     * @return {@link Result }<{@link GoodsSettingApiResponse }>
     * @since 2022-04-25 16:47:40
     */
    @GetMapping("/sys/feign/tools/setting/goods")
	GoodsSettingApiResponse getGoodsSetting(String name);

    @GetMapping("/sys/feign/tools/setting/order")
	OrderSettingApiResponse getOrderSetting(String name);

    @GetMapping("/sys/feign/tools/setting/experience")
	ExperienceSettingApiResponse getExperienceSetting(String name);

    @GetMapping("/sys/feign/tools/setting/point")
	PointSettingApiResponse getPointSetting(String name);

    @GetMapping("/sys/feign/tools/setting/qq/connect")
	QQConnectSettingApiResponse getQQConnectSetting(String name);

    @GetMapping("/sys/feign/tools/setting/wechat/connect")
	WechatConnectSettingApiResponse getWechatConnectSetting(String name);

    @GetMapping("/sys/feign/tools/setting/seckill")
	SeckillSettingApiResponse getSeckillSetting(String name);

    @GetMapping("/sys/feign/tools/setting/ali")
		AlipayPaymentSettingApiResponse getAlipayPaymentSetting(String name);

    @GetMapping("/sys/feign/tools/setting/wechat")
	WechatPaymentSettingApiResponse getWechatPaymentSetting(String name);
}
