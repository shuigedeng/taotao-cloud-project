package com.taotao.cloud.wechat.biz.wechat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
* 新增关注订阅消息
* @author xxm
* @date 2022/7/16
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatSubscribeHandler implements WeChatMpMessageHandler {

    @Override
    public String getEvent() {
        return WxConsts.EventType.SUBSCRIBE;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        return new TextBuilder()
                .toUser(wxMessage.getFromUser())
                .content("感谢关注").build();
    }
}
