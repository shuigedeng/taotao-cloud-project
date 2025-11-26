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

package com.taotao.cloud.wechat.biz.module.open.controller;

import com.taotao.cloud.wechat.biz.module.open.service.WxOpenServiceDemo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@RestController
@RequestMapping("/notify")
public class WechatNotifyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected WxOpenServiceDemo wxOpenService;

    @RequestMapping("/receive_ticket")
    public Object receiveTicket(
            @RequestBody(required = false) String requestBody,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("signature") String signature,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        this.logger.info(
                "\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature,
                encType,
                msgSignature,
                timestamp,
                nonce,
                requestBody);

        if (!StringUtils.equalsIgnoreCase("aes", encType)
                || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        // aes加密的消息
        WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(
                requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        try {
            String out = wxOpenService.getWxOpenComponentService().route(inMessage);
            this.logger.debug("\n组装回复信息：{}", out);
        } catch (WxErrorException e) {
            this.logger.error("receive_ticket", e);
        }

        return "success";
    }

    @RequestMapping("{appId}/callback")
    public Object callback(
            @RequestBody(required = false) String requestBody,
            @PathVariable("appId") String appId,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("openid") String openid,
            @RequestParam("encrypt_type") String encType,
            @RequestParam("msg_signature") String msgSignature) {
        this.logger.info(
                "\n"
                        + "接收微信请求：[appId=[{}], openid=[{}], signature=[{}], encType=[{}],"
                        + " msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n"
                        + "{}\n"
                        + "] ",
                appId,
                openid,
                signature,
                encType,
                msgSignature,
                timestamp,
                nonce,
                requestBody);
        if (!StringUtils.equalsIgnoreCase("aes", encType)
                || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = "";
        // aes加密的消息
        WxMpXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedMpXml(
                requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        // 全网发布测试用例
        if (StringUtils.equalsAnyIgnoreCase(appId, "wxd101a85aa106f53e", "wx570bc396a51b8ff8")) {
            try {
                if (StringUtils.equals(inMessage.getMsgType(), "text")) {
                    if (StringUtils.equals(inMessage.getContent(), "TESTCOMPONENT_MSG_TYPE_TEXT")) {
                        out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(
                                WxMpXmlOutMessage.TEXT()
                                        .content("TESTCOMPONENT_MSG_TYPE_TEXT_callback")
                                        .fromUser(inMessage.getToUser())
                                        .toUser(inMessage.getFromUser())
                                        .build(),
                                wxOpenService.getWxOpenConfigStorage());
                    } else if (StringUtils.startsWith(inMessage.getContent(), "QUERY_AUTH_CODE:")) {
                        String msg = inMessage.getContent().replace("QUERY_AUTH_CODE:", "") + "_from_api";
                        WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT()
                                .content(msg)
                                .toUser(inMessage.getFromUser())
                                .build();
                        wxOpenService
                                .getWxOpenComponentService()
                                .getWxMpServiceByAppid(appId)
                                .getKefuService()
                                .sendKefuMessage(kefuMessage);
                    }
                } else if (StringUtils.equals(inMessage.getMsgType(), "event")) {
                    WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT()
                            .content(inMessage.getEvent() + "from_callback")
                            .toUser(inMessage.getFromUser())
                            .build();
                    wxOpenService
                            .getWxOpenComponentService()
                            .getWxMpServiceByAppid(appId)
                            .getKefuService()
                            .sendKefuMessage(kefuMessage);
                }
            } catch (WxErrorException e) {
                logger.error("callback", e);
            }
        } else {
            WxMpXmlOutMessage outMessage =
                    wxOpenService.getWxOpenMessageRouter().route(inMessage, appId);
            if (outMessage != null) {
                out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(
                        outMessage, wxOpenService.getWxOpenConfigStorage());
            }
        }
        return out;
    }
}
