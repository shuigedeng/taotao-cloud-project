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

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignSettingApiFallback;
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
        fallbackFactory = FeignSettingApiFallback.class)
public interface IFeignSettingApi {

    /**
     * 通过key获取配置
     *
     * @param key key
     * @return 配置信息
     * @since 2022-03-25 14:10:22
     */
    @GetMapping("/sys/feign/tools/setting")
	FeignSettingResponse get(@RequestParam(value = "key") String key);

    @GetMapping("/sys/feign/tools/setting/base")
	FeignBaseSettingResponse getBaseSetting(String name);

    /**
     * 获得商品设置
     *
     * @param name 名字
     * @return {@link Result }<{@link FeignGoodsSettingResponse }>
     * @since 2022-04-25 16:47:40
     */
    @GetMapping("/sys/feign/tools/setting/goods")
	FeignGoodsSettingResponse getGoodsSetting(String name);

    @GetMapping("/sys/feign/tools/setting/order")
	FeignOrderSettingResponse getOrderSetting(String name);

    @GetMapping("/sys/feign/tools/setting/experience")
	FeignExperienceSettingResponse getExperienceSetting(String name);

    @GetMapping("/sys/feign/tools/setting/point")
	FeignPointSettingResponse getPointSetting(String name);

    @GetMapping("/sys/feign/tools/setting/qq/connect")
	FeignQQConnectSettingResponse getQQConnectSetting(String name);

    @GetMapping("/sys/feign/tools/setting/wechat/connect")
	FeignWechatConnectSettingResponse getWechatConnectSetting(String name);

    @GetMapping("/sys/feign/tools/setting/seckill")
	FeignSeckillSettingResponse getSeckillSetting(String name);

    @GetMapping("/sys/feign/tools/setting/ali")
	FeignAlipayPaymentSettingResponse getAlipayPaymentSetting(String name);

    @GetMapping("/sys/feign/tools/setting/wechat")
	FeignWechatPaymentSettingResponse getWechatPaymentSetting(String name);
}
