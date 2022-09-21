package com.taotao.cloud.wechat.biz.wechat.handler;

import cn.bootx.starter.wechat.core.login.service.WeChatQrLoginService;
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
 * 微信扫码事件
 * @author xxm  
 * @date 2022/8/4
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatQrScanHandler implements WeChatMpMessageHandler{
    private final WeChatQrLoginService weChatQrLoginService;

    @Override
    public String getEvent() {
        return WxConsts.EventType.SCAN;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) {
        // 扫描用户的OpenId
        String openId = wxMessage.getFromUser();
        // 二维码key值
        String qrCodeKey = wxMessage.getEventKey();
        weChatQrLoginService.setOpenId(qrCodeKey,openId);

        return new TextBuilder()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .content("感谢关注").build();
    }
}
