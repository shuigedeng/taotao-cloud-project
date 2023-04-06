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

package com.taotao.cloud.im.biz.platform.modules.chat.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.service.ChatMsgService;
import com.platform.modules.chat.vo.ChatVo01;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 聊天 */
@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Resource
    private ChatMsgService chatMsgService;

    /** 发送信息 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/sendMsg")
    public AjaxResult sendMsg(@Validated @RequestBody ChatVo01 chatVo) {
        return AjaxResult.success(chatMsgService.sendFriendMsg(chatVo));
    }

    /** 获取大消息 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getBigMsg/{msgId}")
    public AjaxResult getBigMsg(@PathVariable String msgId) {
        return AjaxResult.success(chatMsgService.getBigMsg(msgId));
    }
}
