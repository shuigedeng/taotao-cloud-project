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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxmpp.processor;

import com.taotao.boot.common.utils.common.JsonUtils;
import java.util.Map;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>微信公众号日志处理 </p>
 *
 *
 * @since : 2021/4/7 13:26
 */
@Component
public class WxmppLogHandler implements WxMpMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(WxmppLogHandler.class);

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMpXmlMessage,
            Map<String, Object> map,
            WxMpService wxMpService,
            WxSessionManager wxSessionManager)
            throws WxErrorException {
        // 代码逻辑未实现,仅仅简单打印信息
        log.info("\n接收到请求消息，内容：{}", JsonUtils.toJson(wxMpXmlMessage));
        return null;
    }
}
