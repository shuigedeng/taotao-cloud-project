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

package com.taotao.cloud.message.biz.austin.web.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.constant.OfficialAccountParamConstant;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 3y 微信服务号 关注 事件 处理器 将eventKey 存储在 redis
 */
@Component("subscribeHandler")
@Slf4j
public class SubscribeHandler implements WxMpMessageHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 拿到场景值和用户信息，写入到redis
     *
     * @param wxMessage
     * @param context
     * @param wxMpService
     * @param sessionManager
     * @return
     */
    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager) {
        try {
            WxMpUser user = wxMpService.getUserService().userInfo(wxMessage.getFromUser());
            String eventKey = wxMessage
                    .getEventKey()
                    .replaceAll(OfficialAccountParamConstant.QR_CODE_SCENE_PREFIX, CommonConstant.EMPTY_STRING);
            redisTemplate.opsForValue().set(eventKey, JSON.toJSONString(user), 30, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("SubscribeHandler#handle fail:{}", Throwables.getStackTraceAsString(e));
        }
        return WxMpXmlOutMessage.TEXT()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .content(OfficialAccountParamConstant.SUBSCRIBE_TIPS)
                .build();
    }
}
