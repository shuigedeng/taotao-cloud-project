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

package com.taotao.cloud.message.biz.websockt.stomp.controller;

import com.taotao.cloud.websocket.stomp.core.MessageConstants;
import com.taotao.cloud.websocket.stomp.processor.WebSocketMessageSender;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

/** 前端使用的 publish 响应接口 */
@RestController
public class WebSocketPublishMessageController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketPublishMessageController.class);

    private final WebSocketMessageSender webSocketMessageSender;

    public WebSocketPublishMessageController(WebSocketMessageSender webSocketMessageSender) {
        this.webSocketMessageSender = webSocketMessageSender;
    }

    // private final DialogueDetailService dialogueDetailService;
    //
    // public WebSocketPublishMessageController(WebSocketMessageSender webSocketMessageSender,
    // DialogueDetailService dialogueDetailService) {
    //    this.webSocketMessageSender = webSocketMessageSender;
    //    this.dialogueDetailService = dialogueDetailService;
    // }

    @MessageMapping("/public/notice")
    @SendTo(MessageConstants.WEBSOCKET_DESTINATION_BROADCAST_NOTICE)
    public String notice(String message, StompHeaderAccessor headerAccessor) {
        System.out.println("---message---" + message);
        if (ObjectUtils.isNotEmpty(headerAccessor)) {
            System.out.println("---id---" + headerAccessor.getUser().getName());
        }

        return message;
    }

    /// **
    // * 发送私信消息。
    // *
    // * @param detail           前端数据 {@link DialogueDetail}
    // * @param headerAccessor 在WebSocketChannelInterceptor拦截器中绑定上的对象
    // */
    // @MessageMapping("/private/message")
    // public void sendPrivateMessage(@Payload DialogueDetail detail, StompHeaderAccessor
    // headerAccessor) {
    //
    //    WebSocketMessage<String> response = new WebSocketMessage<>();
    //    response.setTo(detail.getReceiverId());
    //    response.setChannel(MessageConstants.WEBSOCKET_DESTINATION_PERSONAL_MESSAGE);
    //
    //    if (StringUtils.isNotBlank(detail.getReceiverId()) &&
    // StringUtils.isNotBlank(detail.getReceiverName())) {
    //        if (StringUtils.isBlank(detail.getSenderId()) &&
    // StringUtils.isBlank(detail.getSenderName())) {
    //            WebSocketPrincipal sender = (WebSocketPrincipal) headerAccessor.getUser();
    //            detail.setSenderId(sender.getUserId());
    //            detail.setSenderName(sender.getUserName());
    //            detail.setSenderAvatar(sender.getAvatar());
    //        }
    //
    //        DialogueDetail result = dialogueDetailService.save(detail);
    //
    //        if (ObjectUtils.isNotEmpty(result)) {
    //            response.setPayload("私信发送成功");
    //
    //        } else {
    //            response.setPayload("私信发送失败");
    //        }
    //    } else {
    //        response.setPayload("私信发送失败，参数错误");
    //    }
    //
    //    webSocketMessageSender.toUser(response);
    // }
}
