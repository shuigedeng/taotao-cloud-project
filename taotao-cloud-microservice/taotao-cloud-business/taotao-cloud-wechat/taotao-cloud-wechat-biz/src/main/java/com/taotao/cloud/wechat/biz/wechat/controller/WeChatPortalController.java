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

package com.taotao.cloud.wechat.biz.wechat.controller;

import cn.bootx.common.core.annotation.IgnoreAuth;
import cn.bootx.starter.wechat.core.portal.service.WeChatPortalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 微信接入入口
 *
 * @author xxm
 * @since 2022/7/16
 */
@IgnoreAuth
@Slf4j
@Tag(name = "微信接入入口")
@RestController
@RequestMapping("/wechat/portal")
@RequiredArgsConstructor
public class WeChatPortalController {
    private final WeChatPortalService weChatPortalService;

    /**
     * 微信接入校验处理
     *
     * @param signature 微信签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String auth(String signature, String timestamp, String nonce, String echostr) {
        return weChatPortalService.auth(signature, timestamp, nonce, echostr);
    }

    /**
     * 微信消息处理
     *
     * @param requestBody 请求报文体
     * @param signature 微信签名
     * @param encType 加签方式
     * @param msgSignature 微信签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(
            @RequestBody String requestBody,
            String signature,
            String timestamp,
            String nonce,
            String openid,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        return weChatPortalService.handleMessage(
                requestBody, signature, timestamp, nonce, openid, encType, msgSignature);
    }
}
