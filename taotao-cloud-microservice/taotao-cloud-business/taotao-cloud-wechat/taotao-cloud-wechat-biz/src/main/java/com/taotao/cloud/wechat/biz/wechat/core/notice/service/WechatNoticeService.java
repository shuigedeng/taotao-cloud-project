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

package com.taotao.cloud.wechat.biz.wechat.core.notice.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.dto.KeyValue;
import cn.bootx.starter.wechat.core.notice.dao.WeChatTemplateManager;
import cn.bootx.starter.wechat.core.notice.entity.WeChatTemplate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

/**
 * 微信消息通知功能
 *
 * @author xxm
 * @since 2022/7/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatNoticeService {
    private final WxMpService wxMpService;
    private final WeChatTemplateManager weChatTemplateManager;

    /** 发送模板信息 根据模板编号 */
    @SneakyThrows
    public String sentNotice(String code, String wxOpenId, List<KeyValue> keyValues) {
        WeChatTemplate weChatTemplate = weChatTemplateManager
                .findTemplateIdByCode(code)
                .orElseThrow(() -> new DataNotExistException("微信消息模板不存在"));
        return this.sentNoticeByTemplateId(weChatTemplate.getTemplateId(), wxOpenId, keyValues);
    }

    /** 发送模板信息 根据微信消息模板ID */
    @SneakyThrows
    public String sentNoticeByTemplateId(String templateId, String wxOpenId, List<KeyValue> keyValues) {
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
        WxMpTemplateMessage message = new WxMpTemplateMessage();
        message.setToUser(wxOpenId);
        message.setTemplateId(templateId);

        List<WxMpTemplateData> wxMpTemplateData = keyValues.stream()
                .map(keyValue -> new WxMpTemplateData(keyValue.getKey(), keyValue.getValue()))
                .toList();
        message.setData(wxMpTemplateData);
        return templateMsgService.sendTemplateMsg(message);
    }
}
