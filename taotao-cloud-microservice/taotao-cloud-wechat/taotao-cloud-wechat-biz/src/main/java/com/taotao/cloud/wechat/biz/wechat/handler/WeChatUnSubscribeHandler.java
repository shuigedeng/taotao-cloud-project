package com.taotao.cloud.wechat.biz.wechat.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
* 用户取消关注订阅消息
* @author xxm  
* @date 2022/7/16 
*/
public class WeChatUnSubscribeHandler implements WeChatMpMessageHandler{
    @Override
    public String getEvent() {
        return WxConsts.EventType.UNSUBSCRIBE;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        return null;
    }
}
