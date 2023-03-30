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

package com.taotao.cloud.wechat.biz.module.cp.handler;

import com.taotao.cloud.wechat.biz.module.cp.builder.TextBuilder;
import java.util.Map;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import org.springframework.stereotype.Component;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Override
    public WxCpXmlOutMessage handle(
            WxCpXmlMessage wxMessage,
            Map<String, Object> context,
            WxCpService cpService,
            WxSessionManager sessionManager)
            throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUserName());

        // 获取微信用户基本信息
        WxCpUser userWxInfo = cpService.getUserService().getById(wxMessage.getFromUserName());

        if (userWxInfo != null) {
            // TODO 可以添加关注用户到本地
        }

        WxCpXmlOutMessage responseResult = null;
        try {
            responseResult = handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("感谢关注", wxMessage, cpService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /** 处理特殊请求，比如如果是扫码进来的，可以做相应处理 */
    private WxCpXmlOutMessage handleSpecial(WxCpXmlMessage wxMessage) {
        // TODO
        return null;
    }
}
