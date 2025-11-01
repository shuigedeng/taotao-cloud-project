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

package com.taotao.cloud.wechat.biz.wecom.core.notice.service;

import static cn.bootx.starter.wecom.code.WeComCode.NOTICE_RECALL_URL;

import cn.bootx.starter.wecom.configuration.WeComProperties;
import cn.bootx.starter.wecom.core.notice.executor.RecallNoticeRequestExecutor;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.cp.api.WxCpMediaService;
import me.chanjar.weixin.cp.api.WxCpMessageService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import org.springframework.stereotype.Service;

/**
 * 企业微信消息发送
 *
 * @author xxm
 * @since 2022/7/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeComNoticeService {
    private final WxCpService wxCpService;
    private final WeComProperties weComProperties;

    /**
     * 发送应用消息
     *
     * @return 消息id, 可用于撤回
     */
    @SneakyThrows
    public String sendNotice(WxCpMessage message) {
        WxCpMessageService messageService = wxCpService.getMessageService();
        message.setAgentId(weComProperties.getAgentId());
        WxCpMessageSendResult result = messageService.send(message);
        return result.getMsgId();
    }

    /**
     * 撤回应用消息
     *
     * @param msgId 消息id
     */
    @SneakyThrows
    public void recallNotice(String msgId) {
        wxCpService.execute(new RecallNoticeRequestExecutor(), NOTICE_RECALL_URL, msgId);
    }

    /** 上传临时素材 */
    @SneakyThrows
    public String updatedMedia(InputStream inputStream, String mediaType) {
        WxCpMediaService mediaService = wxCpService.getMediaService();
        byte[] bytes = IoUtil.readBytes(inputStream);
        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes));
        WxMediaUploadResult result = mediaService.upload(mediaType, fileType, new ByteArrayInputStream(bytes));
        return result.getMediaId();
    }
}
