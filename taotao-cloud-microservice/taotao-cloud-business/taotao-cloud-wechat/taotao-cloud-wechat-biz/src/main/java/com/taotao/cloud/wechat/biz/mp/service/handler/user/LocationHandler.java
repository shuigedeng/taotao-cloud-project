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

package com.taotao.cloud.wechat.biz.mp.service.handler.user;

import cn.iocoder.yudao.module.mp.framework.mp.core.context.MpContextHolder;
import cn.iocoder.yudao.module.mp.service.message.MpAutoReplyService;
import java.util.Map;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

/**
 * 上报地理位置的事件处理器
 *
 * <p>触发操作：打开微信公众号 -> 点击 + 号 -> 选择「语音」
 *
 * <p>逻辑：粉丝上传地理位置时，也可以触发自动回复
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class LocationHandler implements WxMpMessageHandler {

    @Resource
    private MpAutoReplyService mpAutoReplyService;

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager) {
        // 防御性编程：必须是 LOCATION 消息
        if (ObjectUtil.notEqual(wxMessage.getMsgType(), WxConsts.XmlMsgType.LOCATION)) {
            return null;
        }
        log.info(
                "[handle][上报地理位置，纬度({})、经度({})、精度({})",
                wxMessage.getLatitude(),
                wxMessage.getLongitude(),
                wxMessage.getPrecision());

        // 自动回复
        return mpAutoReplyService.replyForMessage(MpContextHolder.getAppId(), wxMessage);
    }
}
