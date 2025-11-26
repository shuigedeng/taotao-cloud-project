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

package com.taotao.cloud.payment.biz.pay.config;

import static com.xhuicloud.common.core.constant.AuthorizationConstants.IS_COMMING_ANONYMOUS_YES;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.jpay.alipay.AliPayApiConfig;
import com.jpay.alipay.AliPayApiConfigKit;
import com.xhuicloud.common.core.enums.pay.PayTypeEnum;
import com.xhuicloud.common.data.ttl.XHuiCommonThreadLocalHolder;
import com.xhuicloud.pay.entity.PayChannel;
import com.xhuicloud.pay.service.PayChannelService;
import com.xhuicloud.upms.feign.SysTenantServiceFeign;
import com.xhuicloud.upms.vo.TenantVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @program: XHuiCloud
 * @description: 支付方式初始化
 * @author: Sinda
 * @create: 2020-06-03 11:53
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class PayConfigInit {

    private final SysTenantServiceFeign sysTenantServiceFeign;

    private final PayChannelService payChannelService;

    public static final Map<Integer, String> tenantIdAliPayAppIdMaps = Maps.newHashMap();

    public static final Map<Integer, TenantVo> tenantMaps = Maps.newHashMap();

    @Async
    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void init() {

        List<PayChannel> payChannels = new ArrayList<>();
        List<TenantVo> data = null;
        while (CollectionUtil.isEmpty(data)) {
            data = sysTenantServiceFeign.list(IS_COMMING_ANONYMOUS_YES).getData();
        }
        data.forEach(tenant -> {
            XHuiCommonThreadLocalHolder.setTenant(tenant.getId());

            List<PayChannel> payChannelList = payChannelService.list(Wrappers.<PayChannel>lambdaQuery()
                    .eq(PayChannel::getDelFlag, 1)
                    .eq(PayChannel::getTenantId, tenant.getId()));
            payChannels.addAll(payChannelList);
            tenantMaps.put(tenant.getId(), tenant);
        });

        payChannels.forEach(payChannel -> {
            tenantIdAliPayAppIdMaps.put(payChannel.getTenantId(), payChannel.getAppId());
        });

        payChannels.forEach(payChannel -> {
            JSONObject params = JSONUtil.parseObj(payChannel.getConfig());
            if (StringUtils.equals(payChannel.getChannelId(), (PayTypeEnum.ALIPAY_WAP.getType()))) {
                AliPayApiConfig aliPayApiConfig = AliPayApiConfig.New()
                        .setAppId(payChannel.getAppId())
                        .setPrivateKey(params.getStr("privateKey"))
                        .setCharset(CharsetUtil.UTF_8)
                        .setAlipayPublicKey(params.getStr("alipayPublicKey"))
                        .setServiceUrl(params.getStr("serviceUrl"))
                        .setSignType("RSA2")
                        .build();
                AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
                log.info("AliPay支付渠道初始化完成:AppId:{}", payChannel.getAppId());
            }
        });
    }
}
