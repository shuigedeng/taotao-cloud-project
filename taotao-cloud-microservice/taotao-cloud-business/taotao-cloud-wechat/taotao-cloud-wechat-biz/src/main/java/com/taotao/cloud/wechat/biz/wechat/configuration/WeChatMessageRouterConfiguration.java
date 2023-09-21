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

package com.taotao.cloud.wechat.biz.wechat.configuration;

import cn.bootx.starter.wechat.handler.WeChatMpMessageHandler;
import cn.bootx.starter.wechat.handler.WeChatMsgHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信信息路由配置
 *
 * @author xxm
 * @since 2022/7/16
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WeChatMessageRouterConfiguration {

    private final List<WeChatMpMessageHandler> weChatMpMessageHandlers;
    private final WeChatMsgHandler weChatMsgHandler;

    @Bean
    public WxMpMessageRouter wxMpMessageRouter(WxMpService wxMpService) {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        // 记录日志

        // 消息路由绑定
        for (WeChatMpMessageHandler weChatMpMessageHandler : weChatMpMessageHandlers) {
            router.rule()
                    .async(false)
                    .msgType(weChatMpMessageHandler.getMsgType())
                    .event(weChatMpMessageHandler.getEvent())
                    .handler(weChatMpMessageHandler)
                    .end();
        }
        // 默认的 文本消息处理
        router.rule().async(false).handler(weChatMsgHandler).end();
        return router;
    }
}
