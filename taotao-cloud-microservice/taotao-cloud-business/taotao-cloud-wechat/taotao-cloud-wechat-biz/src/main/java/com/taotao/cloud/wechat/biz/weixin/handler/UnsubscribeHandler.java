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

package com.taotao.cloud.wechat.biz.weixin.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.weixin.constant.ConfigConstant;
import com.joolun.weixin.entity.WxUser;
import com.joolun.weixin.mapper.WxUserMapper;
import com.joolun.weixin.service.WxMsgService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

/**
 * @author www.joolun.com
 */
@Slf4j
@Component
@AllArgsConstructor
public class UnsubscribeHandler extends AbstractHandler {

    private final WxMsgService wxMsgService;
    private final WxUserMapper wxUserMapper;

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        log.info("取消关注用户 OPENID: " + openId);
        WxUser wxUser = wxUserMapper.selectOne(Wrappers.<WxUser>lambdaQuery().eq(WxUser::getOpenId, openId));
        if (wxUser != null) {
            wxUser.setSubscribe(ConfigConstant.SUBSCRIBE_TYPE_NO);
            wxUser.setCancelSubscribeTime(LocalDateTime.now());
            wxUserMapper.updateById(wxUser);
            // 消息记录
            MsgHandler.getWxMpXmlOutMessage(wxMessage, null, wxUser, wxMsgService);
        }
        return null;
    }
}
