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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.processor;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import java.util.Map;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>微信小程序Log处理器 </p>
 *
 *
 * @since : 2021/4/7 12:44
 */
public class WxappLogHandler implements WxMaMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(WxappLogHandler.class);

    @Override
    public WxMaXmlOutMessage handle(
            WxMaMessage wxMaMessage,
            Map<String, Object> context,
            WxMaService wxMaService,
            WxSessionManager sessionManager)
            throws WxErrorException {
        log.info("收到消息：" + wxMaMessage.toString());
        wxMaService
                .getMsgService()
                .sendKefuMsg(WxMaKefuMessage.newTextBuilder()
                        .content("收到信息为：" + wxMaMessage.toJson())
                        .toUser(wxMaMessage.getFromUser())
                        .build());
        return null;
    }
}
