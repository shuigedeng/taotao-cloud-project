package com.taotao.cloud.wechat.biz.wechat.handler;

import cn.bootx.starter.wechat.core.login.service.WeChatQrLoginService;
import cn.hutool.core.util.StrUtil;
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

import static cn.bootx.starter.wechat.WeChatCode.EVENT_KEY_QRSCENE;

/**
* 新增关注订阅消息
* @author xxm
* @date 2022/7/16
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
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String openId = wxMessage.getFromUser();
        log.info("新关注用户 OPENID: " + openId);
        // 判断是否携带参数, 携带参数出厂扫码情况
        if (StrUtil.startWith(wxMessage.getEventKey(),EVENT_KEY_QRSCENE)){
            // 二维码key值
            String qrCodeKey = StrUtil.subAfter(wxMessage.getEventKey(), EVENT_KEY_QRSCENE, true);
            weChatQrLoginService.setOpenId(qrCodeKey,openId);
        }

        return new TextBuilder()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .content("感谢关注").build();
    }
}
