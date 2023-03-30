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
    SettingVO get(@RequestParam(value = "key") String key);

    @GetMapping("/sys/feign/tools/setting/base")
    BaseSetting getBaseSetting(String name);

    /**
     * 获得商品设置
     *
     * @param name 名字
     * @return {@link Result }<{@link GoodsSettingVO }>
     * @since 2022-04-25 16:47:40
     */
    @GetMapping("/sys/feign/tools/setting/goods")
    GoodsSettingVO getGoodsSetting(String name);

    @GetMapping("/sys/feign/tools/setting/order")
    OrderSettingVO getOrderSetting(String name);

    @GetMapping("/sys/feign/tools/setting/experience")
    ExperienceSettingVO getExperienceSetting(String name);

    @GetMapping("/sys/feign/tools/setting/point")
    PointSettingVO getPointSetting(String name);

    @GetMapping("/sys/feign/tools/setting/qq/connect")
    QQConnectSettingVO getQQConnectSetting(String name);

    @GetMapping("/sys/feign/tools/setting/wechat/connect")
    WechatConnectSettingVO getWechatConnectSetting(String name);

    @GetMapping("/sys/feign/tools/setting/seckill")
    SeckillSetting getSeckillSetting(String name);

    @GetMapping("/sys/feign/tools/setting/ali")
    AlipayPaymentSetting getAlipayPaymentSetting(String name);

    @GetMapping("/sys/feign/tools/setting/wechat")
    WechatPaymentSetting getWechatPaymentSetting(String name);
}
