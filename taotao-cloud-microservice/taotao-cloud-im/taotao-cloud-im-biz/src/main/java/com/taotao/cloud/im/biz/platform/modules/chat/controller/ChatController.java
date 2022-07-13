package com.taotao.cloud.im.biz.platform.modules.chat.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.service.ChatMsgService;
import com.platform.modules.chat.vo.ChatVo01;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 聊天
 */
@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Resource
    private ChatMsgService chatMsgService;

    /**
     * 发送信息
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/sendMsg")
    public AjaxResult sendMsg(@Validated @RequestBody ChatVo01 chatVo) {
        return AjaxResult.success(chatMsgService.sendFriendMsg(chatVo));
    }

    /**
     * 获取大消息
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getBigMsg/{msgId}")
    public AjaxResult getBigMsg(@PathVariable String msgId) {
        return AjaxResult.success(chatMsgService.getBigMsg(msgId));
    }

}
