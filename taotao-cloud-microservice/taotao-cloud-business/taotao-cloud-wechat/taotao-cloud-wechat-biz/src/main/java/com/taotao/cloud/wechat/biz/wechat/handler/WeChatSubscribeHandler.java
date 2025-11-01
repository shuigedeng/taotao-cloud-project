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

package com.taotao.cloud.wechat.biz.wechat.handler;

import static cn.bootx.starter.wechat.WeChatCode.EVENT_KEY_QRSCENE;

import cn.bootx.starter.wechat.core.login.service.WeChatQrLoginService;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.springframework.stereotype.Component;

/**
 * 新增关注订阅消息
 *
 * @author xxm
 * @since 2022/7/16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatSubscribeHandler implements WeChatMpMessageHandler {
    private final WeChatQrLoginService weChatQrLoginService;

    @Override
    public String getEvent() {
        return WxConsts.EventType.SUBSCRIBE;
    }

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        log.info("新关注用户 OPENID: " + openId);
        // 判断是否携带参数, 携带参数出厂扫码情况
        if (StrUtil.startWith(wxMessage.getEventKey(), EVENT_KEY_QRSCENE)) {
            // 二维码key值
            String qrCodeKey = StrUtil.subAfter(wxMessage.getEventKey(), EVENT_KEY_QRSCENE, true);
            weChatQrLoginService.setOpenId(qrCodeKey, openId);
        }

        return new TextBuilder()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .content("感谢关注")
                .build();
    }
}
